package net.ddns.rarnold.randomworkoutroutine.repository;

import net.ddns.rarnold.randomworkoutroutine.model.ExerciseOption;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface ExerciseOptionRepository extends CrudRepository<ExerciseOption, UUID> {
    @Query("SELECT e.name FROM ExerciseOption e")
    Set<String> findAllNames();
}
