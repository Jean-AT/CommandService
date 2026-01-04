package com.service.command.orders.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderDetailsDTO {
    private Long productId;
    private int quantity;
    private String observation;
}
