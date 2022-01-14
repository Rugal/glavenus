package ga.rugal.pt.springmvc.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import config.Constant;

import ga.rugal.glavenus.graphql.PostDto;
import ga.rugal.glavenus.openapi.api.PostApi;
import ga.rugal.pt.core.entity.Post;
import ga.rugal.pt.core.entity.User;
import ga.rugal.pt.core.service.PostService;
import ga.rugal.pt.core.service.UserService;
import ga.rugal.pt.springmvc.mapper.PostMapper;

import com.turn.ttorrent.bcodec.BeDecoder;
import com.turn.ttorrent.bcodec.BeEncoder;
import com.turn.ttorrent.bcodec.BeValue;
import com.turn.ttorrent.tracker.TrackedTorrent;
import io.swagger.annotations.Api;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Post controller.
 *
 * @author Rugal Bernstein
 */
@Api(tags = "PostController")
@RestController
@Slf4j
public class PostController implements PostApi {

  @javax.annotation.Resource(name = Constant.HOST)
  private String host;

  @javax.annotation.Resource(name = Constant.PORT)
  private int port;

  @Autowired
  @Setter
  private PostService postService;

  @Autowired
  @Setter
  private UserService userService;

  @Autowired
  private HttpServletRequest request;

  /**
   * Upload a torrent file against a post.
   *
   * @param pid  id of post
   * @param file the torrent file in multipart format
   *
   * @return HTTP response
   */
  @Override
  public ResponseEntity<Void> upload(final @PathVariable(Constant.PID) Integer pid,
                                     final @RequestPart(Constant.FILE) MultipartFile file) {
    final Optional<Post> optional = this.postService.getDao().findById(pid);
    if (optional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    Post db = optional.get();
    try {
      final TrackedTorrent torrent = new TrackedTorrent(file.getBytes());
      LOG.info("Get torrent name [{}] hash [{}] file length [{}]",
               file.getOriginalFilename(),
               torrent.getHexInfoHash(),
               torrent.getSize());

      db.setHash(torrent.getHexInfoHash());
      db.setTorrent(file.getBytes());
      db.setSize(torrent.getSize());
      db = this.postService.getDao().save(db);
    } catch (final NoSuchAlgorithmException ex) {
      LOG.error("SHA-1 algorithm is required to decrypt torrent file", ex);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    } catch (final IOException ex) {
      LOG.error("Unable to open/parse torrent file", ex);
      return ResponseEntity.unprocessableEntity().build();
    }

    final PostDto from = PostMapper.I.from(db);
    final URI location = ServletUriComponentsBuilder
      .fromCurrentRequest()
      .buildAndExpand(from.getPid()).toUri();
    return ResponseEntity
      .created(location)
      .build();
  }

  /**
   * Generate a user related announce URL.
   *
   * @param uid    User id
   * @param secret secret in plain text
   *
   * @return the announce URL
   */
  private String assembleAnnounceUrl(final int uid, final String secret) {
    return String.format(Constant.ANNOUNCE_TEMPLATE_URL,
                         this.host,
                         this.port,
                         uid,
                         BCrypt.hashpw(secret, BCrypt.gensalt()));
  }

  /**
   * Download the torrent file of a post.
   *
   * @param pid id of post
   *
   * @return HTTP response
   */
  @Override
  public ResponseEntity<Resource> download(final @PathVariable(Constant.PID) Integer pid) {
    final int uid = (Integer) this.request.getAttribute(Constant.UID);
    // make sure post
    final Optional<Post> optionalPost = this.postService.getDao().findById(pid);
    // user definitely exists as it passed AuthenticationInterceptor
    final Optional<User> optionalUser = this.userService.getDao().findById(uid);

    if (optionalPost.isEmpty() || optionalUser.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    final Post post = optionalPost.get();
    final User user = optionalUser.get();

    final ByteBuffer bencode;
    try {
      final Map<String, BeValue> origin = BeDecoder
        .bdecode(new ByteArrayInputStream(post.getTorrent())).getMap();
      origin.put("announce",
                 new BeValue(this.assembleAnnounceUrl(user.getUid(), user.getSecret())));
      bencode = BeEncoder.bencode(origin);
    } catch (final IOException ex) {
      LOG.error("Unable to encode/decode torrent", ex);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    return ResponseEntity.ok()
      .contentType(MediaType.parseMediaType(Constant.BITTORRENT_MIME))
      .header(HttpHeaders.CONTENT_DISPOSITION,
              String.format("attachment; filename=%s.torrent", post.getHash()))
      .body(new ByteArrayResource(bencode.array()));
  }
}
