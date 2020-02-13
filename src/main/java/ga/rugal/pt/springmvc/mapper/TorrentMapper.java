package ga.rugal.pt.springmvc.mapper;

import ga.rugal.pt.springmvc.graphql.type.torrent.Torrent;

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

  TorrentMapper INSTANCE = Mappers.getMapper(TorrentMapper.class);

  /**
   * Map TrackedTorrent to DTO Torrent for GraphQL.
   *
   * @param t tracked torrent object
   *
   * @return DTO Torrent object
   */
  default Torrent to(final TrackedTorrent t) {
    final Torrent torrent = new Torrent();
    torrent.setHash(t.getHexInfoHash());
    torrent.setPeers(PeerMapper.INSTANCE.to(Lists.newArrayList(t.getPeers().values())));
    return torrent;
  }
}
