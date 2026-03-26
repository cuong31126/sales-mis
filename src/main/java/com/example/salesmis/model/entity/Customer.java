package com.example.salesmis.model.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_code", nullable = false, unique = true, length = 20)
    private String customerCode;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(length = 20)
    private String phone;

    @Column(unique = true, length = 150)
    private String email;

    @Column(length = 255)
    private String address;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<SalesOrder> orders = new ArrayList<>();

    public Customer() {
    }

    public Customer(String customerCode, String fullName) {
        this.customerCode = customerCode;
        this.fullName = fullName;
    }

    public Long getId() { return id; }
    public String getCustomerCode() { return customerCode; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public Boolean getActive() { return active; }
    public List<SalesOrder> getOrders() { return orders; }

    public void setId(Long id) { this.id = id; }
    public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setAddress(String address) { this.address = address; }
    public void setActive(Boolean active) { this.active = active; }
    public void setOrders(List<SalesOrder> orders) { this.orders = orders; }

    @Override
    public String toString() {
        return customerCode + " - " + fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
