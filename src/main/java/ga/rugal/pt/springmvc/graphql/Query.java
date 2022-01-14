package ga.rugal.pt.springmvc.graphql;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

import config.Constant;

import ga.rugal.glavenus.graphql.PostDto;
import ga.rugal.glavenus.graphql.PostPageDto;
import ga.rugal.glavenus.graphql.QueryResolver;
import ga.rugal.glavenus.graphql.ReviewPageDto;
import ga.rugal.glavenus.graphql.TagDto;
import ga.rugal.glavenus.graphql.TorrentDto;
import ga.rugal.glavenus.graphql.UserDto;
import ga.rugal.pt.core.entity.Post;
import ga.rugal.pt.core.entity.Review;
import ga.rugal.pt.core.entity.User;
import ga.rugal.pt.core.service.PostService;
import ga.rugal.pt.core.service.ReviewService;
import ga.rugal.pt.core.service.TagService;
import ga.rugal.pt.core.service.UserService;
import ga.rugal.pt.springmvc.graphql.exception.PostNotFoundException;
import ga.rugal.pt.springmvc.graphql.exception.UnauthenticatedException;
import ga.rugal.pt.springmvc.graphql.exception.UserNotFoundException;
import ga.rugal.pt.springmvc.mapper.PostMapper;
import ga.rugal.pt.springmvc.mapper.PostPageMapper;
import ga.rugal.pt.springmvc.mapper.ReviewPageMapper;
import ga.rugal.pt.springmvc.mapper.TagMapper;
import ga.rugal.pt.springmvc.mapper.TorrentMapper;
import ga.rugal.pt.springmvc.mapper.UserMapper;
import ga.rugal.pt.springmvc.mapper.dto.PostPage;
import ga.rugal.pt.springmvc.mapper.dto.ReviewPage;

import com.turn.ttorrent.tracker.TrackedTorrent;
import graphql.kickstart.tools.GraphQLQueryResolver;
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
public class Query implements GraphQLQueryResolver, QueryResolver {

  @Autowired
  @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
  private ConcurrentMap<String, TrackedTorrent> torrents;

  @Autowired
  private TagService tagService;

  @Autowired
  private UserService userService;

  @Autowired
  private PostService postService;

  @Autowired
  private ReviewService reviewService;

  @Override
  public PostDto getPost(final Integer pid) throws Exception {
    return PostMapper.I.from(this.postService.getDao().findById(pid).orElse(null));
  }

  /**
   * Get Post by page.
   *
   * @param size  size of a page, default to 20
   * @param index 0 based index of page to fetch, default to 0
   *
   * @return PostPage object
   *
   * @throws Exception while fetching
   */
  @Override
  public PostPageDto getPostPage(final Integer size, final Integer index) throws Exception {
    final Page<Post> findAll = this.postService.getDao().findAll(PageRequest
      .of(index, size, Sort.Direction.DESC, Constant.CREATE_AT));
    return PostPageMapper.I.from(new PostPage(findAll.getContent(),
                                              size,
                                              index,
                                              findAll.getTotalPages()));
  }

  @Override
  public TagDto getTag(final Integer tid) throws Exception {
    return TagMapper.I.from(this.tagService.getDao().findById(tid).orElse(null));
  }

  @Override
  public List<TagDto> findTagByName(final String name) throws Exception {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   * Get ReviewPage for a post.
   *
   * @param pid   pid for post
   * @param size  page size, default as 20
   * @param index page index, start from 0
   *
   * @return ReviewPage object
   *
   * @throws Exception while fetching
   */
  @Override
  public ReviewPageDto getReviewPage(final Integer pid, final Integer size, final Integer index)
    throws Exception {
    final Optional<Post> optional = this.postService.getDao().findById(pid);
    if (optional.isEmpty()) {
      throw new PostNotFoundException();
    }

    final Page<Review> findAll = this.reviewService.getDao()
      .findByPost(optional.get(),
                  PageRequest.of(index, size, Sort.Direction.DESC, Constant.CREATE_AT));
    return ReviewPageMapper.I.from(new ReviewPage(findAll.getContent(),
                                                  size,
                                                  index,
                                                  findAll.getTotalPages()));
  }

  @Override
  public UserDto getUser(final Integer uid) throws Exception {
    return UserMapper.I.from(this.userService.getDao().findById(uid).orElse(null));
  }

  @Override
  public String login(final String email, final String password) throws Exception {
    final Optional<User> optional = this.userService.getDao().findByEmail(email);
    if (optional.isEmpty()) {
      throw new UserNotFoundException();
    }
    final Optional<String> jwt = this.userService.login(optional.get().getUid(), password);
    if (jwt.isEmpty()) {
      throw new UnauthenticatedException();
    }
    // Create jwt
    return jwt.get();
  }

  /**
   * Get tracked torrent information.
   *
   * @param hash hash string
   *
   * @return torrent object that contains peers
   *
   * @throws Exception while fetching
   */
  @Override
  public TorrentDto getTorrent(final String hash) throws Exception {
    if (!this.torrents.containsKey(hash)) {
      return null;
    }
    return TorrentMapper.I.from(this.torrents.getOrDefault(hash, null));
  }
}
