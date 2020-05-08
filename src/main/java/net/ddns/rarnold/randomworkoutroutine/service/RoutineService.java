package net.ddns.rarnold.randomworkoutroutine.service;

import lombok.RequiredArgsConstructor;
import net.ddns.rarnold.randomworkoutroutine.model.entity.Routine;
import net.ddns.rarnold.randomworkoutroutine.repository.RoutineRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository repository;

    public Routine getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No routine with the ID '" + id + "' exists"));
    }

    public void save(Routine routine) {
        repository.save(routine);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public List<Routine> getNames() {
        List<Object[]> routines = repository.findAllIdsAndNames();
        return routines.stream()
                .map(objects -> {
                    Routine routine = new Routine();
                    routine.setId((UUID) objects[0]);
                    routine.setName((String) objects[1]);
                    return routine;
                })
                .collect(Collectors.toList());
    }
}
