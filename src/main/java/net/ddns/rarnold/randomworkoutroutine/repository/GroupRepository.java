package net.ddns.rarnold.randomworkoutroutine.repository;

import net.ddns.rarnold.randomworkoutroutine.model.Group;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GroupRepository extends CrudRepository<Group, UUID> {
    @Query("SELECT e.id, e.name FROM #{#entityName} e")
    List<Object[]> findAllIdsAndNames();
}
