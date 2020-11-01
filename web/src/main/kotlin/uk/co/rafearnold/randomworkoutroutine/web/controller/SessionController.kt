package uk.co.rafearnold.randomworkoutroutine.web.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import uk.co.rafearnold.randomworkoutroutine.model.Exercise
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Group
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Routine
import uk.co.rafearnold.randomworkoutroutine.web.service.RoutineService
import uk.co.rafearnold.randomworkoutroutine.web.session.RoutineSession
import java.util.*
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

/**
 * A controller for managing a user's workout session.
 */
@RestController
@RequestMapping("/api/session")
class SessionController(private val routineService: RoutineService) {

    /**
     * Returns a boolean that indicates if the user has a session in progress.
     */
    @GetMapping("/in-progress")
    fun isInProgress(httpSession: HttpSession): Boolean =
        httpSession.getAttribute(RoutineSession.ROUTINE_SESSION_ATTRIBUTE_NAME) != null

    /**
     * Begins a new workout session with the [Routine] associated with [id]. A new session can not
     * be started if the user already has one in progress.
     *
     * @return The first [Exercise] of the session.
     */
    @PostMapping("/start/{id}")
    fun start(
        httpSession: HttpSession,
        response: HttpServletResponse,
        @PathVariable id: UUID
    ): Optional<Exercise> {
        if (isInProgress(httpSession)) {
            // Session already in progress.
            response.sendError(HttpStatus.CONFLICT.value(), "Session already in progress")
            return Optional.empty()
        }
        val routineOptional: Optional<Routine> = routineService.getById(id)
        if (routineOptional.isEmpty) {
            // No routine with the requested ID.
            response.sendError(HttpStatus.NOT_FOUND.value(), "No routine with ID '$id' found.")
            return Optional.empty()
        }
        val routine = routineOptional.get()
        if (routine.groups.isEmpty()) {
            // Routine does not contain any groups.
            response.sendError(HttpStatus.CONFLICT.value(), "Session does not contain any groups")
            return Optional.empty()
        }
        if (routine.groups.stream().anyMatch { group: Group -> group.exercises.isEmpty() }) {
            // Routine contains a group that does not contain any exercises.
            response.sendError(
                HttpStatus.CONFLICT.value(),
                "Session contains a group that does not contain any exercises"
            )
            return Optional.empty()
        }
        val session = RoutineSession(routine)
        httpSession.setAttribute(RoutineSession.ROUTINE_SESSION_ATTRIBUTE_NAME, session)
        // Return the session's first exercise.
        return Optional.of(session.currentExercise)
    }

    /**
     * Returns the next [Exercise] of the currently active session.
     */
    @PostMapping("/next")
    fun nextExercise(httpSession: HttpSession, response: HttpServletResponse): Optional<Exercise> {
        if (!isInProgress(httpSession)) {
            // No active session.
            response.sendError(HttpStatus.CONFLICT.value(), "No active session")
            return Optional.empty()
        }
        return getRoutineSession(httpSession).map { it.nextExercise() }
    }

    /**
     * Returns the current [Exercise] of the currently active session.
     */
    @GetMapping("/current")
    fun getCurrentExercise(httpSession: HttpSession, response: HttpServletResponse): Optional<Exercise> {
        if (!isInProgress(httpSession)) {
            // No active session.
            response.sendError(HttpStatus.CONFLICT.value(), "No active session")
            return Optional.empty()
        }
        return getRoutineSession(httpSession).map { it.currentExercise }
    }

    /**
     * Returns the set count of the currently active session.
     */
    @GetMapping("/set-count")
    fun getSetCount(httpSession: HttpSession, response: HttpServletResponse): Optional<Int> {
        if (!isInProgress(httpSession)) {
            // No active session.
            response.sendError(HttpStatus.CONFLICT.value(), "No active session")
            return Optional.empty()
        }
        return getRoutineSession(httpSession).map { it.setCount }
    }

    /**
     * Stops the currently active session.
     */
    @PostMapping("/stop")
    fun stop(httpSession: HttpSession, response: HttpServletResponse) {
        if (!isInProgress(httpSession)) {
            // No active session.
            response.sendError(HttpStatus.CONFLICT.value(), "No active session")
        }
        return httpSession.removeAttribute(RoutineSession.ROUTINE_SESSION_ATTRIBUTE_NAME)
    }

    companion object {
        /**
         * @return The currently active [RoutineSession] from [httpSession] or null if there is no
         * active session.
         */
        private fun getRoutineSession(httpSession: HttpSession): Optional<RoutineSession> {
            val session = httpSession.getAttribute(RoutineSession.ROUTINE_SESSION_ATTRIBUTE_NAME)
            return if (session != null) Optional.of(session as RoutineSession) else Optional.empty()
        }
    }
}