package com.cinerikuy.transaction.dto;

import com.cinerikuy.transaction.entity.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
public class TransactionRequest {
    private String code;
    private CustomerData customerData;
    private CinemaData cinemaData;
    @JsonIgnoreProperties(value = { "movieName" })
    private MovieData movieData;
    @JsonIgnoreProperties(value = { "products.productType" })
    private List<ProductData> productDataList;
}
