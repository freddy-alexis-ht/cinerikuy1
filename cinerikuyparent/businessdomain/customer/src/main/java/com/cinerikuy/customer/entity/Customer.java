package com.cinerikuy.customer.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String dni;
    private String username;
    private String firstName;
    private String lastName;
    @Transient
    private List<?> transactions;
}
