package uk.co.rafearnold.randomworkoutroutine.web.service

import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Item
import uk.co.rafearnold.randomworkoutroutine.web.repository.ItemRepository
import java.util.*
import java.util.stream.Collectors

abstract class ItemService<T : Item>(protected val repository: ItemRepository<T>) {

    fun getById(id: UUID): Optional<T> = repository.findById(id)

    // TODO: Don't allow saving of routines and groups with no children. This is a problem when the
    //  user deletes a group or exercise that is the only child of a routine or group. Maybe just
    //  stop the user starting sessions if the routine or a group in the routine has no children.
    fun save(item: T) {
        repository.save(item)
    }

    open fun delete(item: T) {
        repository.delete(item)
    }

    fun getNames(): List<T> = transformIdAndNameToItem(repository.findAllIdsAndNames())

    fun search(searchTerm: String, excludedNames: Set<String>): List<T> =
        transformIdAndNameToItem(
            repository.findAllIdsAndNames(searchTerm, if (excludedNames.isEmpty()) setOf("") else excludedNames)
        )

    private fun transformIdAndNameToItem(idsAndNames: List<Array<Any>>): List<T> =
        idsAndNames.stream()
            .map { objects: Array<Any> -> createItem(objects[0] as UUID, objects[1] as String) }
            .collect(Collectors.toList())

    protected abstract fun createItem(id: UUID, name: String): T
}
