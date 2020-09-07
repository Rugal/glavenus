package ga.rugal.pt.core.service.impl;

import java.util.Optional;
import javax.annotation.Nonnull;

import ga.rugal.pt.core.dao.UserDao;
import ga.rugal.pt.core.entity.User;
import ga.rugal.pt.core.service.JwtService;
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

  @Autowired
  @Getter
  @Setter
  private JwtService jwtService;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean authenticate(final int uid, final @Nonnull String password) {
    final Optional<User> optional = this.dao.findById(uid);
    if (optional.isEmpty()) {
      return false;
    }
    try {
      return BCrypt.checkpw(password, optional.get().getPassword());
    } catch (final IllegalArgumentException e) {
      LOG.error("Unable to check with BCrypt", e);
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<String> login(final int uid, final @Nonnull String password) {
    return this.authenticate(uid, password)
           ? Optional.of(this.jwtService.encrypt(this.dao.findById(uid).get()))
           : Optional.empty();
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
    try {
      return BCrypt.checkpw(optional.get().getSecret(), secret);
    } catch (final IllegalArgumentException e) {
      LOG.error("Unable to check with BCrypt", e);
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public User save(final User user) {
    user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
    return this.dao.save(user);
  }
}
