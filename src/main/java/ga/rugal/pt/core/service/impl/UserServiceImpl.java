package ga.rugal.pt.core.service.impl;

import java.util.Optional;
import javax.annotation.Nonnull;

import ga.rugal.pt.core.dao.UserDao;
import ga.rugal.pt.core.entity.User;
import ga.rugal.pt.core.service.UserService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

  @Autowired
  @Getter
  @Setter
  private UserDao dao;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean authenticate(final int uid, final @Nonnull String password) {
    final Optional<User> optional = this.dao.findById(uid);
    if (optional.isEmpty()) {
      return false;
    }
    return BCrypt.checkpw(password, optional.get().getPassword());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canAnnounce(final int uid, final @Nonnull String secret) {
    final Optional<User> optional = this.dao.findById(uid);
    if (optional.isEmpty()) {
      return false;
    }
    return BCrypt.checkpw(optional.get().getSecret(), secret);
  }
}
