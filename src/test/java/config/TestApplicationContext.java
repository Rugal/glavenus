package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import ga.rugal.pt.core.entity.Post;
import ga.rugal.pt.openapi.model.NewPostDto;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.turn.ttorrent.common.Torrent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockMultipartFile;

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
  public Torrent torrent(final File torrentFile) throws IOException, NoSuchAlgorithmException {
    return Torrent.load(torrentFile);
  }

  @Bean
  public NewPostDto newPostDto(final Faker faker) {
    final NewPostDto post = new NewPostDto();
    post.setContent(faker.hitchhikersGuideToTheGalaxy().location());
    post.setTitle(faker.name().name());
    return post;
  }

  @Bean
  public Post post(final Faker faker, final Torrent torrent) {
    final Post post = new Post();
    post.setContent(faker.hitchhikersGuideToTheGalaxy().location());
    post.setPid(1);
    post.setSize(62642L);
    post.setTitle(faker.name().name());
    post.setHash(torrent.getHexInfoHash());
    post.setTorrent(torrent.getEncoded());
    post.setEnable(true);
    return post;
  }

  @Bean
  public MockMultipartFile mmf(final File torrentFile) throws IOException {
    return new MockMultipartFile("file",
                                 torrentFile.getName(),
                                 SystemDefaultProperty.BITTORRENT_MIME,
                                 new FileInputStream(torrentFile));
  }
}
