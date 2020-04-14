package net.ddns.rarnold.randomworkoutroutine.service;

import lombok.RequiredArgsConstructor;
import net.ddns.rarnold.randomworkoutroutine.model.ExerciseOption;
import net.ddns.rarnold.randomworkoutroutine.repository.ExerciseOptionRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExerciseOptionService {

    private final ExerciseOptionRepository repository;

    public void save(ExerciseOption option) {
        repository.save(option);
    }
}
