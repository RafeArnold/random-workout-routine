package uk.co.rafearnold.randomworkoutroutine.model

interface ExerciseSet {
    val exerciseName: String
    val repCount: Int
}

data class ExerciseSetImpl(override val exerciseName: String, override val repCount: Int) : ExerciseSet
