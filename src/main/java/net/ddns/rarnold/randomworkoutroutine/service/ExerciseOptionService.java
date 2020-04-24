package net.ddns.rarnold.randomworkoutroutine.service;

import lombok.RequiredArgsConstructor;
import net.ddns.rarnold.randomworkoutroutine.model.ExerciseOption;
import net.ddns.rarnold.randomworkoutroutine.repository.ExerciseOptionRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExerciseOptionService {

    private final ExerciseOptionRepository repository;

    public void save(ExerciseOption option) {
        repository.save(option);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Set<String> getNames() {
        return repository.findAllNames();
    }
}
