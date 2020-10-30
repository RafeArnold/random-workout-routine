package uk.co.rafearnold.randomworkoutroutine.model

interface Exercise {
    val name: String
    val repCount: Int
}

data class ExerciseImpl(override val name: String, override val repCount: Int) : Exercise
