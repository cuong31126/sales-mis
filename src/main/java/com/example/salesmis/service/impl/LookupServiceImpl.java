package com.example.salesmis.service.impl;

import com.example.salesmis.dao.CustomerDAO;
import com.example.salesmis.dao.ProductDAO;
import com.example.salesmis.model.entity.Customer;
import com.example.salesmis.model.entity.Product;
import com.example.salesmis.service.LookupService;

import java.util.List;

public class LookupServiceImpl implements LookupService {
    private final CustomerDAO customerDAO;
    private final ProductDAO productDAO;

    public LookupServiceImpl(CustomerDAO customerDAO, ProductDAO productDAO) {
        this.customerDAO = customerDAO;
        this.productDAO = productDAO;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerDAO.findAll();
    }

    @Override
    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }
}
