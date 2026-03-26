package com.example.salesmis;

import com.example.salesmis.controller.OrderController;
import com.example.salesmis.dao.CustomerDAO;
import com.example.salesmis.dao.ProductDAO;
import com.example.salesmis.dao.SalesOrderDAO;
import com.example.salesmis.dao.impl.CustomerDAOImpl;
import com.example.salesmis.dao.impl.ProductDAOImpl;
import com.example.salesmis.dao.impl.SalesOrderDAOImpl;
import com.example.salesmis.service.LookupService;
import com.example.salesmis.service.OrderService;
import com.example.salesmis.service.impl.LookupServiceImpl;
import com.example.salesmis.service.impl.OrderServiceImpl;
import com.example.salesmis.view.MainFrame;
import com.example.salesmis.view.OrderManagementPanel;

import javax.swing.*;

public class AppLauncher {
    public static void main(String[] args) {
        CustomerDAO customerDAO = new CustomerDAOImpl();
        ProductDAO productDAO = new ProductDAOImpl();
        SalesOrderDAO salesOrderDAO = new SalesOrderDAOImpl();

        LookupService lookupService = new LookupServiceImpl(customerDAO, productDAO);
        OrderService orderService = new OrderServiceImpl(salesOrderDAO, customerDAO, productDAO);
        OrderController orderController = new OrderController(orderService, lookupService);

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(new OrderManagementPanel(orderController));
            frame.setVisible(true);
        });
    }
}
