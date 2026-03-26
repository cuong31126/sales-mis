package com.example.salesmis.model.dto;
import com.example.salesmis.model.enumtype.OrderStatus;

public record StatusCountDTO (OrderStatus status, Long totalOrders) {}
