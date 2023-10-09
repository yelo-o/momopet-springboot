package com.momo.service;

import com.momo.domain.Order;
import com.momo.domain.OrderItem;
import com.momo.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public void saveOrder(Order order, OrderItem orderItem) {
        orderRepository.save(order, orderItem);
    }

    public List<Order> findMyOrders(String email) {
        return orderRepository.find(email);
    }

    public List<Order> findSittingOrders(String email) {
        return orderRepository.findSittingOrders(email);
    }

    public Order findOne(Long orderId) {
        return orderRepository.findOne(orderId);
    }
}
