package uk.co.rafearnold.randomworkoutroutine.web.model.entity

import uk.co.rafearnold.randomworkoutroutine.model.Group
import java.util.*
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.ManyToMany
import javax.persistence.OrderColumn

/**
 * An implementation of [Group] for JPA.
 */
@Entity(name = "grouping")
class Group(
    id: UUID = UUID.randomUUID(),
    name: String = "",
    tags: MutableSet<String> = mutableSetOf(),
    @ManyToMany(fetch = FetchType.EAGER) @OrderColumn override var exercises: MutableList<ExerciseOption> = mutableListOf()
) : Item(id, name, tags), Group {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Group) return false

        if (id != other.id) return false
        if (name != other.name) return false
        if (tags != other.tags) return false
        if (exercises != other.exercises) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + tags.hashCode()
        result = 31 * result + exercises.hashCode()
        return result
    }
}
