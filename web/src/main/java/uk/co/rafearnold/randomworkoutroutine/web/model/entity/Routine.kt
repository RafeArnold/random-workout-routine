package uk.co.rafearnold.randomworkoutroutine.web.model.entity

import java.util.*
import javax.persistence.*

@Entity
class Routine(
        id: UUID = UUID.randomUUID(),
        name: String = "",
        tags: MutableSet<String> = mutableSetOf(),
        @ManyToMany(fetch = FetchType.EAGER) @OrderColumn var groups: MutableList<Group> = mutableListOf()
) : Item(id, name, tags)
