package net.ddns.rarnold.randomworkoutroutine.repository;

import net.ddns.rarnold.randomworkoutroutine.model.Group;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GroupRepository extends CrudRepository<Group, UUID> {
    @Query("SELECT g.id, g.name FROM grouping g")
    List<Object[]> findAllIdsAndNames();

    @Query("SELECT g.id, g.name FROM grouping g WHERE lower(g.name) LIKE :#{'%' + #filter.toLowerCase() + '%'}")
    List<Object[]> findAllWithNameContainingIgnoreCase(@Param("filter") String filter);
}
