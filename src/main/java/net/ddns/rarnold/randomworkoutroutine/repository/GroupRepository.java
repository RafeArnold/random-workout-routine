package net.ddns.rarnold.randomworkoutroutine.repository;

import net.ddns.rarnold.randomworkoutroutine.model.entity.ExerciseOption;
import net.ddns.rarnold.randomworkoutroutine.model.entity.Group;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends ItemRepository<Group> {

    List<Group> findAllByExerciseOptionsContaining(ExerciseOption exercise);
}
