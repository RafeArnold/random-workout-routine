package uk.co.rafearnold.randomworkoutroutine.util;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class RandomUtils {
    private final Random random = new Random();

    public int nextInt(int bound) {
        return random.nextInt(bound);
    }

    public double nextDouble(double bound) {
        return random.nextDouble() * bound;
    }

    public int nextInt(int lowerBound, int upperBound) {
        return nextInt(upperBound + 1 - lowerBound) + lowerBound;
    }

    public int nextWeightedInt(double[] weights) {
        double randInt = nextDouble(weights.length);
        int index;
        for (index = 0; randInt >= weights[index]; index++) {
            randInt -= weights[index];
        }
        return index;
    }
}
