package ga.rugal.pt.springmvc.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

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

  @Override
  protected boolean beforeUpdate(final TrackedTorrent torrent,
                                 final Map<String, BeValue> parameters) {
    final int uid; //user id
    final String credential; //password in plaintext
    try {
      uid = parameters.get("uid").getInt();
      credential = parameters.get("credential").getString();
      if (this.userService.authenticate(uid, credential)) {
        LOG.info("Mismatched uid [{}] and credential [{}]", uid, credential);
        return false;
      }
    } catch (final InvalidBEncodingException ex) {
      LOG.error("Invalid encoding for uid [{}] and credential [{}]", ex);
      return false;
    }
    LOG.info("Matched uid [{}] and credential [{}]", uid, credential);
    return true;
  }
}
