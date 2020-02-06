package ga.rugal.pt.core.dao;

import ga.rugal.pt.core.entity.Announce;
import ga.rugal.pt.core.entity.Post;
import ga.rugal.pt.core.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface AnnounceDao extends CrudRepository<Announce, Integer> {

  Page<Announce> findByUserAndPostOrderByUpdateAtDesc(User user, Post post, Pageable pageable);
}
