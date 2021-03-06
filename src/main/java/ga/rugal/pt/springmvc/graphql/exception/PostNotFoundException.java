package ga.rugal.pt.springmvc.graphql.exception;

import java.util.List;
import java.util.Map;

import config.Constant;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

/**
 * Indicate the specified post does not exist.
 *
 * @author Rugal
 */
public class PostNotFoundException extends RuntimeException implements GraphQLError {

  @Override
  public List<SourceLocation> getLocations() {
    return null;
  }

  @Override
  public ErrorType getErrorType() {
    return ErrorType.ValidationError;
  }

  @Override
  public Map<String, Object> getExtensions() {
    return Map.of(Constant.CODE, 404);
  }
}
