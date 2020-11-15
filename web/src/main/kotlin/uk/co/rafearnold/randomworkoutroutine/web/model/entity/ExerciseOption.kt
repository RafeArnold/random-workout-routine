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
) : Item(id, name, tags), ExerciseOption {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ExerciseOption) return false

        if (id != other.id) return false
        if (name != other.name) return false
        if (tags != other.tags) return false
        if (repCountLowerBound != other.repCountLowerBound) return false
        if (repCountUpperBound != other.repCountUpperBound) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + tags.hashCode()
        result = 31 * result + repCountLowerBound
        result = 31 * result + repCountUpperBound
        return result
    }
}

/**
 * Creates an [Exercise] object whose name is equal to this [ExerciseOption] and whose
 * [Exercise.repCount] is between this [ExerciseOption]'s [ExerciseOption.repCountLowerBound]
 * and [ExerciseOption.repCountUpperBound] (both inclusive).
 */
fun ExerciseOption.getExercise(): Exercise =
    ExerciseImpl(name, Random.nextInt(repCountLowerBound, repCountUpperBound + 1))
