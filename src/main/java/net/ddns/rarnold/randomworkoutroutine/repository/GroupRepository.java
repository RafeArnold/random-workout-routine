package net.ddns.rarnold.randomworkoutroutine.repository;

import net.ddns.rarnold.randomworkoutroutine.model.entity.ExerciseOption;
import net.ddns.rarnold.randomworkoutroutine.model.entity.Group;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface GroupRepository extends CrudRepository<Group, UUID> {
    @Query("SELECT g.id, g.name FROM grouping g")
    List<Object[]> findAllIdsAndNames();

    /**
     * Finds all {@link Group}s whose names contain the given search term, ignoring case,
     * and returns the ID and name of the entities found. A set of names can be included for the
     * search to ignore. These names to exclude must exactly match the names of the
     * {@link Group}s that should be ignored.
     *
     * @param searchTerm    The term to search for within names.
     * @param excludedNames The names of entities to be ignored.
     * @return A collection of {@link Group} projections whose names match the given filter parameters.
     */
    @Query("SELECT g.id, g.name FROM grouping g WHERE LOWER(g.name) LIKE :#{'%' + #searchTerm.toLowerCase() + '%'} AND g.name NOT IN :excludedNames")
    List<Object[]> findAllIdsAndNamesWithNameContainingIgnoreCase(@Param("searchTerm") String searchTerm, @Param("excludedNames") Collection<String> excludedNames);

    List<Group> findAllByExerciseOptionsContaining(ExerciseOption exercise);
}
