package com.example.salesmis.dao.impl;

import com.example.salesmis.config.JpaUtil;
import com.example.salesmis.dao.ReportDAO;
import com.example.salesmis.model.dto.CustomerRevenueDTO;
import com.example.salesmis.model.dto.MonthlyRevenueDTO;
import com.example.salesmis.model.dto.ProductSalesDTO;
import com.example.salesmis.model.dto.StatusCountDTO;
import com.example.salesmis.model.entity.Customer;
import com.example.salesmis.model.entity.Product;
import com.example.salesmis.model.entity.SalesOrder;
import com.example.salesmis.model.enumtype.OrderStatus;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ReportDAOImpl implements ReportDAO {

    @Override
    public List<SalesOrder> q01_findAllOrdersWithCustomer() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT o
                    FROM SalesOrder o
                    JOIN FETCH o.customer
                    ORDER BY o.orderDate DESC, o.id DESC
                    """, SalesOrder.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public SalesOrder q02_findOrderByOrderNo(String orderNo) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT o
                    FROM SalesOrder o
                    JOIN FETCH o.customer
                    WHERE o.orderNo = :orderNo
                    """, SalesOrder.class)
                    .setParameter("orderNo", orderNo)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public List<SalesOrder> q03_findOrdersByCustomerKeyword(String keyword) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT o
                    FROM SalesOrder o
                    JOIN FETCH o.customer c
                    WHERE LOWER(c.fullName) LIKE LOWER(:kw)
                    ORDER BY o.orderDate DESC
                    """, SalesOrder.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<SalesOrder> q04_findOrdersBetween(LocalDate from, LocalDate to) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT o
                    FROM SalesOrder o
                    JOIN FETCH o.customer
                    WHERE o.orderDate BETWEEN :fromDate AND :toDate
                    ORDER BY o.orderDate
                    """, SalesOrder.class)
                    .setParameter("fromDate", from)
                    .setParameter("toDate", to)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<SalesOrder> q05_findOrdersByStatus(OrderStatus status) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT o
                    FROM SalesOrder o
                    JOIN FETCH o.customer
                    WHERE o.status = :status
                    ORDER BY o.orderDate DESC
                    """, SalesOrder.class)
                    .setParameter("status", status)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> q06_findLowStockProducts(int threshold) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT p
                    FROM Product p
                    WHERE p.stockQty <= :threshold
                    ORDER BY p.stockQty ASC, p.productName ASC
                    """, Product.class)
                    .setParameter("threshold", threshold)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<ProductSalesDTO> q07_findTopSellingProducts() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT new com.example.salesmis.model.dto.ProductSalesDTO(
                        p.sku,
                        p.productName,
                        SUM(d.quantity),
                        SUM(d.lineTotal)
                    )
                    FROM OrderDetail d
                    JOIN d.product p
                    GROUP BY p.sku, p.productName
                    ORDER BY SUM(d.quantity) DESC, SUM(d.lineTotal) DESC
                    """, ProductSalesDTO.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<CustomerRevenueDTO> q08_findRevenueByCustomer() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT new com.example.salesmis.model.dto.CustomerRevenueDTO(
                        c.customerCode,
                        c.fullName,
                        COALESCE(SUM(o.totalAmount), 0)
                    )
                    FROM Customer c
                    LEFT JOIN c.orders o
                    GROUP BY c.customerCode, c.fullName
                    ORDER BY COALESCE(SUM(o.totalAmount), 0) DESC
                    """, CustomerRevenueDTO.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<MonthlyRevenueDTO> q09_findRevenueByMonth() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT new com.example.salesmis.model.dto.MonthlyRevenueDTO(
                        year(o.orderDate),
                        month(o.orderDate),
                        SUM(o.totalAmount)
                    )
                    FROM SalesOrder o
                    GROUP BY year(o.orderDate), month(o.orderDate)
                    ORDER BY year(o.orderDate), month(o.orderDate)
                    """, MonthlyRevenueDTO.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<StatusCountDTO> q10_countOrdersByStatus() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT new com.example.salesmis.model.dto.StatusCountDTO(
                        o.status,
                        COUNT(o)
                    )
                    FROM SalesOrder o
                    GROUP BY o.status
                    ORDER BY o.status
                    """, StatusCountDTO.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Customer> q11_findCustomersWithoutOrders() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT c
                    FROM Customer c
                    WHERE c.orders IS EMPTY
                    ORDER BY c.customerCode
                    """, Customer.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<SalesOrder> q12_findOrdersContainingProduct(Long productId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT DISTINCT o
                    FROM SalesOrder o
                    JOIN FETCH o.customer
                    JOIN o.orderDetails d
                    WHERE d.product.id = :productId
                    ORDER BY o.orderDate DESC
                    """, SalesOrder.class)
                    .setParameter("productId", productId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public BigDecimal q13_findAverageOrderValue() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Double avg = em.createQuery("""
                    SELECT AVG(o.totalAmount)
                    FROM SalesOrder o
                    """, Double.class).getSingleResult();
            return avg == null ? BigDecimal.ZERO : BigDecimal.valueOf(avg);
        } finally {
            em.close();
        }
    }

    @Override
    public BigDecimal q14_findMaxOrderValue() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            BigDecimal max = em.createQuery("""
                    SELECT MAX(o.totalAmount)
                    FROM SalesOrder o
                    """, BigDecimal.class).getSingleResult();
            return max == null ? BigDecimal.ZERO : max;
        } finally {
            em.close();
        }
    }

    @Override
    public BigDecimal q15_findMinOrderValue() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            BigDecimal min = em.createQuery("""
                    SELECT MIN(o.totalAmount)
                    FROM SalesOrder o
                    """, BigDecimal.class).getSingleResult();
            return min == null ? BigDecimal.ZERO : min;
        } finally {
            em.close();
        }
    }
}
