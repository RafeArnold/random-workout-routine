package uk.co.rafearnold.randomworkoutroutine.web.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import uk.co.rafearnold.randomworkoutroutine.model.Group
import java.util.*
import javax.persistence.*

@Entity(name = "grouping")
class Group(
        id: UUID = UUID.randomUUID(),
        name: String = "",
        tags: MutableSet<String> = mutableSetOf(),
        @ManyToMany(fetch = FetchType.EAGER) override var exercises: MutableList<ExerciseOption> = mutableListOf()
) : Item(id, name, tags), Group {

    @Transient
    @JsonIgnore
    var optionWeights: DoubleArray = DoubleArray(0)
}
