package uk.co.rafearnold.randomworkoutroutine.web.util

import kotlin.random.Random

fun nextWeightedInt(weights: DoubleArray): Int {
    var randInt = Random.nextDouble(weights.sum())
    var index = 0
    while (randInt >= weights[index]) {
        randInt -= weights[index]
        index++
    }
    return index
}
