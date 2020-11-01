package uk.co.rafearnold.randomworkoutroutine.web.model.entity

import uk.co.rafearnold.randomworkoutroutine.model.Group
import java.util.*
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.ManyToMany

/**
 * An implementation of [Group] for JPA.
 */
@Entity(name = "grouping")
class Group(
    id: UUID = UUID.randomUUID(),
    name: String = "",
    tags: MutableSet<String> = mutableSetOf(),
    @ManyToMany(fetch = FetchType.EAGER) override var exercises: MutableList<ExerciseOption> = mutableListOf()
) : Item(id, name, tags), Group
