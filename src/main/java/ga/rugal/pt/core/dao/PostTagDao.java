package ga.rugal.pt.core.dao;

import java.util.List;
import java.util.Optional;

import ga.rugal.pt.core.entity.Post;
import ga.rugal.pt.core.entity.PostTag;
import ga.rugal.pt.core.entity.Tag;

import org.springframework.data.repository.CrudRepository;

public interface PostTagDao extends CrudRepository<PostTag, Integer> {

  List<PostTag> findByPost(Post post);

  List<PostTag> findByTag(Tag tag);

  Optional<PostTag> findByPostAndTag(Post post, Tag tag);
}
