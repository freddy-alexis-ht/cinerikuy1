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
    private String dni;
    // private String cinemaCode;
    private long movieId;
    private String movieName;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "transactionProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionProduct> products;
}
