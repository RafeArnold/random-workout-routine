package uk.co.rafearnold.randomworkoutroutine.web.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.ExerciseOption
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Group
import java.util.*

@Repository
interface GroupRepository : CrudRepository<Group, UUID> {
    fun findAllByExercisesContaining(exercise: ExerciseOption): List<Group>
}
