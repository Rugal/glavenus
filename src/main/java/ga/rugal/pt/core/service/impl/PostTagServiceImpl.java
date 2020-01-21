package ga.rugal.pt.core.service.impl;

import ga.rugal.pt.core.dao.PostTagDao;
import ga.rugal.pt.core.service.PostTagService;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostTagServiceImpl implements PostTagService {

  @Autowired
  @Getter
  @Setter
  private PostTagDao dao;
}
