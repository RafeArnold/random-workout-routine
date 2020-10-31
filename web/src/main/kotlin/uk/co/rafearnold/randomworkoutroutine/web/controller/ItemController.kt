package uk.co.rafearnold.randomworkoutroutine.web.controller

import org.springframework.web.bind.annotation.*
import uk.co.rafearnold.randomworkoutroutine.web.model.Filter
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Item
import uk.co.rafearnold.randomworkoutroutine.web.service.ItemService
import java.util.*

abstract class ItemController<T : Item>(protected val service: ItemService<T>) {

    // TODO: 404 when item doesn't exist.
    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): Optional<T> = service.getById(id)

    // TODO: Better response status when an existing item is provided without its ID.
    // TODO: Don't allow items with blank names.
    @PostMapping("/save")
    fun save(@RequestBody option: T) = service.save(option)

    @DeleteMapping("/delete/{id}")
    fun delete(@PathVariable id: UUID) = service.getById(id).ifPresent { item: T -> service.delete(item) }

    @GetMapping("/names")
    fun getNames(): List<T> = service.getNames()

    @PostMapping("/search")
    fun searchNames(@RequestBody(required = false) filter: Filter): List<T> = service.search(filter)
}
