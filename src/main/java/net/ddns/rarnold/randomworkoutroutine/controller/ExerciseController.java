package net.ddns.rarnold.randomworkoutroutine.controller;

import lombok.RequiredArgsConstructor;
import net.ddns.rarnold.randomworkoutroutine.model.Exercise;
import net.ddns.rarnold.randomworkoutroutine.service.ExerciseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exercise")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    @GetMapping("/next")
    public Exercise getNext() {
        return exerciseService.getRandomExercise();
    }
}
