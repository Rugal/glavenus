package ga.rugal.pt.core.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import ga.rugal.UnitTestBase;
import ga.rugal.pt.core.dao.UserDao;
import ga.rugal.pt.core.entity.User;
import ga.rugal.pt.core.service.JwtService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

public class UserServiceImplTest extends UnitTestBase {

  private final UserServiceImpl service = new UserServiceImpl();

  @Autowired
  private User user;

  @MockBean
  private UserDao dao;

  @MockBean
  private JwtService jwtService;

  @BeforeEach
  public void setUp() {
    this.service.setDao(this.dao);
    this.service.setJwtService(this.jwtService);

    when(this.dao.findById(any())).thenReturn(Optional.of(this.user));

    when(this.jwtService.encrypt(any())).thenReturn("");
  }

  @Test
  public void authenticate_not_found() {
    when(this.dao.findById(any())).thenReturn(Optional.empty());

    Assertions.assertFalse(this.service.authenticate(1, ""));

    verify(this.dao).findById(any());
  }

  @Test
  public void authenticate_true() {
    Assertions.assertTrue(this.service.authenticate(1, "1"));

    verify(this.dao).findById(any());
  }

  @Test
  public void login_not_found() {
    when(this.dao.findById(any())).thenReturn(Optional.empty());

    Assertions.assertFalse(this.service.login(1, "1").isPresent());

    verify(this.dao).findById(any());
    verify(this.jwtService, never()).encrypt(any());
  }

  @Test
  public void login_false() {
    Assertions.assertFalse(this.service.login(1, "0").isPresent());

    verify(this.dao).findById(any());
    verify(this.jwtService, never()).encrypt(any());
  }

  @Test
  public void login_true() {
    Assertions.assertTrue(this.service.login(1, "1").isPresent());

    verify(this.dao, times(2)).findById(any());
    verify(this.jwtService).encrypt(any());
  }

  @Test
  public void announce_false() {
    when(this.dao.findById(any())).thenReturn(Optional.empty());

    Assertions.assertFalse(this.service.announce(1, ""));

    verify(this.dao).findById(any());
  }

  @Test
  public void announce_true() {
    Assertions.assertTrue(this.service.announce(1, BCrypt.hashpw("1", BCrypt.gensalt())));

    verify(this.dao).findById(any());
  }

  @Test
  public void save() {
    this.service.save(this.user);

    verify(this.dao).save(any());
  }
}
