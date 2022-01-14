package ga.rugal.pt.springmvc.controller;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import config.Constant;
import config.SystemDefaultProperty;

import ga.rugal.pt.core.entity.Announce;
import ga.rugal.pt.core.entity.Client;
import ga.rugal.pt.core.entity.Post;
import ga.rugal.pt.core.entity.User;
import ga.rugal.pt.core.service.AnnounceService;
import ga.rugal.pt.core.service.ClientService;
import ga.rugal.pt.core.service.PostService;
import ga.rugal.pt.core.service.UserService;

import com.turn.ttorrent.bcodec.BeValue;
import com.turn.ttorrent.bcodec.InvalidBEncodingException;
import com.turn.ttorrent.tracker.TrackedPeer;
import com.turn.ttorrent.tracker.TrackedTorrent;
import com.turn.ttorrent.tracker.TrackerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Custom implementation of TrackerService.
 *
 * @author rugal
 */
@Slf4j
public class TrackerController extends TrackerService {

  @Autowired
  private UserService userService;

  @Autowired
  private ClientService clientService;

  @Autowired
  private PostService postService;

  @Autowired
  private AnnounceService announceService;

  public TrackerController(final ConcurrentMap<String, TrackedTorrent> torrents) {
    super(torrents);
  }

  /**
   * Wrapper method for authenticating announce secret.
   *
   * @param parameters
   *
   * @return authenticate user identity
   */
  private Optional<User> authenticate(final Map<String, BeValue> parameters) {
    try {
      //user id
      final int uid = parameters.get(Constant.UID).getInt();
      //password in plaintext
      final String secret = parameters.get(Constant.SECRET).getString();
      if (this.userService.announce(uid, secret)) {
        LOG.info("Matched uid [{}] and secret [{}]", uid, secret);
        return this.userService.getDao().findById(uid);
      }
      LOG.info("Mismatched uid [{}] and secret [{}]", uid, secret);
    } catch (final InvalidBEncodingException ex) {
      LOG.error("Invalid encoding for uid secret", ex);
    }
    return Optional.empty();
  }

  /**
   * Get first part of peer_id.<BR>
   * Format {@code /^-\w{2}\d{4}-\w{12}$/} for instance -AZ2060-XXXXXXXXXXXX
   *
   * @param user      user object
   * @param rawPeerId the raw peer_id that is 20 bytes long
   *
   * @return first part of peer_id if there is a valid one, otherwise return empty
   */
  private Optional<String> extractPeerId(final User user, final String rawPeerId) {
    final Matcher matcher = Pattern.compile(SystemDefaultProperty.PEER_ID_PATTERN)
            .matcher(rawPeerId);
    if (!matcher.find()) {
      LOG.info("User [{}] has invalid peer_id [{}]", user.getUid(), rawPeerId);
      return Optional.empty();
    }
    final String peerId = matcher.group(1);
    LOG.info("User [{}] has valid peer_id [{}] extract client_id [{}]",
             user.getUid(),
             rawPeerId,
             peerId);
    return Optional.of(peerId);
  }

  private Optional<Client> getClient(final User user, final Map<String, BeValue> parameters) {
    final String rawPeerId;
    try {
      rawPeerId = parameters.get(Constant.PEER_ID).getString();
    } catch (final InvalidBEncodingException ex) {
      LOG.info("User [{}] has invalid peer_id encoding", user.getUid());
      return Optional.empty();
    }
    final Optional<String> peerId = this.extractPeerId(user, rawPeerId);
    if (peerId.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(this.clientService.findByPeerId(peerId.get()));
  }

  /**
   * Validate client by checking peer_id from user announce request.
   *
   * @param user       user object
   * @param parameters all request parameters
   *
   * @return true if the client exists and enabled, otherwise return false
   */
  private boolean validateClient(final User user, final Map<String, BeValue> parameters) {
    if (!parameters.containsKey(Constant.PEER_ID)) {
      LOG.info("User [{}] does not have peer_id [{}]", user.getUid());
      return false;
    }
    try {
      final Optional<String> peerId = this.extractPeerId(user,
                                                         parameters.get(Constant.PEER_ID)
                                                                 .getString());
      if (peerId.isEmpty()) {
        return false;
      }
      return this.clientService.findByPeerId(peerId.get()).getEnable();
    } catch (final InvalidBEncodingException ex) {
      LOG.info("User [{}] has invalid peer_id encoding", user.getUid());
      return false;
    }
  }

  @Override
  protected boolean beforeUpdate(final TrackedTorrent torrent,
                                 final Map<String, BeValue> parameters) {
    final Optional<User> optionalUser = this.authenticate(parameters);
    if (optionalUser.isEmpty()) {
      return false;
    }
    final User user = optionalUser.get();
    // deny access if invalid user status(like banned etc.,)
    if (!user.isValid()) {
      LOG.info("User [{}] has non-valid status [{}]", user.getUid(), user.getStatus());
      return false;
    }

    return this.validateClient(user, parameters);
  }

  @Override
  protected boolean afterUpdate(final TrackedTorrent torrent,
                                final TrackedPeer peer,
                                final Map<String, BeValue> parameters) {
    // Get user, guarantee to have user so do not check emptiness
    final User user = this.userService.getDao().findById(peer.getUid()).get();
    // Check post existence
    final Optional<Post> optionalPost = this.postService.getDao()
            .findByHash(torrent.getHexInfoHash());
    if (optionalPost.isEmpty()) {
      LOG.debug("User [{}] announce against nonexist post", user.getUid());
      return false;
    }
    final Post post = optionalPost.get();
    // Get client
    final Optional<Client> client = this.getClient(user, parameters);
    if (client.isEmpty()) {
      LOG.debug("User [{}] announce by invalid client", user.getUid());
      return false;
    }
    final long downloadDifference = peer.getDownloaded() - peer.getLastDownloaded();
    final long uploadDifference = peer.getUploaded() - peer.getLastUploaded();

    Announce build = Announce.builder()
            .download(downloadDifference)
            .upload(uploadDifference)
            .client(client.get())
            .user(user)
            .post(post)
            .build();
    build = this.announceService.getDao().save(build);
    LOG.debug("User [{}] announced {}", user.getUid(), build);
    return true;
  }
}
