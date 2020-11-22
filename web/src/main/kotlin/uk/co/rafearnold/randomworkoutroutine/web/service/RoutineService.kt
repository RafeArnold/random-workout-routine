package uk.co.rafearnold.randomworkoutroutine.web.service

import org.springframework.stereotype.Service
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Exercise
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.ExerciseOption
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Group
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Routine
import uk.co.rafearnold.randomworkoutroutine.web.repository.ExerciseOptionRepository
import uk.co.rafearnold.randomworkoutroutine.web.repository.GroupRepository
import uk.co.rafearnold.randomworkoutroutine.web.repository.RoutineRepository
import java.util.*

/**
 * Service for retrieving and manipulating [Routine] entities.
 */
@Service
class RoutineService(
    routineRepository: RoutineRepository,
    private val exerciseOptionRepository: ExerciseOptionRepository,
    private val groupRepository: GroupRepository
) : ItemService<Routine>(routineRepository) {

    override fun saveChildren(item: Routine) {
        val groups: List<Group> = item.groups
        exerciseOptionRepository.saveAll(groups.flatMap { it.exercises })
        groupRepository.saveAll(groups)
    }

    fun deleteOptionsByExercise(exercise: Exercise) {
        val exerciseOptions: Iterable<ExerciseOption> = exerciseOptionRepository.findAllByExercise(exercise)
        removeAllExerciseOptionsFromGroups(exerciseOptions)
        exerciseOptionRepository.deleteAll(exerciseOptions)
    }

    private fun removeAllExerciseOptionsFromGroups(exerciseOptions: Iterable<ExerciseOption>) {
        val groups: Set<Group> =
            exerciseOptions.flatMap { groupRepository.findAllByExercisesContaining(it) }.toSet()
        val exerciseOptionIds: List<UUID> = exerciseOptions.map { it.id }
        for (group in groups) {
            group.exercises = group.exercises.filter { it.id !in exerciseOptionIds }.toMutableList()
        }
        groupRepository.saveAll(groups)
    }

    override fun delete(item: Routine) {
        super.delete(item)
        val groups: List<Group> = item.groups
        groupRepository.deleteAll(groups)
        exerciseOptionRepository.deleteAll(groups.flatMap { it.exercises })
    }
}
