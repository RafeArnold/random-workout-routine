package uk.co.rafearnold.randomworkoutroutine.model

import java.util.*

interface Item {
    val id: UUID
    val name: String
    val tags: Set<String>
}

interface ExerciseOption : Item {
    val repCountLowerBound: Int
    val repCountUpperBound: Int
}

interface Group : Item {
    val exercises: List<ExerciseOption>
}

interface Routine : Item {
    val groups: List<Group>
}

data class ExerciseOptionImpl(
    override val id: UUID,
    override val name: String,
    override val tags: Set<String>,
    override val repCountLowerBound: Int,
    override val repCountUpperBound: Int
) : ExerciseOption

data class GroupImpl(
    override val id: UUID,
    override val name: String,
    override val tags: Set<String>,
    override val exercises: List<ExerciseOption>
) : Group

data class RoutineImpl(
    override val id: UUID,
    override val name: String,
    override val tags: Set<String>,
    override val groups: List<Group>
) : Routine