package com.example.salesmis.model.dto;

import java.math.BigDecimal;

public class OrderLineInput {
    private Long productId;
    private int quantity;
    private BigDecimal unitPrice;

    public OrderLineInput() {
    }

    public OrderLineInput(Long productId, int quantity, BigDecimal unitPrice) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Long getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }

    public void setProductId(Long productId) { this.productId = productId; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
}
