package ga.rugal.pt.springmvc.graphql;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import config.Constant;

import ga.rugal.glavenus.graphql.MutationResolver;
import ga.rugal.glavenus.graphql.NewPostDto;
import ga.rugal.glavenus.graphql.NewTagDto;
import ga.rugal.glavenus.graphql.NewUserDto;
import ga.rugal.glavenus.graphql.PostDto;
import ga.rugal.glavenus.graphql.PostTagDto;
import ga.rugal.glavenus.graphql.TagDto;
import ga.rugal.glavenus.graphql.UserDto;
import ga.rugal.pt.core.entity.Post;
import ga.rugal.pt.core.entity.PostTag;
import ga.rugal.pt.core.entity.Tag;
import ga.rugal.pt.core.service.PostService;
import ga.rugal.pt.core.service.PostTagService;
import ga.rugal.pt.core.service.TagService;
import ga.rugal.pt.core.service.UserService;
import ga.rugal.pt.springmvc.graphql.exception.EmailDuplicatedException;
import ga.rugal.pt.springmvc.graphql.exception.PostNotFoundException;
import ga.rugal.pt.springmvc.graphql.exception.PostTagNotFoundException;
import ga.rugal.pt.springmvc.graphql.exception.TagNotFoundException;
import ga.rugal.pt.springmvc.mapper.PostMapper;
import ga.rugal.pt.springmvc.mapper.PostTagMapper;
import ga.rugal.pt.springmvc.mapper.TagMapper;
import ga.rugal.pt.springmvc.mapper.UserMapper;

import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The root mutation class for GraphQL.
 *
 * @author Rugal
 */
@Component
@Slf4j
public class Mutation implements GraphQLMutationResolver, MutationResolver {

  @Autowired
  private HttpServletRequest request;

  @Autowired
  @Setter
  private PostService postService;

  @Autowired
  @Setter
  private UserService userService;

  @Autowired
  @Setter
  private TagService tagService;

  @Autowired
  @Setter
  private PostTagService postTagService;

  @Override
  public TagDto createTag(final NewTagDto input) throws Exception {
    return TagMapper.I.from(this.tagService.getDao().save(TagMapper.I.to(input)));
  }

  /**
   * Create new post with title and content filled.
   *
   * @param input new post object
   *
   * @return created post object
   *
   * @throws Exception other exception during execution
   */
  @Override
  public PostDto createPost(final NewPostDto input) throws Exception {
    final int uid = (Integer) this.request.getAttribute(Constant.UID);
    // user must exist as it passed AuthenticationInterceptor
    // 1. create post
    final Post newPost = PostMapper.I.to(input);
    // 2. set author
    newPost.setAuthor(this.userService.getDao().findById(uid).get());

    return PostMapper.I.from(this.postService.getDao().save(newPost));
  }

  @Override
  public UserDto createUser(final NewUserDto input) throws Exception {
    if (this.userService.getDao().existsByEmail(input.getEmail())) {
      // Duplicate email
      throw new EmailDuplicatedException();
    }
    return UserMapper.I.from(this.userService.save(UserMapper.I.to(input)));
  }

  @Override
  public PostTagDto attach(final Integer pid, final Integer tid) throws Exception {
    final Optional<Post> optionalPost = this.postService.getDao().findById(pid);
    if (optionalPost.isEmpty()) {
      throw new PostNotFoundException();
    }
    final Optional<Tag> optionalTag = this.tagService.getDao().findById(tid);
    if (optionalTag.isEmpty()) {
      throw new TagNotFoundException();
    }
    final PostTag postTag = new PostTag();
    postTag.setPost(optionalPost.get());
    postTag.setTag(optionalTag.get());

    return PostTagMapper.I.from(this.postTagService.getDao().save(postTag));
  }

  /**
   * Detach a tag from post.
   *
   * @param ptid the id of a PostTag
   *
   * @return {@code true} is deletion successful, otherwise return {@code false}
   *
   * @throws Exception any other exception during execution
   */
  @Override
  public Boolean detach(final Integer ptid) throws Exception {
    final Optional<PostTag> optional = this.postTagService.getDao().findById(ptid);
    // PostTag not found
    if (optional.isEmpty()) {
      throw new PostTagNotFoundException();
    }
    this.postTagService.getDao().delete(optional.get());
    return true;
  }

  @Override
  public Boolean deletePost(final Integer pid) throws Exception {
    if (!this.postService.getDao().existsById(pid)) {
      return false;
    }
    this.postService.getDao().deleteById(pid);
    return true;
  }

  @Override
  public PostDto updatePost(final Integer pid, final NewPostDto input) throws Exception {
    if (!this.postService.getDao().existsById(pid)) {
      throw new PostNotFoundException();
    }

    final Post to = PostMapper.I.to(input);
    to.setPid(pid);
    return PostMapper.I.from(this.postService.getDao().save(to));
  }
}
