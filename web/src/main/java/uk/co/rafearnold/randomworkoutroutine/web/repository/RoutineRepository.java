package uk.co.rafearnold.randomworkoutroutine.web.repository;

import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Group;
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Routine;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoutineRepository extends ItemRepository<Routine> {

    List<Routine> findAllByGroupsContaining(Group group);
}
