package uk.co.rafearnold.randomworkoutroutine.web.session

import uk.co.rafearnold.randomworkoutroutine.model.ExerciseSet
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.ExerciseOption
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Group
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Routine
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.getSet
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
     * An array of arrays determining the likelihood of each [ExerciseOption] in each [Group] in
     * [routine] being chosen when [nextExercise] is called. Each element in the outer array
     * corresponds to the [Group] at the same index in [routine]. Each value in each inner array
     * corresponds to the [ExerciseOption] at the same index in the relevant [Group]. The higher a
     * value, the more likely its corresponding [ExerciseOption] is to be chosen.
     */
    private val groupOptionWeights: Array<DoubleArray> = initialiseWeights(routine)

    /**
     * The current exercise set the user is doing.
     */
    var currentSet: ExerciseSet = nextExercise()
        private set

    /**
     * Creates an array whose size is equal to the number of [Group]s in [routine]. Each element
     * of the array is itself an array of [Double] weights whose size is equal to the number of
     * [ExerciseOption]s in the corresponding [Group] in [routine]. All weights are initially set
     * to the same value to ensure equal probability.
     */
    private fun initialiseWeights(routine: Routine): Array<DoubleArray> =
        Array(routine.groups.size) { DoubleArray(routine.groups[it].exercises.size) { 1.0 } }

    /**
     * Selects the next exercise set for the session. The next set is selected, at random, from the
     * next [Group] in [routine]'s cycle using [groupOptionWeights]. Once the new exercise has been
     * selected, that exercise's weight is then adjusted to make it less likely to be selected from
     * that [Group] again.
     */
    fun nextExercise(): ExerciseSet {
        setCount++
        currentGroupIndex++
        currentGroupIndex %= routine.groups.size
        val currentGroup = routine.groups[currentGroupIndex]
        val optionWeights = groupOptionWeights[currentGroupIndex]
        val nextOptionIndex: Int = nextWeightedInt(optionWeights)
        val option = currentGroup.exercises[nextOptionIndex]
        currentSet = option.getSet()
        adjustWeight(optionWeights, nextOptionIndex)
        normaliseWeights(optionWeights)
        return currentSet
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
