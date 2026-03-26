package com.example.salesmis.view ;
import javax.swing.* ;
import java.awt.* ;

public class MainFrame extends JFrame {
    public MainFrame(OrderManagementPanel panel ) {
        setTitle("MIS - Sales Management le cuong ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300 , 760) ;
        setLocationRelativeTo(null);
        setLayout(new BorderLayout()) ;
        add(panel , BorderLayout.CENTER) ;
    }
}
