import java.util.Random;
import java.util.Scanner;

public class Routine {

    private static final Group[] routine =
            {
                    // Group A
                    new Group(new ExerciseOption[]{
                            new ExerciseOption("sit ups", 8, 25),
                            new ExerciseOption("press ups", 5, 20),
                            new ExerciseOption("tricep dips", 10, 25)
                    }),
                    // Group B
                    new Group(new ExerciseOption[]{
                            new ExerciseOption("rope climbs", 1, 5),
                            new ExerciseOption("monkey bars", 1, 4),
                            new ExerciseOption("bicep curls", 8, 26)
                    })
            };

    private static Random random = new Random();
    private static Scanner keyboard = new Scanner(System.in);

    private static boolean inProgress = true;
    private static int currentGroupIndex = 0;

    public static void main(String[] args) {
        do {
            Exercise nextExercise = nextExercise(currentGroupIndex);
            displayExercise(nextExercise);
            waitForInput();
            incrementCurrentGroupIndex();
        } while (inProgress);
    }

    private static Exercise nextExercise(int currentGroupIndex) {
        Group currentGroup = routine[currentGroupIndex];
        ExerciseOption exerciseOption = getRandomExerciseOption(currentGroup);
        return getExerciseFromOption(exerciseOption);
    }

    private static ExerciseOption getRandomExerciseOption(Group group) {
        ExerciseOption[] exerciseOptions = group.exerciseOptions;
        int exerciseIndex = random.nextInt(exerciseOptions.length);
        return exerciseOptions[exerciseIndex];
    }

    private static Exercise getExerciseFromOption(ExerciseOption option) {
        int upperBound = option.repCountUpperBound;
        int lowerBound = option.repCountLowerBound;
        int repCount = random.nextInt(upperBound + 1 - lowerBound) + lowerBound;
        return new Exercise(option.name, repCount);
    }

    private static void displayExercise(Exercise exercise) {
        System.out.println("Exercise : " + exercise.name + ", reps : " + exercise.repCount);
    }

    private static void waitForInput() {
        System.out.println("Continue? (y/n)");
        String next = keyboard.next();
        if (next.equals("n")) {
            inProgress = false;
        } else if (!next.equals("y")) {
            throw new IllegalArgumentException("Input '" + next + "' not recognised");
        }
    }

    private static void incrementCurrentGroupIndex() {
        currentGroupIndex++;
        if (currentGroupIndex >= routine.length) {
            currentGroupIndex = 0;
        }
    }

    private static class Group {
        public ExerciseOption[] exerciseOptions;

        public Group(ExerciseOption[] exerciseOptions) {
            this.exerciseOptions = exerciseOptions;
        }
    }

    private static class ExerciseOption {
        public String name;
        public int repCountLowerBound;
        public int repCountUpperBound;

        public ExerciseOption(String name, int repCountLowerBound, int repCountUpperBound) {
            this.name = name;
            this.repCountLowerBound = repCountLowerBound;
            this.repCountUpperBound = repCountUpperBound;
        }
    }

    private static class Exercise {
        public String name;
        public int repCount;

        public Exercise(String name, int repCount) {
            this.name = name;
            this.repCount = repCount;
        }
    }
}
