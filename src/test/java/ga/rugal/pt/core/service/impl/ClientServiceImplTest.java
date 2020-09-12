package ga.rugal.pt.core.service.impl;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import config.Constant;

import ga.rugal.UnitTestBase;
import ga.rugal.pt.core.dao.ClientDao;
import ga.rugal.pt.core.entity.Client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

public class ClientServiceImplTest extends UnitTestBase {

  private final ClientServiceImpl service = new ClientServiceImpl();

  private static final String CLIENT_NAME = "UT";

  private static final String CLIENT_VERSION = "1.0";

  @MockBean
  private ClientDao dao;

  @Autowired
  private Client client;

  @BeforeEach
  public void setUp() {
    this.service.setDao(this.dao);

    when(this.dao.findByNameAndVersion(eq(CLIENT_NAME), eq(CLIENT_VERSION)))
      .thenReturn(Optional.of(this.client));

    when(this.dao.findByNameAndVersion(eq(CLIENT_NAME), eq(Constant.STAR)))
      .thenReturn(Optional.of(this.client));

    when(this.dao.findByNameAndVersion(eq(Constant.STAR), eq(Constant.STAR)))
      .thenReturn(Optional.of(this.client));
  }

  @Test
  public void findByPeerId_specific() {
    this.service.findByPeerId(CLIENT_NAME, CLIENT_VERSION);

    verify(this.dao).findByNameAndVersion(CLIENT_NAME, CLIENT_VERSION);
    verify(this.dao, never()).findByNameAndVersion(CLIENT_NAME, Constant.STAR);
    verify(this.dao, never()).findByNameAndVersion(Constant.STAR, Constant.STAR);
  }

  @Test
  public void findByPeerId_client_specific() {
    when(this.dao.findByNameAndVersion(eq(CLIENT_NAME), eq(CLIENT_VERSION)))
      .thenReturn(Optional.empty());

    this.service.findByPeerId(CLIENT_NAME, CLIENT_VERSION);

    verify(this.dao).findByNameAndVersion(CLIENT_NAME, CLIENT_VERSION);
    verify(this.dao).findByNameAndVersion(CLIENT_NAME, Constant.STAR);
    verify(this.dao, never()).findByNameAndVersion(Constant.STAR, Constant.STAR);
  }

  @Test
  public void findByPeerId_generic() {
    when(this.dao.findByNameAndVersion(eq(CLIENT_NAME), eq(CLIENT_VERSION)))
      .thenReturn(Optional.empty());
    when(this.dao.findByNameAndVersion(eq(CLIENT_NAME), eq(Constant.STAR)))
      .thenReturn(Optional.empty());

    this.service.findByPeerId(CLIENT_NAME, CLIENT_VERSION);

    verify(this.dao).findByNameAndVersion(CLIENT_NAME, CLIENT_VERSION);
    verify(this.dao).findByNameAndVersion(CLIENT_NAME, Constant.STAR);
    verify(this.dao).findByNameAndVersion(Constant.STAR, Constant.STAR);
  }

  @Test
  public void findByPeerId() {
    this.service.findByPeerId(CLIENT_NAME + CLIENT_VERSION);

    verify(this.dao).findByNameAndVersion(CLIENT_NAME, CLIENT_VERSION);
    verify(this.dao, never()).findByNameAndVersion(CLIENT_NAME, Constant.STAR);
    verify(this.dao, never()).findByNameAndVersion(Constant.STAR, Constant.STAR);
  }
}
