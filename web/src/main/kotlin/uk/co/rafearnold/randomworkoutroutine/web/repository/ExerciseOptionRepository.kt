package uk.co.rafearnold.randomworkoutroutine.web.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Exercise
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.ExerciseOption
import java.util.*

@Repository
interface ExerciseOptionRepository : CrudRepository<ExerciseOption, UUID> {
    fun findAllByExercise(exercise: Exercise): List<ExerciseOption>
}
