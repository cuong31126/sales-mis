package com.example.salesmis.service;

import com.example.salesmis.model.entity.Customer;
import com.example.salesmis.model.entity.Product;

import java.util.List;

public interface LookupService {
    List<Customer> getAllCustomers();
    List<Product> getAllProducts();
}
