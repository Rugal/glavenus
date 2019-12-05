package ga.rugal.pt.core.service.impl;

import ga.rugal.pt.core.dao.PostDao;
import ga.rugal.pt.core.service.PostService;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {

  @Autowired
  @Getter
  @Setter
  private PostDao dao;
}
