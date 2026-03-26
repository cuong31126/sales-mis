package com.example.salesmis.model.dto;

import java.math.BigDecimal;

public record ProductSalesDTO(String sku, String productName, Long totalQty, BigDecimal revenue) {}
