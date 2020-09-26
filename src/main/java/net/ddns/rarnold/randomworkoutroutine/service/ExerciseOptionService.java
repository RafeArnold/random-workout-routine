package net.ddns.rarnold.randomworkoutroutine.service;

import net.ddns.rarnold.randomworkoutroutine.model.entity.ExerciseOption;
import net.ddns.rarnold.randomworkoutroutine.repository.ExerciseOptionRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ExerciseOptionService extends ItemService<ExerciseOption> {

    private final GroupService groupService;

    public ExerciseOptionService(ExerciseOptionRepository repository, GroupService groupService) {
        super(repository);
        this.groupService = groupService;
    }

    public void delete(UUID id) {
        groupService.removeExerciseFromAll(getById(id));
        super.delete(id);
    }

    @Override
    protected ExerciseOption createItem(UUID id, String name) {
        ExerciseOption option = new ExerciseOption();
        option.setId(id);
        option.setName(name);
        return option;
    }
}
