package config;

import java.io.File;

import ga.rugal.pt.core.entity.Post;
import ga.rugal.pt.openapi.model.NewPostDto;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Rugal Bernstein
 */
@Configuration
public class TestApplicationContext {

  private static final String TORRENT_PATH = "configuration/torrent";

  @Bean
  @ConditionalOnMissingBean
  public ObjectMapper objectMapper() {
    final ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return mapper;
  }

  @Bean
  public Faker faker() {
    return new Faker();
  }

  /**
   * The torrent file of which name starts with Junit. This torrent is not persisted in DB yet, just
   * for testing.
   *
   * @return
   */
  @Bean
  public File torrentFile() {
    return new File(TORRENT_PATH)
            .listFiles((File dir, String fileName) -> fileName.startsWith("Junit"))[0];
  }

  @Bean
  public NewPostDto newPostDto(final Faker faker) {
    final NewPostDto post = new NewPostDto();
    post.setContent(faker.hitchhikersGuideToTheGalaxy().location());
    post.setTitle(faker.name().name());
    return post;
  }

  @Bean
  public Post post(final Faker faker) {
    final Post post = new Post();
    post.setContent(faker.hitchhikersGuideToTheGalaxy().location());
    post.setPid(1);
    post.setSize(62642L);
    post.setTitle(faker.name().name());
    post.setHash("A12F4E3EFEDC35937670811147A076BC596176CA");
    post.setEnable(true);
    return post;
  }
}
