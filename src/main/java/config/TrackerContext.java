package config;

import java.io.IOException;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ga.rugal.pt.core.entity.Post;
import ga.rugal.pt.core.service.PostService;

import com.turn.ttorrent.tracker.TrackedTorrent;
import com.turn.ttorrent.tracker.Tracker;
import com.turn.ttorrent.tracker.TrackerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Torrent tracker application context.
 *
 * @author rugal
 */
@Configuration
@Slf4j
public class TrackerContext {

  @Bean
  public ConcurrentMap<String, TrackedTorrent> torrents() {
    return new ConcurrentHashMap<>();
  }

  @Bean
  public TrackerService service(final ConcurrentMap<String, TrackedTorrent> torrents) {
    return new TrackerService(torrents);
  }

  /**
   * Create a tracker server in local, with same port to servlet container Spring will start this
   * tracker after creation of bean.<BR>
   * Load all valid torrent from database and announce them in tracker, then start tracker thread.
   *
   * @param postService PostService that get all posts
   * @param service     TrackerService that actually handle the request
   *
   * @return
   *
   * @throws IOException              Network issue
   * @throws NoSuchAlgorithmException Unable to decode torrent
   *
   */
  @Bean(initMethod = "start", destroyMethod = "stop")
  public Tracker tracker(final PostService postService, final TrackerService service)
          throws IOException, NoSuchAlgorithmException {

    final Tracker tracker = new Tracker(InetAddress.getLocalHost(), service);
    for (Post p : postService.getDao().findAll()) {
      if (Objects.nonNull(p.getBencode())) {
        tracker.announce(new TrackedTorrent(p.getBencode()));
      }
    }
    return tracker;
  }
}
