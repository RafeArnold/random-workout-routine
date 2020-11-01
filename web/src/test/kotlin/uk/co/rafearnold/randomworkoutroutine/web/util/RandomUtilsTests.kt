package uk.co.rafearnold.randomworkoutroutine.web.util

import org.junit.jupiter.api.Test

internal class RandomUtilsTests {

    @Test
    fun `when one weight is higher than another then it is more likely to be chosen`() {
        val weights: DoubleArray = doubleArrayOf(0.0, 0.9, 1.5, 6.333)
        val iterations = 1000000
        val results = IntArray(weights.size)
        for (i in 1..iterations) {
            results[nextWeightedInt(weights)]++
        }
        val error: Int = (0.01 * iterations).toInt()
        assert(results[0] == 0)
        for (i in weights.indices) {
            val expectedAverage: Int = (iterations * weights[i] / weights.sum()).toInt()
            assert(results[i] in (expectedAverage - error)..(expectedAverage + error))
        }
    }
}
