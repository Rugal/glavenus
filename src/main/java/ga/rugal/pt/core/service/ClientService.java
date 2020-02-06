package ga.rugal.pt.core.service;

import ga.rugal.pt.core.dao.ClientDao;
import ga.rugal.pt.core.entity.Client;

public interface ClientService extends BaseService<ClientDao> {

  /**
   * Find client by name and version from PeerId.
   *
   * @param name    client short name e.g. UT
   * @param version client version e.g. 1.0
   *
   * @return client object that represents a torrent software
   */
  Client findByPeerId(String name, String version);
}
