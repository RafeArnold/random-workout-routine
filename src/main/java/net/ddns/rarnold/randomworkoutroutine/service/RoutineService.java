package net.ddns.rarnold.randomworkoutroutine.service;

import lombok.RequiredArgsConstructor;
import net.ddns.rarnold.randomworkoutroutine.model.Routine;
import net.ddns.rarnold.randomworkoutroutine.repository.RoutineRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository repository;

    public Routine getById(UUID id) {
        Optional<Routine> routine = repository.findById(id);
        if (!routine.isPresent()) {
            throw new IllegalArgumentException("No routine with the ID '" + id + "' exists");
        }
        return routine.get();
    }

    public void save(Routine routine) {
        repository.save(routine);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Set<String> getNames() {
        return repository.findAllNames();
    }
}
