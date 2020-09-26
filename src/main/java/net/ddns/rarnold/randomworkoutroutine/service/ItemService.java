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

    public T getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No item with the ID '" + id + "' exists"));
    }

    public void save(T item) {
        repository.save(item);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public List<T> getNames() {
        return transformIdAndNameToItem(repository.findAllIdsAndNames());
    }

    public List<T> searchNames(Filter filter) {
        String searchTerm = "";
        Set<String> excludedTerms = Collections.singleton("");
        if (filter != null) {
            if (filter.getSearchTerm() != null) {
                searchTerm = filter.getSearchTerm();
            }
            if (filter.getExcludedTerms() != null && !filter.getExcludedTerms().isEmpty()) {
                excludedTerms = filter.getExcludedTerms();
            }
        }
        return transformIdAndNameToItem(repository.findAllIdsAndNamesWithNameContainingIgnoreCase(searchTerm, excludedTerms));
    }

    protected List<T> transformIdAndNameToItem(List<Object[]> idsAndNames) {
        return idsAndNames.stream()
                .map(objects -> createItem((UUID) objects[0], (String) objects[1]))
                .collect(Collectors.toList());
    }

    protected abstract T createItem(UUID id, String name);
}
