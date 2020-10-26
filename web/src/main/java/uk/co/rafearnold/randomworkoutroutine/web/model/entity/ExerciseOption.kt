package uk.co.rafearnold.randomworkoutroutine.web.model.entity

import java.util.*
import javax.persistence.*
import kotlin.random.Random

@Entity
class ExerciseOption(
        id: UUID = UUID.randomUUID(),
        name: String = "",
        tags: MutableSet<String> = mutableSetOf(),
        @Column(nullable = false) var repCountLowerBound: Int = 0,
        @Column(nullable = false) var repCountUpperBound: Int = 0
) : Item(id, name, tags)

data class Exercise(val name: String, val repCount: Int)

fun ExerciseOption.getExercise() = Exercise(name, Random.nextInt(repCountLowerBound, repCountUpperBound))
