package com.elice.sdz.order.Controller;

import com.elice.sdz.order.controller.OrderController;
import com.elice.sdz.order.dto.OrderDto;
import com.elice.sdz.order.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllOrders() throws Exception {
        OrderDto order = new OrderDto();
        order.setOrderId(1L);
        order.setOrderCount(2);
        order.setOrderAmount(100.0);

        List<OrderDto> orders = Arrays.asList(order);

        Mockito.when(orderService.getAllOrders()).thenReturn(orders);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value(1L))
                .andExpect(jsonPath("$[0].orderCount").value(2));
    }

    @Test
    void createOrder() throws Exception {
        OrderDto order = new OrderDto();
        order.setOrderId(1L);
        order.setOrderCount(2);
        order.setOrderAmount(100.0);

        Mockito.when(orderService.createOrder(Mockito.any())).thenReturn(order);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1L))
                .andExpect(jsonPath("$.orderCount").value(2));
    }
}
