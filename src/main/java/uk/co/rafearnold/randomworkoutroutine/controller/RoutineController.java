package uk.co.rafearnold.randomworkoutroutine.controller;

import uk.co.rafearnold.randomworkoutroutine.model.Exercise;
import uk.co.rafearnold.randomworkoutroutine.model.entity.Routine;
import uk.co.rafearnold.randomworkoutroutine.service.RoutineService;
import uk.co.rafearnold.randomworkoutroutine.session.RoutineSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/routine")
public class RoutineController extends ItemController<Routine> {

    public RoutineController(RoutineService service) {
        super(service);
    }

    // TODO: Should all session endpoints be moved to a "session" controller?
    @GetMapping("/in-progress")
    public boolean isInProgress(HttpSession httpSession) {
        return httpSession.getAttribute(RoutineSession.ROUTINE_SESSION_ATTRIBUTE_NAME) != null;
    }

    @PostMapping("/start/{id}")
    public ResponseEntity<Object> start(HttpSession httpSession,
                                        HttpServletResponse response,
                                        @PathVariable UUID id) throws IOException {
        if (isInProgress(httpSession)) {
            // Session already in progress.
            response.sendError(HttpStatus.CONFLICT.value(), "Session already in progress");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Optional<Routine> routineOptional = service.getById(id);
        if (routineOptional.isEmpty()) {
            // No routine with the requested ID.
            response.sendError(HttpStatus.NOT_FOUND.value(), "No routine with ID '" + id + "' found.");
            return ResponseEntity.notFound().build();
        }
        Routine routine = routineOptional.get();
        if (routine.getGroups() == null || routine.getGroups().isEmpty()) {
            // Routine does not contain any groups.
            response.sendError(HttpStatus.CONFLICT.value(), "Routine does not contain any groups");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        if (routine.getGroups().stream().anyMatch(group ->
                group.getExerciseOptions() == null || group.getExerciseOptions().isEmpty())) {
            // Routine contains a group that does not contain any exercises.
            response.sendError(HttpStatus.CONFLICT.value(), "Routine contains a group that does not contain any exercises");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        httpSession.setAttribute(RoutineSession.ROUTINE_SESSION_ATTRIBUTE_NAME, new RoutineSession(routine));
        // Return the session's first exercise.
        return ResponseEntity.ok(nextExercise(httpSession));
    }

    // TODO: Error code when user attempts to contact session endpoints when there is no active
    //  session. Maybe 409.
    @PostMapping("/next")
    public ResponseEntity<Exercise> nextExercise(HttpSession httpSession) {
        return ResponseEntity.of(getRoutineSession(httpSession).map(RoutineSession::nextExercise));
    }

    // TODO: This should probably not execute nextExercise when there is no current exercise. Just
    //  give an error (like 409). Then this method can be a GET.
    @PostMapping("/current")
    public ResponseEntity<Exercise> getCurrentExercise(HttpSession httpSession) {
        return ResponseEntity.of(getRoutineSession(httpSession).map(RoutineSession::getCurrentExercise));
    }

    @GetMapping("/set-count")
    public ResponseEntity<Integer> getSetCount(HttpSession httpSession) {
        return ResponseEntity.of(getRoutineSession(httpSession).map(RoutineSession::getSetCount));
    }

    @PostMapping("/stop")
    public void stop(HttpSession httpSession) {
        httpSession.removeAttribute(RoutineSession.ROUTINE_SESSION_ATTRIBUTE_NAME);
    }

    private static Optional<RoutineSession> getRoutineSession(HttpSession httpSession) {
        Object session = httpSession.getAttribute(RoutineSession.ROUTINE_SESSION_ATTRIBUTE_NAME);
        if (session != null) {
            return Optional.of((RoutineSession) session);
        }
        return Optional.empty();
    }
}
