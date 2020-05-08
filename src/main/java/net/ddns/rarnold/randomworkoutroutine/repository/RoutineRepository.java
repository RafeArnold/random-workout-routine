package net.ddns.rarnold.randomworkoutroutine.repository;

import net.ddns.rarnold.randomworkoutroutine.model.entity.Routine;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoutineRepository extends CrudRepository<Routine, UUID> {
    @Query("SELECT r.id, r.name FROM Routine r")
    List<Object[]> findAllIdsAndNames();
}
