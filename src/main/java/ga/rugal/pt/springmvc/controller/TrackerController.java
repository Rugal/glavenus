
package ga.rugal.pt.springmvc.controller;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

import ga.rugal.pt.core.entity.User;
import ga.rugal.pt.core.service.UserService;

import com.turn.ttorrent.bcodec.BeValue;
import com.turn.ttorrent.bcodec.InvalidBEncodingException;
import com.turn.ttorrent.tracker.TrackedTorrent;
import com.turn.ttorrent.tracker.TrackerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Custom implementation of TrackerService.
 *
 * @author rugal
 */
@Slf4j
public class TrackerController extends TrackerService {

  @Autowired
  private UserService userService;

  public TrackerController(final ConcurrentMap<String, TrackedTorrent> torrents) {
    super(torrents);
  }

  private Optional<User> authenticate(final Map<String, BeValue> parameters) {
    try {
      //user id
      final int uid = parameters.get("uid").getInt();
      //password in plaintext
      final String credential = parameters.get("credential").getString();
      if (this.userService.authenticate(uid, credential)) {
        LOG.info("Matched uid [{}] and credential [{}]", uid, credential);
        return this.userService.getDao().findById(uid);
      }
      LOG.info("Mismatched uid [{}] and credential [{}]", uid, credential);
    } catch (final InvalidBEncodingException ex) {
      LOG.error("Invalid encoding for uid credential", ex);
    }
    return Optional.empty();
  }

  @Override
  protected boolean beforeUpdate(final TrackedTorrent torrent,
                                 final Map<String, BeValue> parameters) {
    return this.authenticate(parameters).isPresent();
  }
}
