package ga.rugal.pt.springmvc.graphql.type.torrent;

import java.util.Date;

import com.turn.ttorrent.tracker.TrackedPeer;
import lombok.Data;

/**
 * GraphQL peer type.
 *
 * @author Rugal Bernstein
 */
@Data
public class Peer {

  private int uid;

  private String peerId;

  private long uploaded;

  private long downloaded;

  private long left;

  private TrackedPeer.PeerState state;

  private Date lastAnnounce;
}
