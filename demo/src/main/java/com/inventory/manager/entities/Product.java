package com.inventory.manager.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ProductComponent> components = new ArrayList<>();

    public void addComponent(ProductComponent comp) {
        components.add(comp);
        comp.setProduct(this);
    }

    public void removeComponent(ProductComponent comp) {
        components.remove(comp);
        comp.setProduct(null);
    }
}

