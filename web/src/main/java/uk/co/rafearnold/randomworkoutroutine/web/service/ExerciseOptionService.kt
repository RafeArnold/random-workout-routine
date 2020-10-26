package uk.co.rafearnold.randomworkoutroutine.web.service;

import uk.co.rafearnold.randomworkoutroutine.web.model.entity.ExerciseOption;
import uk.co.rafearnold.randomworkoutroutine.web.repository.ExerciseOptionRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ExerciseOptionService extends ItemService<ExerciseOption> {

    private final GroupService groupService;

    public ExerciseOptionService(ExerciseOptionRepository repository, GroupService groupService) {
        super(repository);
        this.groupService = groupService;
    }

    public void delete(ExerciseOption item) {
        groupService.removeExerciseFromAll(item);
        super.delete(item);
    }

    @Override
    protected ExerciseOption createItem(UUID id, String name) {
        ExerciseOption option = new ExerciseOption();
        option.setId(id);
        option.setName(name);
        return option;
    }
}
