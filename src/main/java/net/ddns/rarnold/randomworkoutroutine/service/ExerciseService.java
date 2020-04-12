package net.ddns.rarnold.randomworkoutroutine.service;

import lombok.RequiredArgsConstructor;
import net.ddns.rarnold.randomworkoutroutine.model.Exercise;
import net.ddns.rarnold.randomworkoutroutine.model.ExerciseOption;
import net.ddns.rarnold.randomworkoutroutine.repository.ExerciseOptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private static final Random random = new Random();

    private final ExerciseOptionRepository exerciseOptionRepository;

    public Exercise getRandomExercise() {
        List<ExerciseOption> options = exerciseOptionRepository.findAll();
        ExerciseOption option = options.get(random.nextInt(options.size()));
        return new Exercise(option.getName(),
                random.nextInt(option.getRepCountUpperBound() + 1 - option.getRepCountLowerBound()) +
                        option.getRepCountLowerBound());
    }
}
