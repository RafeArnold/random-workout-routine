package uk.co.rafearnold.randomworkoutroutine.web.service

import org.springframework.stereotype.Service
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.ExerciseOption
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Group
import uk.co.rafearnold.randomworkoutroutine.web.repository.ExerciseOptionRepository

/**
 * Service for retrieving and manipulating [ExerciseOption] entities.
 */
@Service
class ExerciseOptionService(
    repository: ExerciseOptionRepository,
    private val groupService: GroupService
) : ItemService<ExerciseOption>(repository) {

    /**
     * Extension of [ItemService.delete] that also removes [item] from all [Group]s that contain
     * it.
     */
    override fun delete(item: ExerciseOption) {
        groupService.removeExerciseFromAll(item)
        super.delete(item)
    }
}
