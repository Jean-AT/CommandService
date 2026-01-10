package com.service.command.orders.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.service.command.products.models.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "OrderDetails")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="orderList")
    @JsonIgnore
    private Order orderList;

    @ManyToOne
    private Product productId;

    @Column(nullable = false)
    private int quantity;

    @Column
    private String observations;

    @Column(nullable = false)
    private BigDecimal priceUnit;
}
