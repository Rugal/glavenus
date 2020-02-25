package ga.rugal.pt.springmvc.graphql;

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

import config.Constant;

import ga.rugal.pt.core.entity.Post;
import ga.rugal.pt.core.entity.Review;
import ga.rugal.pt.core.entity.User;
import ga.rugal.pt.core.service.PostService;
import ga.rugal.pt.core.service.ReviewService;
import ga.rugal.pt.core.service.UserService;
import ga.rugal.pt.springmvc.graphql.type.torrent.Torrent;
import ga.rugal.pt.springmvc.mapper.TorrentMapper;
import ga.rugal.pt.springmvc.mapper.dto.PostPage;
import ga.rugal.pt.springmvc.mapper.dto.ReviewPage;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.google.common.collect.Lists;
import com.turn.ttorrent.tracker.TrackedTorrent;
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
  @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
  private ConcurrentMap<String, TrackedTorrent> torrents;

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
            .of(index, size, Sort.Direction.DESC, Constant.CREATE_AT));
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
                        PageRequest.of(index, size, Sort.Direction.DESC, Constant.CREATE_AT));
    return new ReviewPage(findAll.getContent(), size, index, findAll.getTotalPages());
  }

  /**
   * Get tracked torrent information.
   *
   * @param hash hash string
   *
   * @return torrent object that contains peers
   */
  public Torrent torrent(final String hash) {
    if (!this.torrents.containsKey(hash)) {
      return null;
    }
    final TrackedTorrent get = this.torrents.get(hash);
    return TorrentMapper.INSTANCE.to(get);
  }
}
