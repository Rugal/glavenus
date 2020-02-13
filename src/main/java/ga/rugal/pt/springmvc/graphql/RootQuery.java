package ga.rugal.pt.springmvc.graphql;

import java.util.Optional;

import ga.rugal.pt.core.entity.Post;
import ga.rugal.pt.core.entity.Review;
import ga.rugal.pt.core.entity.User;
import ga.rugal.pt.core.service.PostService;
import ga.rugal.pt.core.service.ReviewService;
import ga.rugal.pt.core.service.UserService;
import ga.rugal.pt.springmvc.mapper.dto.AuthData;
import ga.rugal.pt.springmvc.mapper.dto.PostPage;
import ga.rugal.pt.springmvc.mapper.dto.ReviewPage;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * GraphQL root query.
 *
 * @author Rugal Bernstein
 */
@Component
@Slf4j
public class RootQuery implements GraphQLQueryResolver {

  @Autowired
  private UserService userService;

  @Autowired
  private PostService postService;

  @Autowired
  private ReviewService reviewService;

  public Optional<User> user(final int uid) {
    return this.userService.getDao().findById(uid);
  }

  public Optional<Post> post(final int pid) {
    return this.postService.getDao().findById(pid);
  }

  /**
   * Get PostPage.
   *
   * @param size  page size, default as 20
   * @param index page index, start from 0
   *
   * @return PostPage object
   */
  public PostPage postPage(final int size, final int index) {
    final Page<Post> findAll = this.postService.getDao().findAll(PageRequest
            .of(index, size, Sort.Direction.DESC, "createAt"));
    return new PostPage(findAll.getContent(), size, index, findAll.getTotalPages());
  }

  /**
   * Get ReviewPage for a post.
   *
   * @param pid   pid for post
   * @param size  page size, default as 20
   * @param index page index, start from 0
   *
   * @return ReviewPage object
   */
  public ReviewPage reviewPage(final int pid, final int size, final int index) {
    final Optional<Post> optional = this.postService.getDao().findById(pid);
    if (optional.isEmpty()) {
      return new ReviewPage(Lists.newArrayList(), size, 0, 0);
    }

    final Page<Review> findAll = this.reviewService.getDao()
            .findByPost(optional.get(),
                        PageRequest.of(index, size, Sort.Direction.DESC, "createAt"));
    return new ReviewPage(findAll.getContent(), size, index, findAll.getTotalPages());
  }

  /**
   * query { test(data: { uid: 1, username: "Rugal" }) }.
   *
   * @param data test
   *
   * @return test
   */
  public Boolean test(final AuthData data) {
    LOG.error("uid: [{}]", data.getUid());
    return null;
  }
}
