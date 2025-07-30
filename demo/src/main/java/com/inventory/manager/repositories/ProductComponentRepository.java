package com.inventory.manager.repositories;

import com.inventory.manager.entities.ProductComponent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductComponentRepository extends JpaRepository<ProductComponent, Long> {}
