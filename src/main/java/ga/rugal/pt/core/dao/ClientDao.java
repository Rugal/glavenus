package ga.rugal.pt.core.dao;

import ga.rugal.pt.core.entity.Client;

import org.springframework.data.repository.CrudRepository;

public interface ClientDao extends CrudRepository<Client, Integer> {

  //findOne, delete, save, count, etc., are inherited from CruiRepository
}
