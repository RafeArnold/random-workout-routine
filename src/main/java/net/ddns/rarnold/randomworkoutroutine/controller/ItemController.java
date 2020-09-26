package net.ddns.rarnold.randomworkoutroutine.controller;

import lombok.RequiredArgsConstructor;
import net.ddns.rarnold.randomworkoutroutine.model.Filter;
import net.ddns.rarnold.randomworkoutroutine.model.entity.Item;
import net.ddns.rarnold.randomworkoutroutine.service.ItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class ItemController<T extends Item> {

    protected final ItemService<T> service;

    @GetMapping("/{id}")
    public T getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping("/save")
    public void save(@RequestBody T option) {
        service.save(option);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @GetMapping("/names")
    public List<T> getNames() {
        return service.getNames();
    }

    @PostMapping("/search")
    public List<T> searchNames(@RequestBody(required = false) Filter filter) {
        return service.search(filter);
    }
}
