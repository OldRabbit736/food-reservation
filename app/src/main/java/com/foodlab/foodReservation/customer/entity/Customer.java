package com.foodlab.foodReservation.customer.entity;


import com.foodlab.foodReservation.order.entity.Orders;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    @OneToMany(mappedBy = "customer")
    private List<Orders> orderList = new ArrayList<>();

    public Customer(String email) {
        this.email = email;
    }
}

