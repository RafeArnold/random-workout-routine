package net.ddns.rarnold.randomworkoutroutine.init;

import lombok.RequiredArgsConstructor;
import net.ddns.rarnold.randomworkoutroutine.model.ExerciseOption;
import net.ddns.rarnold.randomworkoutroutine.model.Group;
import net.ddns.rarnold.randomworkoutroutine.model.Routine;
import net.ddns.rarnold.randomworkoutroutine.repository.ExerciseOptionRepository;
import net.ddns.rarnold.randomworkoutroutine.repository.GroupRepository;
import net.ddns.rarnold.randomworkoutroutine.repository.RoutineRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {
    private final RoutineRepository routineRepository;
    private final GroupRepository groupRepository;
    private final ExerciseOptionRepository optionRepository;

    @Override
    public void run(ApplicationArguments args) {
        List<ExerciseOption> options = new ArrayList<>();
        options.add(new ExerciseOption("sit ups", 8, 25));
        options.add(new ExerciseOption("push ups", 5, 20));
        options.add(new ExerciseOption("tricep dips", 10, 25));
        options.add(new ExerciseOption("rope climbs", 1, 5));
        options.add(new ExerciseOption("monkey bars", 1, 4));
        options.add(new ExerciseOption("bicep curls", 8, 26));
        for (ExerciseOption option : options) {
            optionRepository.save(option);
        }
        Routine routine = new Routine();
        routine.setName("mix");
        routineRepository.save(routine);
        groupRepository.save(new Group(routine, 0, Arrays.asList(options.get(0), options.get(1), options.get(2))));
        groupRepository.save(new Group(routine, 1, Arrays.asList(options.get(3), options.get(4), options.get(5))));
    }
}