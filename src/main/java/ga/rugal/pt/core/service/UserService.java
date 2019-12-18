package ga.rugal.pt.core.service;

import javax.annotation.Nonnull;

import ga.rugal.pt.core.dao.UserDao;

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
   * Authenticate an user identity with uid and secret for announce.<BR>
   *
   * @param uid    User id
   * @param secret The secret must be cipher text
   *
   * @return {@code true} only if user exists and secret matches, otherwise return {@code false}
   */
  boolean canAnnounce(int uid, @Nonnull String secret);
}
