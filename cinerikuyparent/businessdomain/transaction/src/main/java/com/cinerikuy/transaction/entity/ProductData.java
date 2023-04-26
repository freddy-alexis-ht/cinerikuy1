package com.cinerikuy.transaction.entity;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class ProductData {
    private String productCode;
    private String productName;
    private int productPrice;
    private int productUnits;
    private String productType;
}
