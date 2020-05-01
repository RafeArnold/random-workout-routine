package net.ddns.rarnold.randomworkoutroutine.controller;

import lombok.RequiredArgsConstructor;
import net.ddns.rarnold.randomworkoutroutine.model.ExerciseOption;
import net.ddns.rarnold.randomworkoutroutine.service.ExerciseOptionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/exercise")
@RequiredArgsConstructor
public class ExerciseOptionController {

    private final ExerciseOptionService exerciseOptionService;

    @GetMapping("/{id}")
    public ExerciseOption getById(@PathVariable UUID id) {
        return exerciseOptionService.getById(id);
    }

    @PostMapping("/save")
    public void save(@RequestBody ExerciseOption option) {
        exerciseOptionService.save(option);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable UUID id) {
        exerciseOptionService.delete(id);
    }

    @GetMapping("/names")
    public List<ExerciseOption> getNames() {
        return exerciseOptionService.getNames();
    }

    @GetMapping("/search")
    public List<ExerciseOption> searchNames(@RequestBody String filter) {
        return exerciseOptionService.searchNames(filter);
    }
}
