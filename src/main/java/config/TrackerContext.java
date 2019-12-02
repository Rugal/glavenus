package config;

import java.io.IOException;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import ga.rugal.pt.core.entity.Post;
import ga.rugal.pt.core.service.PostService;

import com.turn.ttorrent.tracker.TrackedTorrent;
import com.turn.ttorrent.tracker.Tracker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

/**
 * Torrent tracker application context.
 *
 * @author rugal
 */
@Slf4j
public class TrackerContext {

  /**
   * Create a tracker server in local, with same port to servlet container Spring will start this
   * tracker after creation of bean.<BR>
   * Load all valid torrent from database and announce them in tracker, then start tracker thread.
   *
   * @param postService
   *
   * @return
   *
   * @throws IOException              Network issue
   * @throws NoSuchAlgorithmException Unable to decode torrent
   *
   */
  @Bean(initMethod = "start", destroyMethod = "stop")
  @Scope("singleton")
  public Tracker tracker(final PostService postService) throws IOException,
                                                               NoSuchAlgorithmException {
    final Tracker tracker = new Tracker(InetAddress.getLocalHost());
    for (Post p : postService.getDao().findAll()) {
      if (Objects.nonNull(p.getBencode())) {
        tracker.announce(new TrackedTorrent(p.getBencode()));
      }
    }
    return tracker;
  }
}
