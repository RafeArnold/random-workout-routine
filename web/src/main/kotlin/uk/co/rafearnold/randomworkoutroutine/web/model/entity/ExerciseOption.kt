package uk.co.rafearnold.randomworkoutroutine.web.model.entity

import uk.co.rafearnold.randomworkoutroutine.model.ExerciseSet
import uk.co.rafearnold.randomworkoutroutine.model.ExerciseSetImpl
import uk.co.rafearnold.randomworkoutroutine.model.ExerciseOption
import java.util.*
import javax.persistence.*
import kotlin.random.Random

/**
 * An implementation of [ExerciseOption] for JPA.
 */
@Entity
class ExerciseOption(
    @Id override var id: UUID = UUID.randomUUID(),
    @ManyToOne override var exercise: Exercise = Exercise(),
    @Column(nullable = false) override var repCountLowerBound: Int = 0,
    @Column(nullable = false) override var repCountUpperBound: Int = 0
) : ExerciseOption {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ExerciseOption) return false

        if (id != other.id) return false
        if (exercise != other.exercise) return false
        if (repCountLowerBound != other.repCountLowerBound) return false
        if (repCountUpperBound != other.repCountUpperBound) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + exercise.hashCode()
        result = 31 * result + repCountLowerBound
        result = 31 * result + repCountUpperBound
        return result
    }
}

/**
 * Creates an [ExerciseSet] object whose name is equal to this [ExerciseOption] and whose
 * [ExerciseSet.repCount] is between this [ExerciseOption]'s [ExerciseOption.repCountLowerBound]
 * and [ExerciseOption.repCountUpperBound] (both inclusive).
 */
fun ExerciseOption.getSet(): ExerciseSet =
    ExerciseSetImpl(exercise.name, Random.nextInt(repCountLowerBound, repCountUpperBound + 1))
