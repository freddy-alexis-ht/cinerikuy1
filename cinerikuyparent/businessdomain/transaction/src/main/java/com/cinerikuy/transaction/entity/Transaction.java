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
    private String status;
    @Embedded
    private CustomerData customerData;
    @Embedded
    private CinemaData cinemaData;
    @Embedded
    private MovieData movieData;
    @ElementCollection
    private List<ProductData> productDataList;
}
