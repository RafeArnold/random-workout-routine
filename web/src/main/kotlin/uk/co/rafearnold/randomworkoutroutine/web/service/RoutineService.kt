package uk.co.rafearnold.randomworkoutroutine.web.service

import org.springframework.stereotype.Service
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Group
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Routine
import uk.co.rafearnold.randomworkoutroutine.web.repository.RoutineRepository

@Service
class RoutineService(repository: RoutineRepository) : ItemService<Routine>(repository) {

    fun removeGroupFromAll(group: Group) {
        val routines = (repository as RoutineRepository).findAllByGroupsContaining(group)
        for (routine in routines) {
            routine.groups = routine.groups.stream()
                    .filter { g: Group -> g.id !== group.id }
                    .collect(Collectors.toList())
            repository.save(routine)
        }
    }
}
