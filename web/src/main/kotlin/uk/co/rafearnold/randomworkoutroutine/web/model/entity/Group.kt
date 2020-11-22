package uk.co.rafearnold.randomworkoutroutine.web.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import uk.co.rafearnold.randomworkoutroutine.model.Group
import java.util.*
import javax.persistence.*

/**
 * An implementation of [Group] for JPA.
 */
@Entity(name = "grouping")
class Group(
    @Id
    override var id: UUID = UUID.randomUUID(),
    @OneToMany(fetch = FetchType.EAGER)
    @OrderColumn
    override var exercises: MutableList<ExerciseOption> = mutableListOf(),
    @ManyToOne(cascade = [CascadeType.ALL])
    @JsonIgnore
    var routine: Routine? = null
) : Group {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Group) return false

        if (id != other.id) return false
        if (exercises != other.exercises) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + exercises.hashCode()
        return result
    }
}
