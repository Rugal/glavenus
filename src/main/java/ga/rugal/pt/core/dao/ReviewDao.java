package ga.rugal.pt.core.dao;

import java.util.List;

import ga.rugal.pt.core.entity.Post;
import ga.rugal.pt.core.entity.Review;
import ga.rugal.pt.core.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ReviewDao extends CrudRepository<Review, Integer> {

  List<Review> findByPost(Post post);

  Page<Review> findByPost(Post post, Pageable pageable);

  Page<Review> findByAuthor(User user, Pageable pageable);

  @Query("SELECT AVG(u.rate) FROM Review u WHERE u.post = ?1")
  Double getRateByPost(Post post);
}
