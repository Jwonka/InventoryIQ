package com.inventory.manager.controller;

import com.inventory.manager.entities.InventoryItem;
import com.inventory.manager.repositories.InventoryItemRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemRestController {
    private final InventoryItemRepository repo;
    public ItemRestController(InventoryItemRepository repo) { this.repo = repo; }

    @GetMapping
    public List<InventoryItem> list() { return repo.findAll(); }

    @PostMapping
    public InventoryItem create(@RequestBody InventoryItem item) { return repo.save(item); }

    @PutMapping("/{id}")
    public InventoryItem updateItem(@PathVariable Long id, @RequestBody @Valid InventoryItem updated) {

        InventoryItem existing = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        existing.setName(updated.getName());
        existing.setSku(updated.getSku());
        existing.setQuantity(updated.getQuantity());
        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable Long id) { repo.deleteById(id); }
}
