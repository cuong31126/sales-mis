package com.example.salesmis.dao.impl;

import com.example.salesmis.config.JpaUtil;
import com.example.salesmis.dao.CustomerDAO;
import com.example.salesmis.model.entity.Customer;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public List<Customer> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Customer c ORDER BY c.customerCode", Customer.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Customer> findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(Customer.class, id));
        } finally {
            em.close();
        }
    }
}
