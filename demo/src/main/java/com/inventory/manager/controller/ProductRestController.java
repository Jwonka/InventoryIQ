package com.inventory.manager.controller;

import com.inventory.manager.entities.InventoryItem;
import com.inventory.manager.entities.Product;
import com.inventory.manager.entities.ProductComponent;
import com.inventory.manager.repositories.InventoryItemRepository;
import com.inventory.manager.repositories.ProductComponentRepository;
import com.inventory.manager.repositories.ProductRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {
    private final ProductRepository productRepo;
    private final InventoryItemRepository itemRepo;
    private final ProductComponentRepository compRepo;

    public ProductRestController(ProductRepository pr, InventoryItemRepository ir, ProductComponentRepository cr) {
        this.productRepo = pr;
        this.itemRepo = ir;
        this.compRepo = cr;
    }

    @GetMapping
    public List<Product> listProducts() { return productRepo.findAll(); }

    @PostMapping
    public Product createProduct(@RequestBody @Valid Product product) { return productRepo.save(product); }

    @PutMapping("/{prodId}")
    public Product updateProduct(@PathVariable Long prodId, @RequestBody @Valid Product updated) {
        Product product = productRepo.findById(prodId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        product.setName(updated.getName());
        product.setCode(updated.getCode());
        product.getComponents().clear();
        updated.getComponents().forEach(product::addComponent);
        return productRepo.save(product);
    }

    @DeleteMapping("/{prodId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long prodId) { productRepo.deleteById(prodId); }

    @GetMapping("/{prodId}/components")
    public List<ProductComponent> listComponents(@PathVariable Long prodId) {
        Product product = productRepo.findById(prodId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return product.getComponents();
    }

    @PostMapping("/{prodId}/components")
    public ProductComponent addComponent(@PathVariable Long prodId, @RequestBody ComponentRequest dto) {
        Product product = productRepo.findById(prodId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        InventoryItem item = itemRepo.findById(dto.getItemId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        ProductComponent comp = new ProductComponent();
        comp.setItem(item);
        comp.setRequiredQuantity(dto.getRequiredQuantity());
        product.addComponent(comp);

        productRepo.save(product);
        return comp;
    }

    @DeleteMapping("/{prodId}/components/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeComponent(@PathVariable Long prodId, @PathVariable Long compId) {
        Product product = productRepo.findById(prodId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ProductComponent comp = compRepo.findById(compId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        product.removeComponent(comp);
        productRepo.save(product);
    }

    @Getter
    @Setter
    public static class ComponentRequest {
        @NotNull
        Long itemId;
        @NotNull @Min(1) Integer requiredQuantity;
    }
}
