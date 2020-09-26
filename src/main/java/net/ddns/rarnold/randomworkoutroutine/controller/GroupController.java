package net.ddns.rarnold.randomworkoutroutine.controller;

import net.ddns.rarnold.randomworkoutroutine.model.entity.Group;
import net.ddns.rarnold.randomworkoutroutine.service.GroupService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/group")
public class GroupController extends ItemController<Group> {

    public GroupController(GroupService service) {
        super(service);
    }
}
