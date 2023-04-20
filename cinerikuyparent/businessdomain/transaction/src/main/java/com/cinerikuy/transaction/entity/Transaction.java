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
    //private CustomerPojo customer;
    //private CinemaPojo cinema;
    private MoviePojo movie;
    private List<ProductPojo> products;
}
