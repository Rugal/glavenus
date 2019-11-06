package ga.rugal.pt.core.service.impl;

import ga.rugal.pt.core.dao.AnnounceDao;
import ga.rugal.pt.core.service.AnnounceService;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnnounceServiceImpl implements AnnounceService {

  @Autowired
  @Getter
  @Setter
  private AnnounceDao dao;
}
