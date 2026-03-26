package com.example.salesmis.dao.impl;

import com.example.salesmis.config.JpaUtil;
import com.example.salesmis.dao.SalesOrderDAO;
import com.example.salesmis.model.entity.SalesOrder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;

public class SalesOrderDAOImpl implements SalesOrderDAO {
    @Override
    public List<SalesOrder> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT DISTINCT o
                    FROM SalesOrder o
                    LEFT JOIN FETCH o.customer
                    LEFT JOIN FETCH o.orderDetails d
                    LEFT JOIN FETCH d.product
                    ORDER BY o.id DESC
                    """, SalesOrder.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<SalesOrder> findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<SalesOrder> list = em.createQuery("""
                    SELECT DISTINCT o
                    FROM SalesOrder o
                    LEFT JOIN FETCH o.customer
                    LEFT JOIN FETCH o.orderDetails d
                    LEFT JOIN FETCH d.product
                    WHERE o.id = :id
                    """, SalesOrder.class)
                    .setParameter("id", id)
                    .getResultList();
            return list.stream().findFirst();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<SalesOrder> findByOrderNo(String orderNo) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<SalesOrder> list = em.createQuery("""
                    SELECT o FROM SalesOrder o
                    WHERE LOWER(o.orderNo) = LOWER(:orderNo)
                    """, SalesOrder.class)
                    .setParameter("orderNo", orderNo)
                    .getResultList();
            return list.stream().findFirst();
        } finally {
            em.close();
        }
    }

    @Override
    public List<SalesOrder> searchByKeyword(String keyword) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT DISTINCT o
                    FROM SalesOrder o
                    JOIN FETCH o.customer c
                    LEFT JOIN FETCH o.orderDetails d
                    LEFT JOIN FETCH d.product
                    WHERE LOWER(o.orderNo) LIKE LOWER(:kw)
                       OR LOWER(c.fullName) LIKE LOWER(:kw)
                    ORDER BY o.id DESC
                    """, SalesOrder.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public SalesOrder save(SalesOrder order) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(order);
            tx.commit();
            return order;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public SalesOrder update(SalesOrder order) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            SalesOrder merged = em.merge(order);
            tx.commit();
            return merged;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            SalesOrder order = em.find(SalesOrder.class, id);
            if (order != null) em.remove(order);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
