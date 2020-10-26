package uk.co.rafearnold.randomworkoutroutine.web.service;

import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Group;
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Routine;
import uk.co.rafearnold.randomworkoutroutine.web.repository.RoutineRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoutineService extends ItemService<Routine> {

    public RoutineService(RoutineRepository repository) {
        super(repository);
    }

    public void removeGroupFromAll(Group group) {
        List<Routine> routines = ((RoutineRepository) repository).findAllByGroupsContaining(group);
        for (Routine routine : routines) {
            routine.setGroups(routine.getGroups().stream()
                    .filter(g -> g.getId() != group.getId())
                    .collect(Collectors.toList()));
            repository.save(routine);
        }
    }

    @Override
    protected Routine createItem(UUID id, String name) {
        Routine routine = new Routine();
        routine.setId(id);
        routine.setName(name);
        return routine;
    }
}
