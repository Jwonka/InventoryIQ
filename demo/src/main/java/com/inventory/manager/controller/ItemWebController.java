package com.inventory.manager.controller;

import com.inventory.manager.entities.InventoryItem;
import com.inventory.manager.repositories.InventoryItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/items")
public class ItemWebController {
    private final InventoryItemRepository repo;
    public ItemWebController(InventoryItemRepository repo) { this.repo = repo; }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", repo.findAll());
        return "items/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("item", new InventoryItem());
        return "items/form";
    }

    @PostMapping
    public String create(@ModelAttribute InventoryItem item) {
        repo.save(item);
        return "redirect:/items";
    }
}
