package uk.co.rafearnold.randomworkoutroutine.web.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Group
import uk.co.rafearnold.randomworkoutroutine.web.service.GroupService

@RestController
@RequestMapping("/api/group")
class GroupController(service: GroupService) : ItemController<Group>(service)
