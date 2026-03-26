package com.example.salesmis.dao;

import com.example.salesmis.model.entity.SalesOrder;

import java.util.List;
import java.util.Optional;

public interface SalesOrderDAO {
    List<SalesOrder> findAll();
    Optional<SalesOrder> findById(Long id);
    Optional<SalesOrder> findByOrderNo(String orderNo);
    List<SalesOrder> searchByKeyword(String keyword);
    SalesOrder save(SalesOrder order);
    SalesOrder update(SalesOrder order);
    void deleteById(Long id);
}
