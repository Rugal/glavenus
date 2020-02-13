package ga.rugal.pt.springmvc.graphql.type.torrent;

import java.util.List;

import lombok.Data;

/**
 * GraphQL torrent type.
 *
 * @author Rugal Bernstein
 */
@Data
public class Torrent {

  private String hash;

  private List<Peer> peers;
}
