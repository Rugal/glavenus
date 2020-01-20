package ga.rugal.pt.springmvc.controller;

import java.util.Optional;

import ga.rugal.pt.core.entity.Post;
import ga.rugal.pt.core.entity.User;
import ga.rugal.pt.core.service.PostService;
import ga.rugal.pt.core.service.UserService;
import ga.rugal.pt.springmvc.mapper.dto.PostPage;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * GraphQL root query for Course.
 *
 * @author Rugal Bernstein
 */
@Component
public class RootQuery implements GraphQLQueryResolver {

  @Autowired
  private UserService userService;

  @Autowired
  private PostService postService;

  public Optional<User> getUser(final int uid) {
    return this.userService.getDao().findById(uid);
  }

  public Optional<Post> getPost(final int pid) {
    return this.postService.getDao().findById(pid);
  }

  public PostPage postPage(final int size, final int index) {
    final Page<Post> findAll = this.postService.getDao().findAll(PageRequest
            .of(index, size, Sort.Direction.DESC, "createAt"));
    return new PostPage(findAll.getContent(), size, index, findAll.getTotalPages());
  }
}
