package uk.co.rafearnold.randomworkoutroutine.web.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Exercise
import uk.co.rafearnold.randomworkoutroutine.web.service.ExerciseService

@RestController
@RequestMapping("/api/exercise")
class ExerciseController(service: ExerciseService) : ItemController<Exercise>(service)
