package com.service.command.products.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String name;
    @Column(nullable = false)
    private BigDecimal price;
    @Column
    private int stock;
    @Column
    private boolean status;

    @Enumerated(EnumType.STRING)
    private ProductsCategory category;

}
