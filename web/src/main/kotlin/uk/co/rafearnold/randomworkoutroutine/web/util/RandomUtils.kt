package uk.co.rafearnold.randomworkoutroutine.web.util

import kotlin.random.Random

/**
 * Returns a random integer between 0, inclusive, and the size of [weights], exclusive. The
 * probably of a given integer being selected is determined by the value at that index in
 * [weights]. Higher values are more likely to be selected than lower values.
 */
fun nextWeightedInt(weights: DoubleArray): Int {
    var randInt = Random.nextDouble(weights.sum())
    var index = 0
    while (randInt >= weights[index]) {
        randInt -= weights[index]
        index++
    }
    return index
}
