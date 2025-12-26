package com.service.command.products.repository;

import com.service.command.products.models.Product;
import com.service.command.products.models.ProductsCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findByName(String name);

    List<Product> findByCategoryAndStatus(ProductsCategory category, boolean status);
    List<Product> findByStatus(boolean status);

}
