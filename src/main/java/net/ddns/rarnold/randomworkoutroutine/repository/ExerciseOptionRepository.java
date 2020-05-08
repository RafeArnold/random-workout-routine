package net.ddns.rarnold.randomworkoutroutine.repository;

import net.ddns.rarnold.randomworkoutroutine.model.entity.ExerciseOption;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExerciseOptionRepository extends CrudRepository<ExerciseOption, UUID> {
    @Query("SELECT e.id, e.name FROM ExerciseOption e")
    List<Object[]> findAllIdsAndNames();

    @Query("SELECT e.id, e.name FROM ExerciseOption e WHERE lower(e.name) LIKE :#{'%' + #filter.toLowerCase() + '%'}")
    List<Object[]> findAllWithNameContainingIgnoreCase(@Param("filter") String filter);
}
