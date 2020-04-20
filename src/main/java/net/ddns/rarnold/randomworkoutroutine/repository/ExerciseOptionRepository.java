package net.ddns.rarnold.randomworkoutroutine.repository;

import net.ddns.rarnold.randomworkoutroutine.model.ExerciseOption;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ExerciseOptionRepository extends CrudRepository<ExerciseOption, String> {
    @Query("SELECT e.name FROM ExerciseOption e")
    Set<String> findAllNames();
}
