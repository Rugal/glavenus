package ga.rugal.pt.springmvc.controller;

import java.io.IOException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import ga.rugal.pt.core.entity.Post;
import ga.rugal.pt.core.service.PostService;
import ga.rugal.pt.openapi.api.PostApi;
import ga.rugal.pt.openapi.model.NewPostDto;
import ga.rugal.pt.openapi.model.PostDto;
import ga.rugal.pt.springmvc.mapper.PostMapper;

import com.turn.ttorrent.tracker.TrackedTorrent;
import io.swagger.annotations.Api;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Course controller.
 *
 * @author Rugal Bernstein
 */
@Api(tags = "PostController")
@RestController
@Slf4j
public class PostController implements PostApi {

  private static final String PID = "pid";

  private static final String FILE = "file";

  @Autowired
  @Setter
  private PostService postService;

  @Override
  public ResponseEntity<PostDto> create(final @RequestBody NewPostDto newPostDto) {
    final Post save = this.postService.getDao().save(PostMapper.INSTANCE.to(newPostDto));
    final PostDto from = PostMapper.INSTANCE.from(save);
    final URI location = ServletUriComponentsBuilder
            .fromCurrentRequest().path("/{id}")
            .buildAndExpand(from.getPid()).toUri();

    return ResponseEntity
            .created(location)
            .body(from);
  }

  @Override
  public ResponseEntity<Void> delete(final @PathVariable(PID) Integer pid) {
    if (!this.postService.getDao().existsById(pid)) {
      return ResponseEntity.notFound().build();
    }
    this.postService.getDao().deleteById(pid);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<PostDto> get(final @PathVariable(PID) Integer pid) {

    final Optional<Post> findById = this.postService.getDao().findById(pid);

    return findById.isEmpty()
           ? ResponseEntity.notFound().build()
           : ResponseEntity.ok(PostMapper.INSTANCE.from(findById.get()));
  }

  @Override
  public ResponseEntity<PostDto> update(final @PathVariable(PID) Integer pid,
                                        final @RequestBody NewPostDto newPostDto) {
    if (!this.postService.getDao().existsById(pid)) {
      return ResponseEntity.notFound().build();
    }

    final Post to = PostMapper.INSTANCE.to(newPostDto);
    to.setPid(pid);
    return ResponseEntity.ok(PostMapper.INSTANCE.from(this.postService.getDao().save(to)));
  }

  @Override
  public ResponseEntity<PostDto> upload(final @PathVariable(PID) Integer pid,
                                        final @RequestPart(FILE) MultipartFile file) {
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
    final PostDto from = PostMapper.INSTANCE.from(db);
    final URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .buildAndExpand(from.getPid()).toUri();
    return ResponseEntity
            .created(location)
            .body(from);
  }
}
