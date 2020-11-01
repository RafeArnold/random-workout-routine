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
    id: UUID = UUID.randomUUID(),
    name: String = "",
    tags: MutableSet<String> = mutableSetOf(),
    @ManyToMany(fetch = FetchType.EAGER) override var exercises: MutableList<ExerciseOption> = mutableListOf()
) : Item(id, name, tags), Group {

    /**
     * An array of weights determining the likelihood of each [ExerciseOption] in [exercises] being
     * chosen. Each value in the array corresponds to the [ExerciseOption] at the same index in
     * [exercises]. The higher a value, the more likely its corresponding [ExerciseOption] is to be
     * chosen.
     */
    @Transient
    @JsonIgnore
    var optionWeights: DoubleArray = DoubleArray(0)
}
