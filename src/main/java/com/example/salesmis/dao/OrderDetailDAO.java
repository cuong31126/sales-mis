package com.example.salesmis.dao;

import com.example.salesmis.model.entity.OrderDetail;
import java.util.List;

public interface OrderDetailDAO {
    List<OrderDetail> findByOrderId(Long orderId);
}
