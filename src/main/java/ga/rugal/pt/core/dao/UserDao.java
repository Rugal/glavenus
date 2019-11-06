package ga.rugal.pt.core.dao;

import ga.rugal.pt.core.entity.User;

import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<User, Integer> {
}
