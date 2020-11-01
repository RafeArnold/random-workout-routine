package uk.co.rafearnold.randomworkoutroutine.web.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import uk.co.rafearnold.randomworkoutroutine.model.SimpleItem
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Item
import java.util.*

interface ItemRepository<T : Item> : CrudRepository<T, UUID> {

    @Query(
        "SELECT NEW uk.co.rafearnold.randomworkoutroutine.model.SimpleItemImpl(e.id, e.name)" +
                " FROM #{#entityName} e"
    )
    fun findAllIdsAndNames(): List<SimpleItem>

    /**
     * Finds all [T] items whose names contain the given search term, ignoring case, and returns
     * them as [SimpleItem]s. A set of names can be included for the search to ignore. These names
     * to exclude must exactly match the names of the [T] items that should be ignored.
     *
     * @param searchTerm The term to search for within names.
     * @param excludedNames The names of entities to be ignored.
     * @return A collection of [SimpleItem]s whose names match the given filter parameters.
     */
    @Query(
        "SELECT NEW uk.co.rafearnold.randomworkoutroutine.model.SimpleItemImpl(e.id, e.name)" +
                " FROM #{#entityName} e" +
                " WHERE (LOWER(e.name) LIKE ?#{'%' + #searchTerm.toLowerCase() + '%'} OR ?#{#searchTerm.toLowerCase()} MEMBER OF e.tags)" +
                " AND e.name NOT IN :excludedNames"
    )
    fun findAllIdsAndNames(
        @Param("searchTerm") searchTerm: String,
        @Param("excludedNames") excludedNames: Collection<String>
    ): List<SimpleItem>
}
