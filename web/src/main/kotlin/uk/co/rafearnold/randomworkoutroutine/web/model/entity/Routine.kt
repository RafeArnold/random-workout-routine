package uk.co.rafearnold.randomworkoutroutine.web.model.entity

import uk.co.rafearnold.randomworkoutroutine.model.Routine
import java.util.*
import javax.persistence.*

/**
 * An implementation of [Routine] for JPA.
 */
@Entity
class Routine(
    id: UUID = UUID.randomUUID(),
    name: String = "",
    tags: MutableSet<String> = mutableSetOf(),
    @ManyToMany(fetch = FetchType.EAGER) @OrderColumn override var groups: MutableList<Group> = mutableListOf()
) : Item(id, name, tags), Routine {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Routine) return false

        if (id != other.id) return false
        if (name != other.name) return false
        if (tags != other.tags) return false
        if (groups != other.groups) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + tags.hashCode()
        result = 31 * result + groups.hashCode()
        return result
    }
}
