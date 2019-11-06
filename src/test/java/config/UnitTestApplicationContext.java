package config;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import ga.rugal.pt.core.dao.ClientDao;
import ga.rugal.pt.core.dao.UserDao;
import ga.rugal.pt.core.dao.PostDao;
import ga.rugal.pt.core.service.ClientService;
import ga.rugal.pt.core.service.PostService;
import ga.rugal.pt.core.service.UserService;

/**
 *
 * @author Rugal Bernstein
 */
@Configuration
public class UnitTestApplicationContext {

  @Bean
  @ConditionalOnMissingBean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  @Scope("prototype")
  public ClientDao courseDao() {
    return Mockito.mock(ClientDao.class);
  }

  @Bean
  @Scope("prototype")
  public UserDao studentDao() {
    return Mockito.mock(UserDao.class);
  }

  @Bean
  @Scope("prototype")
  public PostDao registrationDao() {
    return Mockito.mock(PostDao.class);
  }

  @Bean
  @Scope("prototype")
  public ClientService courseService() {
    return Mockito.mock(ClientService.class);
  }

  @Bean
  @Scope("prototype")
  public PostService registrationService() {
    return Mockito.mock(PostService.class);
  }

  @Bean
  @Scope("prototype")
  public UserService studentService() {
    return Mockito.mock(UserService.class);
  }
}
