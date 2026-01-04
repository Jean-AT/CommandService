package com.service.command.orders.controller;

import com.service.command.orders.dto.OrderDTO;
import com.service.command.orders.dto.OrderDetailsDTO;
import com.service.command.orders.models.Order;
import com.service.command.orders.models.OrderStatus;
import com.service.command.orders.service.OrderService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@NoArgsConstructor
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> CreateOrder(@RequestBody OrderDTO  orderDTO){
        orderService.CreateOrder(orderDTO);
        return ResponseEntity.ok(orderDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> addItemToTheOrder(@PathVariable Long id, @RequestBody OrderDetailsDTO items){
        orderService.addItem(id,items);
        return ResponseEntity.ok(items);
    }

    @DeleteMapping("/order/delete/{id}")
    public ResponseEntity<?> removeItem(@PathVariable Long id,@RequestBody Long itemId){
        return ResponseEntity.ok(orderService.removeItem(id,itemId));
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> changeStatusOrder(@PathVariable Long id,@RequestBody OrderStatus status){
        return ResponseEntity.ok(orderService.changeStatus(id,status));
    }

    @GetMapping("/list/{user}")
    public List<Order> listMyOrders(@PathVariable String user){
        try {
            return orderService.listMyOrders(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/list/{user}/{status}")
    public List<Order> listMyOrders(@PathVariable String user,@PathVariable OrderStatus status){
        try {
            return orderService.getMyActiveOrders(user,status);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}