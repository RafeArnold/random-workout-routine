package uk.co.rafearnold.randomworkoutroutine.web.service

import uk.co.rafearnold.randomworkoutroutine.model.SimpleItem
import uk.co.rafearnold.randomworkoutroutine.model.SimpleItemImpl
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Item
import uk.co.rafearnold.randomworkoutroutine.web.repository.ItemRepository
import java.util.*

abstract class ItemService<T : Item>(protected val repository: ItemRepository<T>) {

    fun getById(id: UUID): Optional<T> = repository.findById(id)

    fun save(item: T) {
        repository.save(item)
    }

    open fun delete(item: T) {
        repository.delete(item)
    }

    fun getNames(): List<SimpleItem> = transformIdAndNameToItem(repository.findAllIdsAndNames())

    fun search(searchTerm: String, excludedNames: Set<String>): List<SimpleItem> =
        transformIdAndNameToItem(
            repository.findAllIdsAndNames(searchTerm, if (excludedNames.isEmpty()) setOf("") else excludedNames)
        )

    private fun transformIdAndNameToItem(idsAndNames: List<Array<Any>>): List<SimpleItem> =
        idsAndNames.map { SimpleItemImpl(it[0] as UUID, it[1] as String) }
}
