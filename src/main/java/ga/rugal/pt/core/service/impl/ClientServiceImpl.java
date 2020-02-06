package ga.rugal.pt.core.service.impl;

import java.util.Optional;

import config.Constant;

import ga.rugal.pt.core.dao.ClientDao;
import ga.rugal.pt.core.entity.Client;
import ga.rugal.pt.core.service.ClientService;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

  @Autowired
  @Getter
  @Setter
  private ClientDao dao;

  /**
   * Find client by name and version from PeerId.<BR>
   * 1. Get by the specified client name and version<BR>
   * 2. Get by the specified client name and generic version<BR>
   * 3. Get by default client
   *
   * @param name    client short name e.g. UT
   * @param version client version e.g. 1.0
   *
   * @return client object that represents a torrent software
   */
  @Override
  public Client findByPeerId(final String name, final String version) {
    //Get the specific client name and version
    Optional<Client> optional = this.dao.findByNameAndVersion(name, version);
    if (optional.isPresent()) {
      return optional.get();
    }
    //Get generic client name
    optional = this.dao.findByNameAndVersion(name, Constant.STAR);
    if (optional.isPresent()) {
      return optional.get();
    }
    //Get default client
    return this.dao.findByNameAndVersion(Constant.STAR, Constant.STAR).get();
  }
}
