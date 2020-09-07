package ga.rugal.pt.springmvc.graphql;

import ga.rugal.glavenus.graphql.PostDto;
import ga.rugal.glavenus.graphql.PostTagDto;
import ga.rugal.glavenus.graphql.PostTagResolver;
import ga.rugal.glavenus.graphql.TagDto;

import com.coxautodev.graphql.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

/**
 * Field resolver for PostTag.
 *
 * @author Rugal Bernstein
 */
@Component
public class PostTagFieldResolver implements GraphQLResolver<PostTagDto>, PostTagResolver {

  @Override
  public PostDto post(final PostTagDto postTagDto) throws Exception {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public TagDto tag(final PostTagDto postTagDto) throws Exception {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
