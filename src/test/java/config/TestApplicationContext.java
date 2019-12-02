package config;

import java.io.File;

import ga.rugal.pt.core.entity.Post;

import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Rugal Bernstein
 */
@Configuration
public class TestApplicationContext {

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
    return new File(SystemDefaultProperty.TORRENT_PATH)
            .listFiles((File dir, String fileName) -> fileName.startsWith("Junit"))[0];
  }

  @Bean
  public Post post() {
    final Post post = new Post();
    return post;
  }
}
