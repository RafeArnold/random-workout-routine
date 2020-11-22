package uk.co.rafearnold.randomworkoutroutine.web.service

import org.springframework.stereotype.Service
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Exercise
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.ExerciseOption
import uk.co.rafearnold.randomworkoutroutine.web.repository.ExerciseRepository

/**
 * Service for retrieving and manipulating [Exercise] entities.
 */
@Service
class ExerciseService(
    repository: ExerciseRepository,
    private val routineService: RoutineService
) : ItemService<Exercise>(repository) {

    /**
     * No-op, since no children to save.
     */
    override fun saveChildren(item: Exercise) {}

    /**
     * Deletes [item] and any [ExerciseOption]s associated with it (i.e. any [ExerciseOption] whose
     * [ExerciseOption.exercise] property equals [item]).
     */
    override fun delete(item: Exercise) {
        routineService.deleteOptionsByExercise(item)
        super.delete(item)
    }
}
