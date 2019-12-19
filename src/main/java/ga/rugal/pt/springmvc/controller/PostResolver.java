package ga.rugal.pt.springmvc.controller;

import ga.rugal.pt.core.entity.Post;
import ga.rugal.pt.core.entity.User;

import com.coxautodev.graphql.tools.GraphQLResolver;

/**
 * Post query resolver.
 *
 * @author Rugal Bernstein
 */
public class PostResolver implements GraphQLResolver<Post> {

  public User getAuthor(final Post p) {
    return p.getAuthor();
  }
}
