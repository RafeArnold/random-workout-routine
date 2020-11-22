package uk.co.rafearnold.randomworkoutroutine.model

import java.util.*

interface SimpleItem {
    val id: UUID
    val name: String
}

interface Item : SimpleItem {
    val tags: Set<String>
}

interface Exercise : Item

interface ExerciseOption {
    val id: UUID
    val exercise: Exercise
    val repCountLowerBound: Int
    val repCountUpperBound: Int
}

interface Group {
    val id: UUID
    val exercises: List<ExerciseOption>
}

interface Routine : Item {
    val groups: List<Group>
}

data class SimpleItemImpl(
    override val id: UUID,
    override val name: String
) : SimpleItem

data class ExerciseOptionImpl(
    override val id: UUID,
    override val exercise: Exercise,
    override val repCountLowerBound: Int,
    override val repCountUpperBound: Int
) : ExerciseOption

data class GroupImpl(
    override val id: UUID,
    override val exercises: List<ExerciseOption>
) : Group

data class RoutineImpl(
    override val id: UUID,
    override val name: String,
    override val tags: Set<String>,
    override val groups: List<Group>
) : Routine
