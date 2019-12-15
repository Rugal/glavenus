package ga.rugal.pt.core.service;

import javax.annotation.Nonnull;

import ga.rugal.pt.core.dao.UserDao;

public interface UserService extends BaseService<UserDao> {

  /**
   * Authenticate a user identity with uid and credential.<BR>
   *
   * @param uid        user id
   * @param credential The credential must be plain text
   *
   * @return {@code true} only if user exists and credential matches, otherwise return {@code false}
   */
  boolean authenticate(int uid, @Nonnull String credential);
}
