package com.cinerikuy.transaction.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class TransactionProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long productId;
    private String productName;

    @JsonIgnore // to avoid infinite recursion:
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Transaction.class)
    @JoinColumn(name = "transactionId", nullable = true)
    private Transaction transactionProduct;

}
