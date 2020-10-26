package net.ddns.rarnold.randomworkoutroutine.service;

import lombok.RequiredArgsConstructor;
import net.ddns.rarnold.randomworkoutroutine.model.Filter;
import net.ddns.rarnold.randomworkoutroutine.model.entity.Item;
import net.ddns.rarnold.randomworkoutroutine.repository.ItemRepository;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class ItemService<T extends Item> {

    protected final ItemRepository<T> repository;

    public Optional<T> getById(UUID id) {
        return repository.findById(id);
    }

    // TODO: Don't allow saving of routines and groups with no children. This is a problem when the
    //  user deletes a group or exercise that is the only child of a routine or group. Maybe just
    //  stop the user starting sessions if the routine or a group in the routine has no children.
    public void save(T item) {
        repository.save(item);
    }

    public void delete(T item) {
        repository.delete(item);
    }

    public List<T> getNames() {
        return transformIdAndNameToItem(repository.findAllIdsAndNames());
    }

    public List<T> search(Filter filter) {
        String searchTerm = "";
        Set<String> excludedNames = Set.of("");
        if (filter != null) {
            if (filter.getSearchTerm() != null) {
                searchTerm = filter.getSearchTerm();
            }
            if (filter.getExcludedTerms() != null && !filter.getExcludedTerms().isEmpty()) {
                excludedNames = filter.getExcludedTerms();
            }
        }
        return transformIdAndNameToItem(repository.findAllIdsAndNames(searchTerm, excludedNames));
    }

    protected List<T> transformIdAndNameToItem(List<Object[]> idsAndNames) {
        return idsAndNames.stream()
                .map(objects -> createItem((UUID) objects[0], (String) objects[1]))
                .collect(Collectors.toList());
    }

    protected abstract T createItem(UUID id, String name);
}
