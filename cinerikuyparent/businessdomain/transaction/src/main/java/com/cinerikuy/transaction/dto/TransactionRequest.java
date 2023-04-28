package com.cinerikuy.transaction.dto;

import com.cinerikuy.transaction.entity.MovieData;
import com.cinerikuy.transaction.entity.ProductData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
public class TransactionRequest {
    private String code;
    // private String dni;
    private String cinemaCode;
    @JsonIgnoreProperties(value = { "movieName" })
    private MovieData movie;
    @JsonIgnoreProperties(value = { "products.productType" })
    private List<ProductData> products;
}
