package com.service.command.orders.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class OrderDTO {
    private int table;
    private Long waiterId;
    private List<OrderDetailsDTO> items;
}
