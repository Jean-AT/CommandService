package com.service.command.orders.service;

import com.service.command.orders.dto.OrderDTO;
import com.service.command.orders.dto.OrderDetailsDTO;
import com.service.command.orders.models.Order;
import com.service.command.orders.models.OrderDetails;
import com.service.command.orders.models.OrderStatus;
import com.service.command.orders.repository.OrderDetailsRepository;
import com.service.command.orders.repository.OrderRepository;
import com.service.command.products.models.Product;
import com.service.command.products.repository.ProductRepository;
import com.service.command.users.models.Users;
import com.service.command.users.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderRepository orderRepository;
    private final UsersRepository usersRepository;
    private final ProductRepository productRepository;


    public Order CreateOrder(OrderDTO now) {

        Order order = new Order();
        order.setTable(now.getTable());
        order.setDate(Timestamp.valueOf(LocalDateTime.now()));
        order.setStatus(OrderStatus.Pending);
        order.setTotalPrice(BigDecimal.ZERO);
        Users waiter = usersRepository.findById(now.getWaiterId())
                .orElseThrow(() -> new RuntimeException("Waiter with " + now.getWaiterId() + " is not found"));
        order.setUser(waiter);

        List<OrderDetails> itemsEntity = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderDetailsDTO itemDto : now.getItems()) {
            OrderDetails item = new OrderDetails();
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            if (product.getStock() < itemDto.getQuantity()) {
                throw new RuntimeException("Not available stock of a " + product.getName());
            }
            item.setProduct(product);
            item.setQuantity(itemDto.getQuantity());
            item.setObservations(itemDto.getObservation());
            item.setPriceUnit(product.getPrice());
            item.setOrder(order);

            BigDecimal subtotal = product.getPrice().multiply(new BigDecimal(itemDto.getQuantity()));
            totalPrice = totalPrice.add(subtotal);

            itemsEntity.add(item);
        }

        order.setItems(itemsEntity);
        order.setTotalPrice(totalPrice);

        return orderRepository.save(order);
    }

    public Order addItem(Long orderId, OrderDetailsDTO item) {

        Order aux = orderRepository.findById(orderId)
                .orElseThrow(()-> new RuntimeException("The order is not found"));

        if (aux.getStatus() == OrderStatus.Paid || aux.getStatus() == OrderStatus.Cancelled){
            throw new RuntimeException("Can't edit a cancelled order");
        }

        Product prodaux = productRepository.findById(item.getProductId())
                .orElseThrow(()-> new RuntimeException("The product is not found"));

        if (prodaux.getStock() < item.getQuantity()){
            throw new RuntimeException("insufficient stock");
        }

        OrderDetails newItem = new OrderDetails();
        newItem.setProduct(prodaux);
        newItem.setQuantity(item.getQuantity());
        newItem.setPriceUnit(prodaux.getPrice());
        newItem.setObservations(item.getObservation());
        newItem.setOrder(aux);

        aux.getItems().add(newItem);

        BigDecimal ttprice = prodaux.getPrice().multiply(new BigDecimal(item.getQuantity()));
        aux.setTotalPrice(ttprice);

        prodaux.setStock(prodaux.getStock()-item.getQuantity());
        productRepository.save(prodaux);

        return orderRepository.save(aux);
    }

    @Transactional
    public Order removeItem(Long orderId, Long itemId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new RuntimeException("The order is not found"));

        if (order.getStatus() == OrderStatus.Paid || order.getStatus() == OrderStatus.Cancelled){
            throw new RuntimeException("Can't edit a cancelled order");
        }

        OrderDetails itemToRemove = order.getItems().stream()
                .filter(item-> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(()-> new RuntimeException("The item is not found"));

        Product product = itemToRemove.getProduct();
        product.setStock(product.getStock()+itemToRemove.getQuantity());
        productRepository.save(product);

        BigDecimal ttprice = itemToRemove.getPriceUnit().multiply(new BigDecimal(itemToRemove.getQuantity()));

        order.setTotalPrice(order.getTotalPrice().subtract(ttprice));

        order.getItems().remove(itemToRemove);

        return orderRepository.save(order);
    }

    public Order changeStatus(Long orderId,OrderStatus status){

        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new RuntimeException("The order is not found"));

        if (order.getStatus() == OrderStatus.Paid || order.getStatus() == OrderStatus.Cancelled){
            throw new RuntimeException("Can't edit a cancelled order");
        }

        order.setStatus(status);

        return  orderRepository.save(order);
    }

    public List<Order> getMyActiveOrders(String username,OrderStatus status) {
        return orderRepository.findByUserUsernameAndStatus(username, status);
    }

    public List<Order> listMyOrders(String username){
        orderRepository.findByUser_Username(username);
        return null;
    }
}
