package config;

import ga.rugal.pt.core.dao.ClientDao;
import ga.rugal.pt.core.dao.PostDao;
import ga.rugal.pt.core.dao.UserDao;
import ga.rugal.pt.core.service.AnnounceService;
import ga.rugal.pt.core.service.ClientService;
import ga.rugal.pt.core.service.JwtService;
import ga.rugal.pt.core.service.PostService;
import ga.rugal.pt.core.service.PostTagService;
import ga.rugal.pt.core.service.ReviewService;
import ga.rugal.pt.core.service.TagService;
import ga.rugal.pt.core.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

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
  public ClientDao clientDao() {
    return Mockito.mock(ClientDao.class);
  }

  @Bean
  @Scope("prototype")
  public UserDao userDao() {
    return Mockito.mock(UserDao.class);
  }

  @Bean
  @Scope("prototype")
  public PostDao postDao() {
    return Mockito.mock(PostDao.class);
  }

  @Bean
  @Scope("prototype")
  public ClientService clientService() {
    return Mockito.mock(ClientService.class);
  }

  @Bean
  @Scope("prototype")
  public PostService postService() {
    return Mockito.mock(PostService.class);
  }

  @Bean
  @Scope("prototype")
  public UserService userService() {
    return Mockito.mock(UserService.class);
  }

  @Bean
  @Scope("prototype")
  public TagService tagService() {
    return Mockito.mock(TagService.class);
  }

  @Bean
  @Scope("prototype")
  public PostTagService postTagService() {
    return Mockito.mock(PostTagService.class);
  }

  @Bean
  @Scope("prototype")
  public JwtService jwtService() {
    return Mockito.mock(JwtService.class);
  }

  @Bean
  @Scope("prototype")
  public AnnounceService announceService() {
    return Mockito.mock(AnnounceService.class);
  }

  @Bean
  @Scope("prototype")
  public ReviewService reviewService() {
    return Mockito.mock(ReviewService.class);
  }
}
