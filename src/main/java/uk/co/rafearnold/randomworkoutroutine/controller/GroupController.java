package uk.co.rafearnold.randomworkoutroutine.controller;

import uk.co.rafearnold.randomworkoutroutine.model.entity.Group;
import uk.co.rafearnold.randomworkoutroutine.service.GroupService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/group")
public class GroupController extends ItemController<Group> {

    public GroupController(GroupService service) {
        super(service);
    }
}
