package ga.rugal.pt.springmvc.graphql;

import java.util.List;
import java.util.stream.Collectors;

import ga.rugal.glavenus.graphql.PostDto;
import ga.rugal.glavenus.graphql.PostResolver;
import ga.rugal.glavenus.graphql.TagDto;
import ga.rugal.glavenus.graphql.UserDto;
import ga.rugal.pt.core.entity.Post;
import ga.rugal.pt.core.service.PostService;
import ga.rugal.pt.core.service.PostTagService;
import ga.rugal.pt.springmvc.mapper.TagMapper;
import ga.rugal.pt.springmvc.mapper.UserMapper;

import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Field resolver for Post.
 *
 * @author Rugal Bernstein
 */
@Component
public class PostFieldResolver implements GraphQLResolver<PostDto>, PostResolver {

  @Autowired
  private PostService postService;

  @Autowired
  private PostTagService postTagService;

  @Override
  public UserDto author(final PostDto postDto) throws Exception {
    final Post p = this.postService.getDao().findById(postDto.getPid()).get();
    return UserMapper.I.from(p.getAuthor());
  }

  @Override
  public List<TagDto> tags(final PostDto postDto) throws Exception {
    final Post p = this.postService.getDao().findById(postDto.getPid()).get();
    return this.postTagService.getDao().findByPost(p).stream()
      .map(pt -> TagMapper.I.from(pt.getTag()))
      .collect(Collectors.toList());
  }
}
