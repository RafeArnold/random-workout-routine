package net.ddns.rarnold.randomworkoutroutine.controller;

import lombok.RequiredArgsConstructor;
import net.ddns.rarnold.randomworkoutroutine.model.Exercise;
import net.ddns.rarnold.randomworkoutroutine.model.Routine;
import net.ddns.rarnold.randomworkoutroutine.service.RoutineService;
import net.ddns.rarnold.randomworkoutroutine.session.RoutineSession;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/routine")
@RequiredArgsConstructor
public class RoutineController {

    private final RoutineService routineService;

    @PostMapping("/save")
    public void save(@RequestBody Routine routine) {
        routineService.save(routine);
    }

    @DeleteMapping("/delete/{name}")
    public void delete(@PathVariable String name) {
        routineService.delete(name);
    }

    @PostMapping("/start/{name}")
    public Exercise start(HttpSession httpSession, @PathVariable String name) {
        httpSession.setAttribute(RoutineSession.ROUTINE_SESSION_ATTRIBUTE_NAME, new RoutineSession(routineService.getByName(name)));
        return nextExercise(httpSession);
    }

    @PostMapping("/next")
    public Exercise nextExercise(HttpSession httpSession) {
        RoutineSession routineSession = getRoutineSession(httpSession);
        return routineSession.nextExercise();
    }

    @GetMapping("/set-count")
    public int getSetCount(HttpSession httpSession) {
        RoutineSession routineSession = getRoutineSession(httpSession);
        return routineSession.getSetCount();
    }

    private static RoutineSession getRoutineSession(HttpSession httpSession) {
        return (RoutineSession) httpSession.getAttribute(RoutineSession.ROUTINE_SESSION_ATTRIBUTE_NAME);
    }
}
