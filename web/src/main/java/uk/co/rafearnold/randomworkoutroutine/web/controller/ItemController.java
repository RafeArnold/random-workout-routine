package uk.co.rafearnold.randomworkoutroutine.web.controller;

import lombok.RequiredArgsConstructor;
import uk.co.rafearnold.randomworkoutroutine.web.model.Filter;
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Item;
import uk.co.rafearnold.randomworkoutroutine.web.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class ItemController<T extends Item> {

    protected final ItemService<T> service;

    // TODO: 404 when item doesn't exist.
    @GetMapping("/{id}")
    public ResponseEntity<T> getById(@PathVariable UUID id) {
        return ResponseEntity.of(service.getById(id));
    }

    // TODO: Better response status when an existing item is provided without its ID.
    // TODO: Don't allow items with blank names.
    @PostMapping("/save")
    public void save(@RequestBody T option) {
        service.save(option);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        Optional<T> itemOptional = service.getById(id);
        itemOptional.ifPresent(service::delete);
        return itemOptional.map(item -> ResponseEntity.ok().build()).orElse(ResponseEntity.notFound().build());
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
