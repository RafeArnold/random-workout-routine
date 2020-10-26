package uk.co.rafearnold.randomworkoutroutine.web.controller;

import uk.co.rafearnold.randomworkoutroutine.web.model.entity.ExerciseOption;
import uk.co.rafearnold.randomworkoutroutine.web.service.ExerciseOptionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exercise")
public class ExerciseOptionController extends ItemController<ExerciseOption> {

    public ExerciseOptionController(ExerciseOptionService service) {
        super(service);
    }
}
