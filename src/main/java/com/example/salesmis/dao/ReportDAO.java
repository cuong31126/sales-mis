package com.example.salesmis.dao;

import com.example.salesmis.model.dto.CustomerRevenueDTO;
import com.example.salesmis.model.dto.MonthlyRevenueDTO;
import com.example.salesmis.model.dto.ProductSalesDTO;
import com.example.salesmis.model.dto.StatusCountDTO;
import com.example.salesmis.model.entity.Customer;
import com.example.salesmis.model.entity.Product;
import com.example.salesmis.model.entity.SalesOrder;
import com.example.salesmis.model.enumtype.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ReportDAO {
    List<SalesOrder> q01_findAllOrdersWithCustomer();
    SalesOrder q02_findOrderByOrderNo(String orderNo);
    List<SalesOrder> q03_findOrdersByCustomerKeyword(String keyword);
    List<SalesOrder> q04_findOrdersBetween(LocalDate from, LocalDate to);
    List<SalesOrder> q05_findOrdersByStatus(OrderStatus status);
    List<Product> q06_findLowStockProducts(int threshold);
    List<ProductSalesDTO> q07_findTopSellingProducts();
    List<CustomerRevenueDTO> q08_findRevenueByCustomer();
    List<MonthlyRevenueDTO> q09_findRevenueByMonth();
    List<StatusCountDTO> q10_countOrdersByStatus();
    List<Customer> q11_findCustomersWithoutOrders();
    List<SalesOrder> q12_findOrdersContainingProduct(Long productId);
    BigDecimal q13_findAverageOrderValue();
    BigDecimal q14_findMaxOrderValue();
    BigDecimal q15_findMinOrderValue();
}
