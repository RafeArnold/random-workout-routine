package net.ddns.rarnold.randomworkoutroutine.service;

import net.ddns.rarnold.randomworkoutroutine.model.entity.ExerciseOption;
import net.ddns.rarnold.randomworkoutroutine.model.entity.Group;
import net.ddns.rarnold.randomworkoutroutine.repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GroupService extends ItemService<Group> {

    private final RoutineService routineService;

    public GroupService(GroupRepository repository, RoutineService routineService) {
        super(repository);
        this.routineService = routineService;
    }

    public void delete(Group item) {
        routineService.removeGroupFromAll(item);
        super.delete(item);
    }

    public void removeExerciseFromAll(ExerciseOption exercise) {
        List<Group> groups = ((GroupRepository) repository).findAllByExerciseOptionsContaining(exercise);
        for (Group group : groups) {
            group.setExerciseOptions(group.getExerciseOptions().stream()
                    .filter(e -> e.getId() != exercise.getId())
                    .collect(Collectors.toList()));
            repository.save(group);
        }
    }

    @Override
    protected Group createItem(UUID id, String name) {
        Group group = new Group();
        group.setId(id);
        group.setName(name);
        return group;
    }
}
