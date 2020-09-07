package ga.rugal.pt.springmvc.graphql;

import ga.rugal.glavenus.graphql.ReviewDto;
import ga.rugal.glavenus.graphql.ReviewResolver;
import ga.rugal.glavenus.graphql.UserDto;

import com.coxautodev.graphql.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

/**
 * Field resolver for Review.
 *
 * @author Rugal Bernstein
 */
@Component
public class ReviewFieldResolver implements GraphQLResolver<ReviewDto>, ReviewResolver {

  @Override
  public UserDto author(final ReviewDto reviewDto) throws Exception {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
