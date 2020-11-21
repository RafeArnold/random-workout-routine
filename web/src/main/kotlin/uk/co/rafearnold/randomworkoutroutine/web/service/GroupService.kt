package uk.co.rafearnold.randomworkoutroutine.web.service

import org.springframework.stereotype.Service
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.ExerciseOption
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Group
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Routine
import uk.co.rafearnold.randomworkoutroutine.web.repository.GroupRepository

/**
 * Service for retrieving and manipulating [Group] entities.
 */
@Service
class GroupService(
    repository: GroupRepository,
    private val routineService: RoutineService
) : ItemService<Group>(repository) {

    /**
     * Extension of [ItemService.delete] that also removes [item] from all [Routine]s that contain
     * it.
     */
    override fun delete(item: Group) {
        routineService.removeGroupFromAll(item)
        super.delete(item)
    }

    /**
     * Removes [exercise] from every [Group] whose [Group.exercises] property contains it.
     */
    fun removeExerciseFromAll(exercise: ExerciseOption) {
        val groups = (repository as GroupRepository).findAllByExercisesContaining(exercise)
        for (group in groups) {
            group.exercises = group.exercises.filter { it.id !== exercise.id }.toMutableList()
            repository.save(group)
        }
    }
}
