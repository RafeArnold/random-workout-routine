package uk.co.rafearnold.randomworkoutroutine.web.repository

import org.springframework.stereotype.Repository
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.ExerciseOption
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Group

@Repository
interface GroupRepository : ItemRepository<Group> {
    fun findAllByExerciseOptionsContaining(exercise: ExerciseOption?): List<Group>
}
