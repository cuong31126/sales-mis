package com.example.salesmis.service;

import com.example.salesmis.model.dto.OrderLineInput;
import com.example.salesmis.model.entity.SalesOrder;
import com.example.salesmis.model.enumtype.OrderStatus;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    List<SalesOrder> getAllOrders();
    List<SalesOrder> searchOrders(String keyword);
    SalesOrder getOrderById(Long id);

    SalesOrder createOrder(String orderNo,
                           LocalDate orderDate,
                           Long customerId,
                           OrderStatus status,
                           String note,
                           List<OrderLineInput> lines);

    SalesOrder updateOrder(Long id,
                           String orderNo,
                           LocalDate orderDate,
                           Long customerId,
                           OrderStatus status,
                           String note,
                           List<OrderLineInput> lines);

    void deleteOrder(Long id);
}
