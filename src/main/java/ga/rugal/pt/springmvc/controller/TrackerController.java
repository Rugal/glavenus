package ga.rugal.pt.springmvc.controller;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import config.Constant;
import config.SystemDefaultProperty;

import ga.rugal.pt.core.entity.Client;
import ga.rugal.pt.core.entity.User;
import ga.rugal.pt.core.service.ClientService;
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

  public TrackerController(final ConcurrentMap<String, TrackedTorrent> torrents) {
    super(torrents);
  }

  /**
   * Wrapper method for authenticating announce secret.
   *
   * @param parameters
   *
   * @return
   */
  private Optional<User> authenticate(final Map<String, BeValue> parameters) {
    try {
      //user id
      final int uid = parameters.get(Constant.UID).getInt();
      //password in plaintext
      final String secret = parameters.get(Constant.SECRET).getString();
      if (this.userService.canAnnounce(uid, secret)) {
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
      //Format /^-\w{2}\d{4}-\w{12}$/ for instance -AZ2060-XXXXXXXXXXXX
      final String rawPeerId = parameters.get(Constant.PEER_ID).getString();
      final Matcher matcher = Pattern.compile(SystemDefaultProperty.PEER_ID_PATTERN)
              .matcher(rawPeerId);
      if (!matcher.find()) {
        LOG.info("User [{}] has invalid peer_id [{}]", user.getUid(), rawPeerId);
        return false;
      }
      final String peerId = matcher.group(1);
      final Client client = this.clientService.findByPeerId(peerId.substring(0, 2),
                                                            peerId.substring(2));
      return client.getEnable();
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
    //TODO: update upload/download information in database
    return true;
  }
}
