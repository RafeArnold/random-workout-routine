package uk.co.rafearnold.randomworkoutroutine.web.controller

import org.hibernate.exception.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import uk.co.rafearnold.randomworkoutroutine.model.SimpleItem
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Item
import uk.co.rafearnold.randomworkoutroutine.web.service.ItemService
import java.util.*
import javax.servlet.http.HttpServletResponse

abstract class ItemController<T : Item>(protected val service: ItemService<T>) {

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID, response: HttpServletResponse): Optional<T> {
        val optional: Optional<T> = service.getById(id)
        if (optional.isEmpty) {
            response.sendError(HttpStatus.NOT_FOUND.value(), "No item with ID '$id' found")
        }
        return optional
    }

    @PostMapping("/save")
    fun save(@RequestBody item: T, response: HttpServletResponse) {
        if (item.name.isBlank()) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Item name must not be blank")
            return
        }
        try {
            service.save(item)
        } catch (e: DataIntegrityViolationException) {
            // Check if it's a unique constraint violation.
            val cause = e.cause
            if (cause is ConstraintViolationException && cause.sqlState == "23505") {
                response.sendError(HttpStatus.CONFLICT.value(), cause.cause?.message)
            }
        }
    }

    @DeleteMapping("/delete/{id}")
    fun delete(@PathVariable id: UUID) = service.getById(id).ifPresent { item: T -> service.delete(item) }

    @GetMapping("/names")
    fun getNames(): List<SimpleItem> = service.getNames()

    @GetMapping("/search")
    fun searchNames(
        @RequestParam(name = "term", required = false, defaultValue = "") searchTerm: String,
        @RequestParam(name = "exclude", required = false, defaultValue = "") excludedNames: Set<String>
    ): List<SimpleItem> = service.search(searchTerm, excludedNames)
}
