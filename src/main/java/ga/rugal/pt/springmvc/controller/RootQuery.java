package ga.rugal.pt.springmvc.controller;

import java.util.Optional;

import ga.rugal.pt.core.entity.User;
import ga.rugal.pt.core.service.UserService;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * GraphQL root query for Course.
 *
 * @author Rugal Bernstein
 */
@Component
public class RootQuery implements GraphQLQueryResolver {

  @Autowired
  private UserService userService;

  public Optional<User> getUser(final int uid) {
    return this.userService.getDao().findById(uid);
  }
}
