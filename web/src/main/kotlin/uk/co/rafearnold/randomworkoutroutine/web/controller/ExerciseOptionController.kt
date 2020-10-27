package uk.co.rafearnold.randomworkoutroutine.web.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.ExerciseOption
import uk.co.rafearnold.randomworkoutroutine.web.service.ExerciseOptionService

@RestController
@RequestMapping("/api/exercise")
class ExerciseOptionController(service: ExerciseOptionService) : ItemController<ExerciseOption>(service)
