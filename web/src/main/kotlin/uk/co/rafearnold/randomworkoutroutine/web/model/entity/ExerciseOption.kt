package uk.co.rafearnold.randomworkoutroutine.web.model.entity

import uk.co.rafearnold.randomworkoutroutine.model.Exercise
import uk.co.rafearnold.randomworkoutroutine.model.ExerciseImpl
import uk.co.rafearnold.randomworkoutroutine.model.ExerciseOption
import java.util.*
import javax.persistence.*
import kotlin.random.Random

/**
 * An implementation of [ExerciseOption] for JPA.
 */
@Entity
class ExerciseOption(
    id: UUID = UUID.randomUUID(),
    name: String = "",
    tags: MutableSet<String> = mutableSetOf(),
    @Column(nullable = false) override var repCountLowerBound: Int = 0,
    @Column(nullable = false) override var repCountUpperBound: Int = 0
) : Item(id, name, tags), ExerciseOption

/**
 * Creates an [Exercise] object whose name is equal to this [ExerciseOption] and whose
 * [Exercise.repCount] is between this [ExerciseOption]'s [ExerciseOption.repCountLowerBound]
 * and [ExerciseOption.repCountUpperBound] (both inclusive).
 */
fun ExerciseOption.getExercise(): Exercise =
    ExerciseImpl(name, Random.nextInt(repCountLowerBound, repCountUpperBound + 1))
