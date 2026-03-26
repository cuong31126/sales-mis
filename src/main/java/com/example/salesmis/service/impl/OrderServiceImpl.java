package com.example.salesmis.service.impl;

import com.example.salesmis.dao.CustomerDAO;
import com.example.salesmis.dao.ProductDAO;
import com.example.salesmis.dao.SalesOrderDAO;
import com.example.salesmis.model.dto.OrderLineInput;
import com.example.salesmis.model.entity.Customer;
import com.example.salesmis.model.entity.OrderDetail;
import com.example.salesmis.model.entity.Product;
import com.example.salesmis.model.entity.SalesOrder;
import com.example.salesmis.model.enumtype.OrderStatus;
import com.example.salesmis.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OrderServiceImpl implements OrderService {
    private final SalesOrderDAO salesOrderDAO;
    private final CustomerDAO customerDAO;
    private final ProductDAO productDAO;

    public OrderServiceImpl(SalesOrderDAO salesOrderDAO, CustomerDAO customerDAO, ProductDAO productDAO) {
        this.salesOrderDAO = salesOrderDAO;
        this.customerDAO = customerDAO;
        this.productDAO = productDAO;
    }

    @Override
    public List<SalesOrder> getAllOrders() {
        return salesOrderDAO.findAll();
    }

    @Override
    public List<SalesOrder> searchOrders(String keyword) {
        if (keyword == null || keyword.isBlank()) return salesOrderDAO.findAll();
        return salesOrderDAO.searchByKeyword(keyword.trim());
    }

    @Override
    public SalesOrder getOrderById(Long id) {
        return salesOrderDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng id = " + id));
    }

    @Override
    public SalesOrder createOrder(String orderNo, LocalDate orderDate, Long customerId,
                                  OrderStatus status, String note, List<OrderLineInput> lines) {
        validate(orderNo, orderDate, customerId, lines);

        if (salesOrderDAO.findByOrderNo(orderNo.trim()).isPresent()) {
            throw new IllegalArgumentException("Mã đơn hàng đã tồn tại.");
        }

        Customer customer = customerDAO.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng."));

        SalesOrder order = new SalesOrder();
        order.setOrderNo(orderNo.trim());
        order.setOrderDate(orderDate);
        order.setCustomer(customer);
        order.setStatus(status);
        order.setNote(note);

        BigDecimal total = BigDecimal.ZERO;
        for (OrderLineInput line : lines) {
            Product product = productDAO.findById(line.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm id = " + line.getProductId()));

            OrderDetail detail = new OrderDetail();
            detail.setProduct(product);
            detail.setQuantity(line.getQuantity());
            detail.setUnitPrice(line.getUnitPrice());
            detail.setLineTotal(line.getUnitPrice().multiply(BigDecimal.valueOf(line.getQuantity())));

            total = total.add(detail.getLineTotal());
            order.addDetail(detail);
        }

        order.setTotalAmount(total);
        return salesOrderDAO.save(order);
    }

    @Override
    public SalesOrder updateOrder(Long id, String orderNo, LocalDate orderDate, Long customerId,
                                  OrderStatus status, String note, List<OrderLineInput> lines) {
        validate(orderNo, orderDate, customerId, lines);

        SalesOrder existing = getOrderById(id);
        Customer customer = customerDAO.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng."));

        salesOrderDAO.findByOrderNo(orderNo.trim()).ifPresent(found -> {
            if (!found.getId().equals(id)) {
                throw new IllegalArgumentException("Mã đơn hàng đã được dùng cho đơn khác.");
            }
        });

        existing.setOrderNo(orderNo.trim());
        existing.setOrderDate(orderDate);
        existing.setCustomer(customer);
        existing.setStatus(status);
        existing.setNote(note);

        existing.clearDetails();

        BigDecimal total = BigDecimal.ZERO;
        for (OrderLineInput line : lines) {
            Product product = productDAO.findById(line.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm id = " + line.getProductId()));

            OrderDetail detail = new OrderDetail();
            detail.setProduct(product);
            detail.setQuantity(line.getQuantity());
            detail.setUnitPrice(line.getUnitPrice());
            detail.setLineTotal(line.getUnitPrice().multiply(BigDecimal.valueOf(line.getQuantity())));

            total = total.add(detail.getLineTotal());
            existing.addDetail(detail);
        }

        existing.setTotalAmount(total);
        return salesOrderDAO.update(existing);
    }

    @Override
    public void deleteOrder(Long id) {
        getOrderById(id);
        salesOrderDAO.deleteById(id);
    }

    private void validate(String orderNo, LocalDate orderDate, Long customerId, List<OrderLineInput> lines) {
        if (orderNo == null || orderNo.isBlank()) throw new IllegalArgumentException("Order No không được trống.");
        if (orderDate == null) throw new IllegalArgumentException("Ngày đơn hàng không được trống.");
        if (customerId == null) throw new IllegalArgumentException("Khách hàng không được trống.");
        if (lines == null || lines.isEmpty()) throw new IllegalArgumentException("Đơn hàng phải có ít nhất 1 dòng chi tiết.");
        for (OrderLineInput line : lines) {
            if (line.getProductId() == null) throw new IllegalArgumentException("Thiếu sản phẩm.");
            if (line.getQuantity() <= 0) throw new IllegalArgumentException("Số lượng phải > 0.");
            if (line.getUnitPrice() == null || line.getUnitPrice().signum() < 0) {
                throw new IllegalArgumentException("Đơn giá không hợp lệ.");
            }
        }
    }
}
