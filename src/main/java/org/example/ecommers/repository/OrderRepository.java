package org.example.ecommers.repository;

import org.example.ecommers.entity.Order;
import org.example.ecommers.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByUserId(Long userId);

    List<Order> findByUserIdAndStatusNot(Long userId, OrderStatus status);


    List<Order> findByStatusNot(OrderStatus status);
}
