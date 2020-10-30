package uk.co.rafearnold.randomworkoutroutine.web.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Exercise
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Group
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Routine
import uk.co.rafearnold.randomworkoutroutine.web.service.RoutineService
import uk.co.rafearnold.randomworkoutroutine.web.session.RoutineSession
import java.util.*
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

@RestController
@RequestMapping("/api/routine")
class RoutineController(service: RoutineService) : ItemController<Routine>(service) {

    // TODO: Should all session endpoints be moved to a "session" controller?
    @GetMapping("/in-progress")
    fun isInProgress(httpSession: HttpSession): Boolean =
            httpSession.getAttribute(RoutineSession.ROUTINE_SESSION_ATTRIBUTE_NAME) != null

    @PostMapping("/start/{id}")
    fun start(httpSession: HttpSession,
              response: HttpServletResponse,
              @PathVariable id: UUID): ResponseEntity<Exercise> {
        if (isInProgress(httpSession)) {
            // Session already in progress.
            response.sendError(HttpStatus.CONFLICT.value(), "Session already in progress")
            return ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
        val routineOptional: Optional<Routine> = service.getById(id)
        if (routineOptional.isEmpty) {
            // No routine with the requested ID.
            response.sendError(HttpStatus.NOT_FOUND.value(), "No routine with ID '$id' found.")
            return ResponseEntity.notFound().build()
        }
        val routine = routineOptional.get()
        if (routine.groups.isEmpty()) {
            // Routine does not contain any groups.
            response.sendError(HttpStatus.CONFLICT.value(), "Routine does not contain any groups")
            return ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
        if (routine.groups.stream().anyMatch { group: Group -> group.exercises.isEmpty() }) {
            // Routine contains a group that does not contain any exercises.
            response.sendError(HttpStatus.CONFLICT.value(), "Routine contains a group that does not contain any exercises")
            return ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
        val session = RoutineSession(routine)
        httpSession.setAttribute(RoutineSession.ROUTINE_SESSION_ATTRIBUTE_NAME, session)
        // Return the session's first exercise.
        return ResponseEntity.ok(session.currentExercise)
    }

    // TODO: Error code when user attempts to contact session endpoints when there is no active
    //  session. Maybe 409.
    @PostMapping("/next")
    fun nextExercise(httpSession: HttpSession): ResponseEntity<Exercise> =
            ResponseEntity.of(getRoutineSession(httpSession).map { it.nextExercise() })

    // TODO: This should probably not execute nextExercise when there is no current exercise. Just
    //  give an error (like 409). Then this method can be a GET.
    @PostMapping("/current")
    fun getCurrentExercise(httpSession: HttpSession): ResponseEntity<Exercise> =
            ResponseEntity.of(getRoutineSession(httpSession).map { it.currentExercise })

    @GetMapping("/set-count")
    fun getSetCount(httpSession: HttpSession): ResponseEntity<Int> =
            ResponseEntity.of(getRoutineSession(httpSession).map { it.setCount })

    @PostMapping("/stop")
    fun stop(httpSession: HttpSession) {
        httpSession.removeAttribute(RoutineSession.ROUTINE_SESSION_ATTRIBUTE_NAME)
    }

    companion object {
        private fun getRoutineSession(httpSession: HttpSession): Optional<RoutineSession> {
            val session = httpSession.getAttribute(RoutineSession.ROUTINE_SESSION_ATTRIBUTE_NAME)
            return if (session != null) {
                Optional.of(session as RoutineSession)
            } else Optional.empty()
        }
    }
}
