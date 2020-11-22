package uk.co.rafearnold.randomworkoutroutine.web.model.entity

import uk.co.rafearnold.randomworkoutroutine.model.Exercise
import java.util.*
import javax.persistence.Entity

/**
 * An implementation of [Exercise] for JPA.
 */
@Entity
class Exercise(
    id: UUID = UUID.randomUUID(),
    name: String = "",
    tags: MutableSet<String> = mutableSetOf()
) : Item(id, name, tags), Exercise {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Exercise) return false

        if (id != other.id) return false
        if (name != other.name) return false
        if (tags != other.tags) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + tags.hashCode()
        return result
    }
}
