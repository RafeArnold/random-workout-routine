package uk.co.rafearnold.randomworkoutroutine.web.controller;

import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Group;
import uk.co.rafearnold.randomworkoutroutine.web.service.GroupService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/group")
public class GroupController extends ItemController<Group> {

    public GroupController(GroupService service) {
        super(service);
    }
}
