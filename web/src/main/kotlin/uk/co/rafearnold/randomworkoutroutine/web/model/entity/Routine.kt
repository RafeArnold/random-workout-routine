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
) : Item(id, name, tags), Routine
