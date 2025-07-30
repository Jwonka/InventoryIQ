package com.inventory.manager.controller;

import com.inventory.manager.entities.InventoryItem;
import com.inventory.manager.entities.Product;
import com.inventory.manager.entities.ProductComponent;
import com.inventory.manager.repositories.InventoryItemRepository;
import com.inventory.manager.repositories.ProductRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/products")
public class ProductWebController {
    private final ProductRepository productRepo;
    private final InventoryItemRepository itemRepo;

    public ProductWebController(ProductRepository productRepo, InventoryItemRepository itemRepo) {
        this.productRepo = productRepo;
        this.itemRepo     = itemRepo;
    }


    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "products/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("product", new Product());
        return "products/form";
    }

    @PostMapping
    public String create(@ModelAttribute Product product) {
        productRepo.save(product);
        return "redirect:/products";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("product", p);
        return "products/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Product updated) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        p.setCode(updated.getCode());
        p.setName(updated.getName());
        productRepo.save(p);
        return "redirect:/products";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        productRepo.deleteById(id);
        return "redirect:/products";
    }

    @GetMapping("/{id}/components")
    public String components(@PathVariable Long id, Model model) {
        Product p = productRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("product", p);
        model.addAttribute("allItems", itemRepo.findAll());
        model.addAttribute("newComp", new ComponentRequest());
        return "products/components";
    }

    @PostMapping("/{id}/components")
    public String addComponent(@PathVariable Long id, @ModelAttribute ComponentRequest dto) {
        Product p = productRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        InventoryItem item = itemRepo.findById(dto.getItemId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ProductComponent comp = new ProductComponent();
        comp.setItem(item);
        comp.setRequiredQuantity(dto.getRequiredQuantity());
        p.addComponent(comp);
        productRepo.save(p);
        return "redirect:/products/" + id + "/components";
    }

    @PostMapping("/{id}/components/{compId}/delete")
    public String removeComponent(@PathVariable Long id,
                                  @PathVariable Long compId) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        p.getComponents().removeIf(c -> c.getId().equals(compId));
        productRepo.save(p);
        return "redirect:/products/" + id + "/components";
    }

    @Getter
    @Setter
    public static class ComponentRequest {
        private Long itemId;
        private Integer requiredQuantity;
    }
}