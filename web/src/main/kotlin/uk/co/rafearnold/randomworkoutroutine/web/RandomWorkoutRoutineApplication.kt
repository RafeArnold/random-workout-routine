package uk.co.rafearnold.randomworkoutroutine.web

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class RandomWorkoutRoutineApplication

fun main(vararg args: String) {
    runApplication<RandomWorkoutRoutineApplication>(*args)
}
