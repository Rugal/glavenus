package ga.rugal.pt.core.service.impl;

import ga.rugal.pt.core.dao.ClientDao;
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
}
