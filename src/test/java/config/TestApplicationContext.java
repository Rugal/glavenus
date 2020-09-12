package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ga.rugal.pt.core.entity.Client;
import ga.rugal.pt.core.entity.Post;
import ga.rugal.pt.core.entity.User;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.turn.ttorrent.common.Torrent;
import com.turn.ttorrent.tracker.TrackedTorrent;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
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
  public String host(final @Value("${server.host:localhost}") String host) {
    return host;
  }

  @Bean
  @ConditionalOnMissingBean
  public Integer port(final @Value("${server.port:6969}") int port) {
    return port;
  }

  @Bean
  @ConditionalOnMissingBean
  public ObjectMapper objectMapper() {
    final ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return mapper;
  }

  @Bean
  @ConditionalOnMissingBean
  public ConcurrentMap<String, TrackedTorrent> torrents() {
    return new ConcurrentHashMap<>();
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
                                 Constant.BITTORRENT_MIME,
                                 new FileInputStream(torrentFile));
  }

  @Bean
  public User user(final Faker faker) {
    final User user = new User();
    user.setUid(1);
    user.setCredit(0);
    user.setStatus(0);
    user.setPassword(BCrypt.hashpw("1", BCrypt.gensalt()));
    user.setEmail(faker.internet().emailAddress());
    return user;
  }

  @Bean
  public Client client(final Faker faker) {
    final Client client = new Client();
    client.setCid(1);
    client.setName(faker.name().firstName());
    client.setEnable(true);
    client.setVersion("1.0.0");
    return client;
  }
}
