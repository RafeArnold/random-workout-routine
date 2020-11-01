package uk.co.rafearnold.randomworkoutroutine.web.service

import org.springframework.stereotype.Service
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.ExerciseOption
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Group
import uk.co.rafearnold.randomworkoutroutine.web.repository.GroupRepository

@Service
class GroupService(
    repository: GroupRepository,
    private val routineService: RoutineService
) : ItemService<Group>(repository) {

    override fun delete(item: Group) {
        routineService.removeGroupFromAll(item)
        super.delete(item)
    }

    fun removeExerciseFromAll(exercise: ExerciseOption) {
        val groups = (repository as GroupRepository).findAllByExercisesContaining(exercise)
        for (group in groups) {
            group.exercises = group.exercises.stream()
                    .filter { e: ExerciseOption -> e.id !== exercise.id }
                    .collect(Collectors.toList())
            repository.save(group)
        }
    }
}
