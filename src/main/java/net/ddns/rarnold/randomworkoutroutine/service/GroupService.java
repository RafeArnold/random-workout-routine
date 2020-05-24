package net.ddns.rarnold.randomworkoutroutine.service;

import lombok.RequiredArgsConstructor;
import net.ddns.rarnold.randomworkoutroutine.model.Filter;
import net.ddns.rarnold.randomworkoutroutine.model.entity.ExerciseOption;
import net.ddns.rarnold.randomworkoutroutine.model.entity.Group;
import net.ddns.rarnold.randomworkoutroutine.repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository repository;
    private final RoutineService routineService;

    public Group getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No group with the ID '" + id + "' exists"));
    }

    public void save(Group group) {
        repository.save(group);
    }

    public void delete(UUID id) {
        routineService.removeGroupFromAll(getById(id));
        repository.deleteById(id);
    }

    public List<Group> getNames() {
        return transformIdAndNameToGroup(repository.findAllIdsAndNames());
    }

    public void removeExerciseFromAll(ExerciseOption exercise) {
        List<Group> groups = repository.findAllByExerciseOptionsContaining(exercise);
        for (Group group : groups) {
            group.setExerciseOptions(group.getExerciseOptions().stream()
                    .filter(e -> e.getId() != exercise.getId())
                    .collect(Collectors.toList()));
            repository.save(group);
        }
    }

    public List<Group> searchNames(Filter filter) {
        String searchTerm = "";
        Set<String> excludedTerms = Collections.singleton("");
        if (filter != null) {
            if (filter.getSearchTerm() != null) {
                searchTerm = filter.getSearchTerm();
            }
            if (filter.getExcludedTerms() != null && !filter.getExcludedTerms().isEmpty()) {
                excludedTerms = filter.getExcludedTerms();
            }
        }
        return transformIdAndNameToGroup(repository.findAllIdsAndNamesWithNameContainingIgnoreCase(searchTerm, excludedTerms));
    }

    private List<Group> transformIdAndNameToGroup(List<Object[]> idsAndNames) {
        return idsAndNames.stream()
                .map(objects -> {
                    Group group = new Group();
                    group.setId((UUID) objects[0]);
                    group.setName((String) objects[1]);
                    return group;
                })
                .collect(Collectors.toList());
    }
}
