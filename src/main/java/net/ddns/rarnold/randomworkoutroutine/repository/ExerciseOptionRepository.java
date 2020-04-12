package net.ddns.rarnold.randomworkoutroutine.repository;

import net.ddns.rarnold.randomworkoutroutine.model.ExerciseOption;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ExerciseOptionRepository extends CrudRepository<ExerciseOption, String> {
    List<ExerciseOption> findAll();
}
