package net.ddns.rarnold.randomworkoutroutine.controller;

import lombok.RequiredArgsConstructor;
import net.ddns.rarnold.randomworkoutroutine.model.Group;
import net.ddns.rarnold.randomworkoutroutine.service.GroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/{id}")
    public Group getById(@PathVariable UUID id) {
        return groupService.getById(id);
    }

    @PostMapping("/save")
    public void save(@RequestBody Group group) {
        groupService.save(group);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable UUID id) {
        groupService.delete(id);
    }

    @GetMapping("/names")
    public List<Group> getNames() {
        return groupService.getNames();
    }

    @GetMapping("/search")
    public List<Group> searchNames(@RequestBody String filter) {
        return groupService.searchNames(filter);
    }
}
