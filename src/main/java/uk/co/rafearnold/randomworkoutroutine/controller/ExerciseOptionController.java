package uk.co.rafearnold.randomworkoutroutine.controller;

import uk.co.rafearnold.randomworkoutroutine.model.entity.ExerciseOption;
import uk.co.rafearnold.randomworkoutroutine.service.ExerciseOptionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exercise")
public class ExerciseOptionController extends ItemController<ExerciseOption> {

    public ExerciseOptionController(ExerciseOptionService service) {
        super(service);
    }
}
