package ga.rugal.pt.springmvc.mapper;

import ga.rugal.glavenus.graphql.PeerStateDto;

import com.turn.ttorrent.tracker.TrackedPeer.PeerState;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The Data Mapper For PeerState.
 *
 * @author Rugal Bernstein
 */
@Mapper(config = CentralConfig.class)
@SuppressFBWarnings("UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD")
public interface PeerStateMapper {

  PeerStateMapper I = Mappers.getMapper(PeerStateMapper.class);

  /**
   * Map TrackedPeer to PeerDto.
   *
   * @param p tracked peer
   *
   * @return peer DTO
   */
  default PeerStateDto from(PeerState p) {
    switch (p) {
      case STARTED:
        return PeerStateDto.STARTED;
      case COMPLETED:
        return PeerStateDto.COMPLETED;
      case STOPPED:
        return PeerStateDto.STOPPED;
      default:
        return PeerStateDto.UNKNOWN;
    }
  }
}
