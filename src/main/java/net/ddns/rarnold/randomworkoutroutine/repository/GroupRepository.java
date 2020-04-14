package net.ddns.rarnold.randomworkoutroutine.repository;

import net.ddns.rarnold.randomworkoutroutine.model.Group;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends CrudRepository<Group, String> {
}
