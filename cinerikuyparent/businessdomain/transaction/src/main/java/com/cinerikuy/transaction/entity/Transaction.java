package com.cinerikuy.transaction.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String code;
    @Embedded
    private CinemaData cinema;
    //private CustomerPojo customer;
    //private MoviePojo movie;
    @ElementCollection
    private List<ProductData> products;
}
