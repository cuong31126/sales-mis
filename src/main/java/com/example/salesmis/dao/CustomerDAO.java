package com.example.salesmis.dao;

import com.example.salesmis.model.entity.Customer;
import java.util.List;
import java.util.Optional;

public interface CustomerDAO {
    List<Customer> findAll();
    Optional<Customer> findById(Long id);
}
