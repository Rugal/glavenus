package ga.rugal.pt.springmvc.mapper;

import java.util.List;

import ga.rugal.pt.springmvc.graphql.type.torrent.Peer;

import com.turn.ttorrent.tracker.TrackedPeer;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * The Data Mapper For Peer.
 *
 * @author Rugal Bernstein
 */
@Mapper(config = CentralConfig.class)
@SuppressFBWarnings("UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD")
public interface PeerMapper {

  PeerMapper INSTANCE = Mappers.getMapper(PeerMapper.class);

  @Mapping(source = "p.hexPeerId", target = "peerId")
  Peer to(TrackedPeer p);

  List<Peer> to(List<TrackedPeer> ps);
}
