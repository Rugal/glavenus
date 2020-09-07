package ga.rugal.pt.springmvc.graphql;

import java.util.List;

import ga.rugal.glavenus.graphql.PostDto;
import ga.rugal.glavenus.graphql.PostResolver;
import ga.rugal.glavenus.graphql.PostTagDto;
import ga.rugal.glavenus.graphql.UserDto;
import ga.rugal.pt.springmvc.mapper.PostMapper;
import ga.rugal.pt.springmvc.mapper.UserMapper;

import com.coxautodev.graphql.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

/**
 * Field resolver for Post.
 *
 * @author Rugal Bernstein
 */
@Component
public class PostFieldResolver implements GraphQLResolver<PostDto>, PostResolver {

  @Override
  public UserDto author(final PostDto postDto) throws Exception {
    return UserMapper.I.from(PostMapper.I.to(postDto).getAuthor());
  }

  @Override
  public List<PostTagDto> tags(final PostDto postDto) throws Exception {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
