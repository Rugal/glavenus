package ga.rugal.pt.core.dao;

import ga.rugal.pt.core.entity.Post;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface PostDao extends PagingAndSortingRepository<Post, Integer> {
}
