package ga.rugal.pt.core.dao;

import java.util.Optional;

import ga.rugal.pt.core.entity.User;

import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<User, Integer> {

  boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);
}
