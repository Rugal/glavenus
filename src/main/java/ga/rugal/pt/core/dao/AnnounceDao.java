package ga.rugal.pt.core.dao;

import ga.rugal.pt.core.entity.Announce;

import org.springframework.data.repository.CrudRepository;

public interface AnnounceDao extends CrudRepository<Announce, Integer> {

  //findOne, delete, save, count, etc., are inherited from CruiRepository
}
