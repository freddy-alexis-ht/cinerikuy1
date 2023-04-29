package com.cinerikuy.transaction.dto;

import com.cinerikuy.transaction.entity.CustomerData;
import com.cinerikuy.transaction.entity.MovieData;
import com.cinerikuy.transaction.entity.ProductData;
import com.cinerikuy.transaction.entity.StatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
public class TransactionRequest {
    private String code;
    private CustomerData customer;
    private String cinemaCode;
    @JsonIgnoreProperties(value = { "movieName" })
    private MovieData movie;
    @JsonIgnoreProperties(value = { "products.productType" })
    private List<ProductData> products;
}
