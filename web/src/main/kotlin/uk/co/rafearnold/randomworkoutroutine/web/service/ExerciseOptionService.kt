package uk.co.rafearnold.randomworkoutroutine.web.service

import org.springframework.stereotype.Service
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.ExerciseOption
import uk.co.rafearnold.randomworkoutroutine.web.repository.ExerciseOptionRepository

@Service
class ExerciseOptionService(
    repository: ExerciseOptionRepository,
    private val groupService: GroupService
) : ItemService<ExerciseOption>(repository) {

    override fun delete(item: ExerciseOption) {
        groupService.removeExerciseFromAll(item)
        super.delete(item)
    }
}
