package uk.co.rafearnold.randomworkoutroutine.web.repository;

import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ItemRepository<T extends Item> extends CrudRepository<T, UUID> {

    @Query("SELECT e.id, e.name FROM #{#entityName} e")
    List<Object[]> findAllIdsAndNames();

    /**
     * Finds all {@link Item}s whose names contain the given search term, ignoring case, and
     * returns the ID and name of the entities found. A set of names can be included for the search
     * to ignore. These names to exclude must exactly match the names of the {@link Item}s that
     * should be ignored.
     *
     * @param searchTerm    The term to search for within names.
     * @param excludedNames The names of entities to be ignored.
     * @return A collection of {@link Item} projections whose names match the given filter
     * parameters.
     */
    @Query("SELECT e.id, e.name FROM #{#entityName} e WHERE (LOWER(e.name) LIKE ?#{'%' + #searchTerm.toLowerCase() + '%'} OR ?#{#searchTerm.toLowerCase()} MEMBER OF e.tags) AND e.name NOT IN :excludedNames")
    List<Object[]> findAllIdsAndNames(@Param("searchTerm") String searchTerm, @Param("excludedNames") Collection<String> excludedNames);
}
