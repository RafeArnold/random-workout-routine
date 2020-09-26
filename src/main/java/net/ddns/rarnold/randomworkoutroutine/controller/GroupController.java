package net.ddns.rarnold.randomworkoutroutine.controller;

import net.ddns.rarnold.randomworkoutroutine.model.entity.Group;
import net.ddns.rarnold.randomworkoutroutine.service.ItemService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/group")
public class GroupController extends ItemController<Group> {

    public GroupController(ItemService<Group> service) {
        super(service);
    }
}
