package net.ddns.rarnold.randomworkoutroutine.controller;

import net.ddns.rarnold.randomworkoutroutine.model.entity.ExerciseOption;
import net.ddns.rarnold.randomworkoutroutine.service.ExerciseOptionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exercise")
public class ExerciseOptionController extends ItemController<ExerciseOption> {

    public ExerciseOptionController(ExerciseOptionService service) {
        super(service);
    }
}
