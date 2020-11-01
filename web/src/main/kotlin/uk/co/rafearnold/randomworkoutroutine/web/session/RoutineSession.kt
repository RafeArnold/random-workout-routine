package uk.co.rafearnold.randomworkoutroutine.web.session

import uk.co.rafearnold.randomworkoutroutine.model.Exercise
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Routine
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Group
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.getExercise
import uk.co.rafearnold.randomworkoutroutine.web.util.nextWeightedInt

/**
 * Holds information about the user's current workout session.
 *
 * @property routine The [Routine] that the user is currently doing.
 */
class RoutineSession(private val routine: Routine) {

    init {
        initialiseWeights(routine)
    }

    /**
     * The index of the [Group] that the user's current exercise was picked from. This is
     * initialised at -1 as it is immediately incremented when [nextExercise] is called.
     */
    private var currentGroupIndex: Int = -1

    /**
     * The current set number that the user is doing. This is initialised at 0 as it is immediately
     * incremented when [nextExercise] is called.
     */
    var setCount: Int = 0
        private set

    /**
     * The current exercise the user is doing.
     */
    var currentExercise: Exercise = nextExercise()
        private set

    /**
     * Initialises the [Group.optionWeights] property of all [Group]s in [routine]. All weights are
     * initially set to the same value to ensure equal probability of each exercise being chosen.
     */
    private fun initialiseWeights(routine: Routine) {
        for (group in routine.groups) {
            group.optionWeights = DoubleArray(group.exercises.size) { 1.0 }
        }
    }

    /**
     * Selects the next exercise for the session. The next exercise is selected from the next
     * [Group] in [routine]'s cycle using the [Group]'s [Group.optionWeights] property. Once the
     * new exercise has been selected, that exercise's weight is then adjusted to make it less
     * likely to be selected from that [Group] again.
     */
    fun nextExercise(): Exercise {
        setCount++
        currentGroupIndex++
        currentGroupIndex %= routine.groups.size
        val currentGroup = routine.groups[currentGroupIndex]
        val optionWeights = currentGroup.optionWeights
        val nextOptionIndex: Int = nextWeightedInt(optionWeights)
        val option = currentGroup.exercises[nextOptionIndex]
        currentExercise = option.getExercise()
        adjustWeight(optionWeights, nextOptionIndex)
        normaliseWeights(optionWeights)
        return currentExercise
    }

    /**
     * Reduces the value of the element at [index] in [weights]. This makes that element less
     * likely to be selected when [weights] is fed into [nextWeightedInt].
     */
    private fun adjustWeight(weights: DoubleArray, index: Int) {
        weights[index] = weights[index] / 2
    }

    /**
     * Adjusts the elements of [weights] to ensure their sum is equal to the size of [weights].
     */
    private fun normaliseWeights(weights: DoubleArray) {
        val weightSum = weights.sum()
        val factor = weights.size / weightSum
        for (i in weights.indices) {
            weights[i] *= factor
        }
    }

    companion object {
        /**
         * The name of the attribute that stores a [RoutineSession] in an
         * [javax.servlet.http.HttpSession].
         */
        const val ROUTINE_SESSION_ATTRIBUTE_NAME = "routineSession"
    }
}
