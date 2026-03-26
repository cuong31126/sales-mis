package com.example.salesmis.dao.impl;

import com.example.salesmis.config.JpaUtil;
import com.example.salesmis.dao.OrderDetailDAO;
import com.example.salesmis.model.entity.OrderDetail;
import jakarta.persistence.EntityManager;

import java.util.List;

public class OrderDetailDAOImpl implements OrderDetailDAO {
    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT d
                    FROM OrderDetail d
                    JOIN FETCH d.product
                    WHERE d.salesOrder.id = :orderId
                    ORDER BY d.id
                    """, OrderDetail.class)
                    .setParameter("orderId", orderId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
