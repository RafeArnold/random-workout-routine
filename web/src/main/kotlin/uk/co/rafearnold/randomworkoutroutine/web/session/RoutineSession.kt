package uk.co.rafearnold.randomworkoutroutine.web.session

import uk.co.rafearnold.randomworkoutroutine.model.Exercise
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Routine
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.getExercise
import uk.co.rafearnold.randomworkoutroutine.web.util.nextWeightedInt

class RoutineSession(private val routine: Routine) {

    init {
        initialiseWeights(routine)
    }

    private var currentGroupIndex: Int = -1
    var setCount: Int = 0
        private set

    var currentExercise: Exercise = nextExercise()
        private set

    private fun initialiseWeights(routine: Routine) {
        for (group in routine.groups) {
            group.optionWeights = DoubleArray(group.exercises.size) { 1.0 }
        }
    }

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

    private fun adjustWeight(weights: DoubleArray, index: Int) {
        weights[index] = weights[index] / 2
    }

    private fun normaliseWeights(weights: DoubleArray) {
        val weightSum = weights.sum()
        val factor = weights.size / weightSum
        for (i in weights.indices) {
            weights[i] *= factor
        }
    }

    companion object {
        const val ROUTINE_SESSION_ATTRIBUTE_NAME = "routineSession"
    }
}
