package net.ddns.rarnold.randomworkoutroutine.controller;

import lombok.RequiredArgsConstructor;
import net.ddns.rarnold.randomworkoutroutine.model.Group;
import net.ddns.rarnold.randomworkoutroutine.service.GroupService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping("/save")
    public void save(@RequestBody Group group) {
        groupService.save(group);
    }

    @DeleteMapping("/delete/{name}")
    public void delete(@PathVariable String name) {
        groupService.delete(name);
    }
}
