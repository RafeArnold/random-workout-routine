package uk.co.rafearnold.randomworkoutroutine.web.model.entity

import uk.co.rafearnold.randomworkoutroutine.model.Exercise
import java.util.*
import javax.persistence.*
import kotlin.random.Random

@Entity
class ExerciseOption(
        id: UUID = UUID.randomUUID(),
        name: String = "",
        tags: MutableSet<String> = mutableSetOf(),
        @Column(nullable = false) override var repCountLowerBound: Int = 0,
        @Column(nullable = false) override var repCountUpperBound: Int = 0
) : Item(id, name, tags), Exercise

data class Exercise(val name: String, val repCount: Int)

fun Exercise.getExercise() = Exercise(name, Random.nextInt(repCountLowerBound, repCountUpperBound))
