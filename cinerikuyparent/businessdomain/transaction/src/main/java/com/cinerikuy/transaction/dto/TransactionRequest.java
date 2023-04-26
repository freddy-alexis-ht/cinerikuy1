package com.cinerikuy.transaction.dto;

import com.cinerikuy.transaction.entity.ProductData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
public class TransactionRequest {
    private String code;
    // private String dni;
    private String cinemaCode;
    //private String movieCode;
    @JsonIgnoreProperties(value = { "productType" })
    private List<ProductData> products;
}
