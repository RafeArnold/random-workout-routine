package net.ddns.rarnold.randomworkoutroutine.controller;

import lombok.RequiredArgsConstructor;
import net.ddns.rarnold.randomworkoutroutine.model.ExerciseOption;
import net.ddns.rarnold.randomworkoutroutine.service.ExerciseOptionService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/exercise")
@RequiredArgsConstructor
public class ExerciseOptionController {

    private final ExerciseOptionService exerciseOptionService;

    @PostMapping("/save")
    public void save(@RequestBody ExerciseOption option) {
        exerciseOptionService.save(option);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable UUID id) {
        exerciseOptionService.delete(id);
    }

    @GetMapping("/names")
    public Set<String> getNames() {
        return exerciseOptionService.getNames();
    }
}
