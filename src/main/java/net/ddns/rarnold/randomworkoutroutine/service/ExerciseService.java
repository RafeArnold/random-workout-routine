package net.ddns.rarnold.randomworkoutroutine.service;

import net.ddns.rarnold.randomworkoutroutine.model.Exercise;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ExerciseService {

    private static final String[] exerciseNames =
            {"sit ups", "push ups", "tricep dips", "rope climbs", "monkey bars", "bicep curls"};
    private static final int repCountLowerBound = 5;
    private static final int repCountUpperBound = 20;

    private static final Random random = new Random();

    public Exercise getRandomExercise() {
        String name = exerciseNames[random.nextInt(exerciseNames.length)];
        int repCount = random.nextInt(repCountUpperBound + 1 - repCountLowerBound) + repCountLowerBound;
        return new Exercise(name, repCount);
    }
}
