package ga.rugal.pt.core.service.impl;

import ga.rugal.pt.core.dao.UserDao;
import ga.rugal.pt.core.service.UserService;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements UserService {

  @Autowired
  @Getter
  @Setter
  private UserDao dao;
}
