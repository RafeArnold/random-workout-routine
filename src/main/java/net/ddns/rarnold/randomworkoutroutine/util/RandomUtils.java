package net.ddns.rarnold.randomworkoutroutine.util;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class RandomUtils {
    private Random random = new Random();

    public int nextInt(int bound) {
        return random.nextInt(bound);
    }

    public int nextInt(int lowerBound, int upperBound) {
        return random.nextInt(upperBound + 1 - lowerBound) + lowerBound;
    }
}
