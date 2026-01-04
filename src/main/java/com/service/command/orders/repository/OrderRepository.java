package com.service.command.orders.repository;

import com.service.command.orders.models.Order;
import com.service.command.orders.models.OrderStatus;
import com.service.command.users.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findByUserUsernameAndStatus(String userUsername, OrderStatus status);

    List<Order> findByUser_Username(String userUsername);

}
