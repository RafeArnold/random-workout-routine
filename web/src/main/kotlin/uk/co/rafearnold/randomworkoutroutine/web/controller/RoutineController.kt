package uk.co.rafearnold.randomworkoutroutine.web.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Routine
import uk.co.rafearnold.randomworkoutroutine.web.service.RoutineService

@RestController
@RequestMapping("/api/routine")
class RoutineController(service: RoutineService) : ItemController<Routine>(service)
