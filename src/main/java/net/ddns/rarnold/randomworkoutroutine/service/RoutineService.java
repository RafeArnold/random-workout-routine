package net.ddns.rarnold.randomworkoutroutine.service;

import lombok.RequiredArgsConstructor;
import net.ddns.rarnold.randomworkoutroutine.model.Routine;
import net.ddns.rarnold.randomworkoutroutine.repository.RoutineRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository repository;

    public Routine getByName(String name) {
        Optional<Routine> routine = repository.findById(name);
        if (!routine.isPresent()) {
            throw new IllegalArgumentException("No routine by the name '" + name + "' exists");
        }
        return routine.get();
    }

    public void save(Routine routine) {
        repository.save(routine);
    }

    public void delete(String name) {
        repository.deleteById(name);
    }
}
