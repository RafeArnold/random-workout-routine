package uk.co.rafearnold.randomworkoutroutine.session;

import lombok.Getter;
import uk.co.rafearnold.randomworkoutroutine.model.Exercise;
import uk.co.rafearnold.randomworkoutroutine.model.entity.ExerciseOption;
import uk.co.rafearnold.randomworkoutroutine.model.entity.Group;
import uk.co.rafearnold.randomworkoutroutine.model.entity.Routine;
import uk.co.rafearnold.randomworkoutroutine.util.RandomUtils;

import java.util.Arrays;

public class RoutineSession {

    public static final String ROUTINE_SESSION_ATTRIBUTE_NAME = "routineSession";

    private final Routine routine;
    @Getter
    private Exercise currentExercise;
    private int currentGroupIndex = -1;
    @Getter
    private int setCount;

    public RoutineSession(Routine routine) {
        initialiseWeights(routine);
        this.routine = routine;
    }

    private void initialiseWeights(Routine routine) {
        for (Group group : routine.getGroups()) {
            double[] weights = new double[group.getExerciseOptions().size()];
            Arrays.fill(weights, 1);
            group.setOptionWeights(weights);
        }
    }

    public Exercise nextExercise() {
        setCount++;
        currentGroupIndex++;
        currentGroupIndex %= routine.getGroups().size();
        Group currentGroup = routine.getGroups().get(currentGroupIndex);
        double[] optionWeights = currentGroup.getOptionWeights();
        int nextOptionIndex = RandomUtils.nextWeightedInt(optionWeights);
        ExerciseOption option = currentGroup.getExerciseOptions().get(nextOptionIndex);
        currentExercise = option.getExercise();
        adjustWeight(optionWeights, nextOptionIndex);
        normaliseWeights(optionWeights);
        return currentExercise;
    }

    private void adjustWeight(double[] weights, int index) {
        weights[index] /= 2;
    }

    private void normaliseWeights(double[] weights) {
        double weightSum = Arrays.stream(weights).sum();
        double factor = weights.length / weightSum;
        for (int i = 0; i < weights.length; i++) {
            weights[i] *= factor;
        }
    }
}
