package uk.co.rafearnold.randomworkoutroutine.web.service

import org.springframework.stereotype.Service
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.ExerciseOption
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Group
import uk.co.rafearnold.randomworkoutroutine.web.repository.GroupRepository
import java.util.*
import java.util.stream.Collectors

@Service
class GroupService(repository: GroupRepository, private val routineService: RoutineService) : ItemService<Group>(repository) {

    override fun delete(item: Group) {
        routineService.removeGroupFromAll(item)
        super.delete(item)
    }

    fun removeExerciseFromAll(exercise: ExerciseOption) {
        val groups = (repository as GroupRepository).findAllByExerciseOptionsContaining(exercise)
        for (group in groups) {
            group.exerciseOptions = group.exerciseOptions.stream()
                    .filter { e: ExerciseOption -> e.id !== exercise.id }
                    .collect(Collectors.toList())
            repository.save(group)
        }
    }

    override fun createItem(id: UUID, name: String): Group = Group(id, name)
}
