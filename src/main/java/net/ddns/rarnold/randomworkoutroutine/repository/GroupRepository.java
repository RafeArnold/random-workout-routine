package net.ddns.rarnold.randomworkoutroutine.repository;

import net.ddns.rarnold.randomworkoutroutine.model.Group;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface GroupRepository extends CrudRepository<Group, UUID> {
    @Query("SELECT g.name FROM grouping g")
    Set<String> findAllNames();
}
