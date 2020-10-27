package uk.co.rafearnold.randomworkoutroutine.web.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*
import javax.persistence.*

@Entity(name = "grouping")
class Group(
        id: UUID = UUID.randomUUID(),
        name: String = "",
        tags: MutableSet<String> = mutableSetOf(),
        @ManyToMany(fetch = FetchType.EAGER) var exerciseOptions: MutableList<ExerciseOption> = mutableListOf()
) : Item(id, name, tags) {

    @Transient
    @JsonIgnore
    var optionWeights: DoubleArray = DoubleArray(0)
}
