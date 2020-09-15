package ga.rugal.pt.core.service;

import java.util.Optional;
import javax.annotation.Nonnull;

import ga.rugal.pt.core.dao.UserDao;
import ga.rugal.pt.core.entity.User;

public interface UserService extends BaseService<UserDao> {

  /**
   * Authenticate an user identity with uid and password for login.
   *
   * @param uid      User id
   * @param password The password must be plain text
   *
   * @return {@code true} only if user exists and password matches, otherwise return {@code false}
   */
  boolean authenticate(int uid, @Nonnull String password);

  /**
   * Authenticate an user identity with uid and secret for announce.
   *
   * @param uid    User id
   * @param secret The secret must be cipher text
   *
   * @return {@code true} only if user exists and secret matches, otherwise return {@code false}
   */
  boolean announce(int uid, @Nonnull String secret);

  /**
   * Login user with uid and password, and return JWT if everything looks good.
   *
   * @param uid      user id
   * @param password user password in plain text
   *
   * @return JWT
   */
  Optional<String> login(int uid, @Nonnull String password);

  /**
   * Create new user, will encrypt the plain text password.
   *
   * @param user information with plain text password
   *
   * @return newly created user with encrypted password
   */
  User save(User user);
}
