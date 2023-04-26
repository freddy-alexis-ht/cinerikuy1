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
    // Cinema data
    //private String cinemaCode;
    //private String cinemaName;
    //private CustomerPojo customer;
    //private CinemaData cinema;
    //private MoviePojo movie;
    //private List<ProductData> products;
}
