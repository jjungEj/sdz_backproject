package com.elice.sdz.order.service;

import com.elice.sdz.order.dto.OrderDto;
import com.elice.sdz.order.entity.Order;
import com.elice.sdz.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)//읽기전용
    public List<OrderDto> getAllOrders() { //모든 주문조회
        return orderRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long id) { //특정 주문조회
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        return toDto(order);
    }

    @Transactional
    public OrderDto createOrder(OrderDto orderDto) { //주문 생성
        Order order = toEntity(orderDto);
        Order savedOrder = orderRepository.save(order);
        return toDto(savedOrder);
    }

    @Transactional
    public OrderDto updateOrder(Long id, OrderDto orderDto) { //주문 수정
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setOrderCount(orderDto.getOrderCount());
        order.setOrderAmount(orderDto.getOrderAmount());
        //order.setOrderStatus(orderDto.getOrderStatus());
        //order.setRefundStatus(orderDto.isRefundStatus());
        Order updatedOrder = orderRepository.save(order);
        return toDto(updatedOrder);
    }

    @Transactional
    public void deleteOrder(Long id) { //주문 삭제
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        orderRepository.delete(order);
    }

    private OrderDto toDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setOrderId(order.getOrderId());
        dto.setOrderCount(order.getOrderCount());
        dto.setOrderAmount(order.getOrderAmount());
        //dto.setOrderStatus(order.getOrderStatus());
        //dto.setRegDate(order.getRegDate());
        //dto.setRefundStatus(order.isRefundStatus());
        return dto;
    }

    private Order toEntity(OrderDto dto) {
        Order order = new Order();
        order.setOrderId(dto.getOrderId());
        order.setOrderCount(dto.getOrderCount());
        order.setOrderAmount(dto.getOrderAmount());
        //order.setOrderStatus(dto.getOrderStatus());
        //order.setRefundStatus(dto.isRefundStatus());
        return order;
    }
}
