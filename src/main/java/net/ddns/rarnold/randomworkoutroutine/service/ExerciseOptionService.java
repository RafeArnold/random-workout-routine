package net.ddns.rarnold.randomworkoutroutine.service;

import lombok.RequiredArgsConstructor;
import net.ddns.rarnold.randomworkoutroutine.model.ExerciseOption;
import net.ddns.rarnold.randomworkoutroutine.repository.ExerciseOptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseOptionService {

    private final ExerciseOptionRepository repository;

    public ExerciseOption getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No exercise with the ID '" + id + "' exists"));
    }

    public void save(ExerciseOption option) {
        repository.save(option);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public List<ExerciseOption> getNames() {
        List<Object[]> options = repository.findAllIdsAndNames();
        return options.stream()
                .map(objects -> {
                    ExerciseOption option = new ExerciseOption();
                    option.setId((UUID) objects[0]);
                    option.setName((String) objects[1]);
                    return option;
                })
                .collect(Collectors.toList());
    }
}
