package net.ddns.rarnold.randomworkoutroutine.service;

import lombok.RequiredArgsConstructor;
import net.ddns.rarnold.randomworkoutroutine.model.Group;
import net.ddns.rarnold.randomworkoutroutine.repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository repository;

    public void save(Group group) {
        repository.save(group);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public List<Group> getNames() {
        List<Object[]> groups = repository.findAllIdsAndNames();
        return groups.stream()
                .map(objects -> {
                    Group group = new Group();
                    group.setId((UUID) objects[0]);
                    group.setName((String) objects[1]);
                    return group;
                })
                .collect(Collectors.toList());
    }
}
