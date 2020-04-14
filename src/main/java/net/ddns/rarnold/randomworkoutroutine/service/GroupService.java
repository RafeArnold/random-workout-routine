package net.ddns.rarnold.randomworkoutroutine.service;

import lombok.RequiredArgsConstructor;
import net.ddns.rarnold.randomworkoutroutine.model.Group;
import net.ddns.rarnold.randomworkoutroutine.repository.GroupRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository repository;

    public void save(Group group) {
        repository.save(group);
    }
}
