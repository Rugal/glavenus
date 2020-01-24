package ga.rugal.pt.core.service.impl;

import ga.rugal.pt.core.dao.ReviewDao;
import ga.rugal.pt.core.service.ReviewService;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {

  @Autowired
  @Getter
  @Setter
  private ReviewDao dao;
}
