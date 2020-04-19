package net.ddns.rarnold.randomworkoutroutine.repository;

import net.ddns.rarnold.randomworkoutroutine.model.Routine;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoutineRepository extends CrudRepository<Routine, String> {
    @Query("SELECT r.name FROM Routine r")
    Set<String> findAllNames();
}
