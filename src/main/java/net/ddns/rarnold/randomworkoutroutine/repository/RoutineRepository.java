package net.ddns.rarnold.randomworkoutroutine.repository;

import net.ddns.rarnold.randomworkoutroutine.model.entity.Group;
import net.ddns.rarnold.randomworkoutroutine.model.entity.Routine;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoutineRepository extends ItemRepository<Routine> {

    List<Routine> findAllByGroupsContaining(Group group);
}
