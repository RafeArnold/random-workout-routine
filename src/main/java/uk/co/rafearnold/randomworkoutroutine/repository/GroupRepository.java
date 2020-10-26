package uk.co.rafearnold.randomworkoutroutine.repository;

import uk.co.rafearnold.randomworkoutroutine.model.entity.ExerciseOption;
import uk.co.rafearnold.randomworkoutroutine.model.entity.Group;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends ItemRepository<Group> {

    List<Group> findAllByExerciseOptionsContaining(ExerciseOption exercise);
}
