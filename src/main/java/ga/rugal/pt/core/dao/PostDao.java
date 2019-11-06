package ga.rugal.pt.core.dao;

import ga.rugal.pt.core.entity.Post;

import org.springframework.data.repository.CrudRepository;

public interface PostDao extends CrudRepository<Post, Integer> {
}
