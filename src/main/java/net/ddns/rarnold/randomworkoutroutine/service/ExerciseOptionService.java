package net.ddns.rarnold.randomworkoutroutine.service;

import lombok.RequiredArgsConstructor;
import net.ddns.rarnold.randomworkoutroutine.model.Filter;
import net.ddns.rarnold.randomworkoutroutine.model.entity.ExerciseOption;
import net.ddns.rarnold.randomworkoutroutine.repository.ExerciseOptionRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseOptionService {

    private final ExerciseOptionRepository repository;
    private final GroupService groupService;

    public ExerciseOption getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No exercise with the ID '" + id + "' exists"));
    }

    public void save(ExerciseOption option) {
        repository.save(option);
    }

    public void delete(UUID id) {
        groupService.removeExerciseFromAll(getById(id));
        repository.deleteById(id);
    }

    public List<ExerciseOption> getNames() {
        return transformIdAndNameToOption(repository.findAllIdsAndNames());
    }

    public List<ExerciseOption> searchNames(Filter filter) {
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
        return transformIdAndNameToOption(repository.findAllIdsAndNamesWithNameContainingIgnoreCase(searchTerm, excludedTerms));
    }

    private List<ExerciseOption> transformIdAndNameToOption(List<Object[]> idsAndNames) {
        return idsAndNames.stream()
                .map(objects -> {
                    ExerciseOption option = new ExerciseOption();
                    option.setId((UUID) objects[0]);
                    option.setName((String) objects[1]);
                    return option;
                })
                .collect(Collectors.toList());
    }
}
