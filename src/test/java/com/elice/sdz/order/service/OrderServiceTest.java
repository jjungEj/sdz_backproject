package com.elice.sdz.order.service;

import com.elice.sdz.order.dto.OrderDto;
import com.elice.sdz.order.entity.Order;
import com.elice.sdz.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrderServiceTest {

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Test
    void getOrderById() {
        Order order = new Order();
        order.setOrderId(1L);
        order.setOrderCount(2);
        order.setOrderAmount(100.0);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderDto orderDto = orderService.getOrderById(1L);

        assertNotNull(orderDto);
        assertEquals(1L, orderDto.getOrderId());
        assertEquals(2, orderDto.getOrderCount());
    }

    @Test
    void createOrder() {
        Order order = new Order();
        order.setOrderId(1L);
        order.setOrderCount(2);
        order.setOrderAmount(100.0);

        when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);

        OrderDto orderDto = new OrderDto();
        orderDto.setOrderCount(2);
        orderDto.setOrderAmount(100.0);

        OrderDto createdOrder = orderService.createOrder(orderDto);

        assertNotNull(createdOrder);
        assertEquals(2, createdOrder.getOrderCount());
        assertEquals(100.0, createdOrder.getOrderAmount());
    }
}
