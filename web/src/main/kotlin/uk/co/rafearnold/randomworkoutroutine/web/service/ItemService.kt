package uk.co.rafearnold.randomworkoutroutine.web.service

import uk.co.rafearnold.randomworkoutroutine.model.SimpleItem
import uk.co.rafearnold.randomworkoutroutine.model.SimpleItemImpl
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Item
import uk.co.rafearnold.randomworkoutroutine.web.repository.ItemRepository
import java.util.*

/**
 * Abstract service for retrieving and manipulating [Item] entities.
 */
abstract class ItemService<T : Item>(protected val repository: ItemRepository<T>) {

    fun getById(id: UUID): Optional<T> = repository.findById(id)

    fun save(item: T) {
        repository.save(item)
    }

    open fun delete(item: T) {
        repository.delete(item)
    }

    /**
     * Retrieves all saved [T] items and returns them as [SimpleItem] objects.
     */
    fun getNames(): List<SimpleItem> = repository.findAllIdsAndNames()

    /**
     * Retrieves all saved [T] items that match [searchTerm], excluding any that match any items in
     * [excludedNames], and returns them as [SimpleItem] objects.
     */
    fun search(searchTerm: String, excludedNames: Set<String>): List<SimpleItem> =
        repository.findAllIdsAndNames(searchTerm, if (excludedNames.isEmpty()) setOf("") else excludedNames)

    /**
     * Transforms each object array in [idsAndNames] into a [SimpleItem]. 
     */
    private fun transformIdAndNameToItem(idsAndNames: List<Array<Any>>): List<SimpleItem> =
        idsAndNames.map { SimpleItemImpl(it[0] as UUID, it[1] as String) }
}
