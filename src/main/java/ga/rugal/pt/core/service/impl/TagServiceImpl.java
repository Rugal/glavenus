package ga.rugal.pt.core.service.impl;

import ga.rugal.pt.core.dao.TagDao;
import ga.rugal.pt.core.service.TagService;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService {

  @Autowired
  @Getter
  @Setter
  private TagDao dao;
}
