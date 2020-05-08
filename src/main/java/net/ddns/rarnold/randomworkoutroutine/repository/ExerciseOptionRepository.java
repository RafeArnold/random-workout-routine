package net.ddns.rarnold.randomworkoutroutine.repository;

import net.ddns.rarnold.randomworkoutroutine.model.entity.ExerciseOption;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface ExerciseOptionRepository extends CrudRepository<ExerciseOption, UUID> {
    @Query("SELECT e.id, e.name FROM ExerciseOption e")
    List<Object[]> findAllIdsAndNames();

    /**
     * Finds all {@link ExerciseOption}s whose names contain the given search term, ignoring case,
     * and returns the ID and name of the entities found. A set of names can be included for the
     * search to ignore. These names to exclude must exactly match the names of the
     * {@link ExerciseOption}s that should be ignored.
     *
     * @param searchTerm    The term to search for within names.
     * @param excludedNames The names of entities to be ignored.
     * @return A collection of {@link ExerciseOption} projections whose names match the given filter parameters.
     */
    @Query("SELECT e.id, e.name FROM ExerciseOption e WHERE LOWER(e.name) LIKE :#{'%' + #searchTerm.toLowerCase() + '%'} AND e.name NOT IN :excludedNames")
    List<Object[]> findAllIdsAndNamesWithNameContainingIgnoreCase(@Param("searchTerm") String searchTerm, @Param("excludedNames") Collection<String> excludedNames);
}
