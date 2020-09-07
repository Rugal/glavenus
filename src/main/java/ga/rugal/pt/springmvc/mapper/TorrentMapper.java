package ga.rugal.pt.springmvc.mapper;

import ga.rugal.glavenus.graphql.TorrentDto;

import com.google.common.collect.Lists;
import com.turn.ttorrent.tracker.TrackedTorrent;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The Data Mapper For Torrent.
 *
 * @author Rugal Bernstein
 */
@Mapper(config = CentralConfig.class, uses = PeerMapper.class)
@SuppressFBWarnings("UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD")
public interface TorrentMapper {

  TorrentMapper I = Mappers.getMapper(TorrentMapper.class);

  /**
   * Map TrackedTorrent to DTO Torrent for GraphQL.
   *
   * @param t tracked torrent object
   *
   * @return DTO Torrent object
   */
  default TorrentDto from(final TrackedTorrent t) {
    if (null == t) {
      return null;
    }
    return new TorrentDto(t.getHexInfoHash(),
                          PeerMapper.I.to(Lists.newArrayList(t.getPeers().values())));
  }
}
