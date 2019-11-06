package ga.rugal.pt.springmvc.controller;

import java.net.URI;
import java.util.Optional;

import ga.rugal.pt.core.entity.Post;
import ga.rugal.pt.core.service.PostService;
import ga.rugal.pt.openapi.api.PostApi;
import ga.rugal.pt.openapi.model.NewPostDto;
import ga.rugal.pt.openapi.model.PostDto;
import ga.rugal.pt.springmvc.mapper.PostMapper;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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

  @Autowired
  private PostService postService;

  @Override
  public ResponseEntity<PostDto> createPost(final @RequestBody NewPostDto newPostDto) {
    final Post save = this.postService.getDao().save(PostMapper.INSTANCE.to(newPostDto));
    final PostDto from = PostMapper.INSTANCE.from(save);
    final URI location = ServletUriComponentsBuilder
            .fromCurrentRequest().path("/post/{id}")
            .buildAndExpand(from.getPid()).toUri();

    return ResponseEntity
            .created(location)
            .body(from);
  }

  @Override
  public ResponseEntity<Void> deletePost(final @PathVariable("pid") Integer pid) {
    if (!this.postService.getDao().existsById(pid)) {
      return ResponseEntity.notFound().build();
    }
    this.postService.getDao().deleteById(pid);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<PostDto> getPost(final @PathVariable("pid") Integer pid) {

    final Optional<Post> findById = this.postService.getDao().findById(pid);

    return findById.isEmpty()
           ? ResponseEntity.notFound().build()
           : ResponseEntity.ok(PostMapper.INSTANCE.from(findById.get()));
  }

  @Override
  public ResponseEntity<PostDto> updatePost(final @PathVariable("pid") Integer pid,
                                            final @RequestBody NewPostDto newPostDto) {
    if (!this.postService.getDao().existsById(pid)) {
      return ResponseEntity.notFound().build();
    }

    final Post to = PostMapper.INSTANCE.to(newPostDto);
    to.setPid(pid);
    return ResponseEntity.ok(PostMapper.INSTANCE.from(this.postService.getDao().save(to)));
  }
}
