package net.ddns.rarnold.randomworkoutroutine.controller;

import lombok.RequiredArgsConstructor;
import net.ddns.rarnold.randomworkoutroutine.model.ExerciseOption;
import net.ddns.rarnold.randomworkoutroutine.service.ExerciseOptionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exercise")
@RequiredArgsConstructor
public class ExerciseOptionController {

    private final ExerciseOptionService exerciseOptionService;

    @PostMapping("/save")
    public void save(@RequestBody ExerciseOption option) {
        exerciseOptionService.save(option);
    }

    @DeleteMapping("/delete/{name}")
    public void delete(@PathVariable String name) {
        exerciseOptionService.delete(name);
    }
}
