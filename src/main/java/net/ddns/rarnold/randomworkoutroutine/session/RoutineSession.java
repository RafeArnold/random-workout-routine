package net.ddns.rarnold.randomworkoutroutine.session;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.ddns.rarnold.randomworkoutroutine.model.Exercise;
import net.ddns.rarnold.randomworkoutroutine.model.ExerciseOption;
import net.ddns.rarnold.randomworkoutroutine.model.Routine;
import net.ddns.rarnold.randomworkoutroutine.util.RandomUtils;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class RoutineSession {

    public static final String ROUTINE_SESSION_ATTRIBUTE_NAME = "routineSession";

    private final Routine routine;
    private Exercise currentExercise;
    private int currentGroupIndex = -1;
    private int setCount;

    public Exercise nextExercise() {
        setCount++;
        currentGroupIndex++;
        currentGroupIndex %= routine.getGroups().size();
        List<ExerciseOption> options = routine.getGroups().get(currentGroupIndex).getExerciseOptions();
        ExerciseOption option = options.get(RandomUtils.nextInt(options.size()));
        Exercise exercise = option.getExercise();
        currentExercise = exercise;
        return exercise;
    }
}
