package ga.rugal.pt.springmvc.mapper;

import java.util.List;

import ga.rugal.glavenus.graphql.PeerDto;

import com.turn.ttorrent.tracker.TrackedPeer;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The Data Mapper For Peer.
 *
 * @author Rugal Bernstein
 */
@Mapper(config = CentralConfig.class)
@SuppressFBWarnings("UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD")
public interface PeerMapper {

  PeerMapper I = Mappers.getMapper(PeerMapper.class);

  /**
   * Map TrackedPeer to PeerDto.
   *
   * @param p tracked peer
   *
   * @return peer DTO
   */
  default PeerDto to(TrackedPeer p) {
    if (null == p) {
      return null;
    }
    final PeerDto d = new PeerDto();
    d.setDownloaded(p.getDownloaded());
    d.setUploaded(p.getUploaded());
    d.setLastAnnounce(p.getLastAnnounce().getTime() / 1000);
    d.setLeft(p.getLeft());
    d.setPeerId(p.getHexPeerId());
    d.setUid(p.getUid());
    d.setState(PeerStateMapper.I.from(p.getState()));
    return d;
  }

  List<PeerDto> to(List<TrackedPeer> ps);
}
