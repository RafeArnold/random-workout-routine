package net.ddns.rarnold.randomworkoutroutine.controller;

import lombok.RequiredArgsConstructor;
import net.ddns.rarnold.randomworkoutroutine.model.Exercise;
import net.ddns.rarnold.randomworkoutroutine.model.Routine;
import net.ddns.rarnold.randomworkoutroutine.service.RoutineService;
import net.ddns.rarnold.randomworkoutroutine.session.RoutineSession;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/routine")
@RequiredArgsConstructor
public class RoutineController {

    private final RoutineService routineService;

    @PostMapping("/save")
    public void save(@RequestBody Routine routine) {
        routineService.save(routine);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable UUID id) {
        routineService.delete(id);
    }

    @GetMapping("/in-progress")
    public boolean isInProgress(HttpSession httpSession) {
        return httpSession.getAttribute(RoutineSession.ROUTINE_SESSION_ATTRIBUTE_NAME) != null;
    }

    @PostMapping("/start/{id}")
    public void start(HttpSession httpSession, @PathVariable UUID id) {
        httpSession.setAttribute(RoutineSession.ROUTINE_SESSION_ATTRIBUTE_NAME,
                new RoutineSession(routineService.getById(id)));
    }

    @PostMapping("/next")
    public Exercise nextExercise(HttpSession httpSession) {
        RoutineSession routineSession = getRoutineSession(httpSession);
        return routineSession.nextExercise();
    }

    @PostMapping("/current")
    public Exercise getCurrentExercise(HttpSession httpSession) {
        RoutineSession routineSession = getRoutineSession(httpSession);
        Exercise current = routineSession.getCurrentExercise();
        return current != null ? current : routineSession.nextExercise();
    }

    @GetMapping("/set-count")
    public int getSetCount(HttpSession httpSession) {
        RoutineSession routineSession = getRoutineSession(httpSession);
        return routineSession.getSetCount();
    }

    @PostMapping("/stop")
    public void stop(HttpSession httpSession) {
        httpSession.removeAttribute(RoutineSession.ROUTINE_SESSION_ATTRIBUTE_NAME);
    }

    @GetMapping("/names")
    public Set<String> getNames() {
        return routineService.getNames();
    }

    private static RoutineSession getRoutineSession(HttpSession httpSession) {
        return (RoutineSession) httpSession.getAttribute(RoutineSession.ROUTINE_SESSION_ATTRIBUTE_NAME);
    }
}
