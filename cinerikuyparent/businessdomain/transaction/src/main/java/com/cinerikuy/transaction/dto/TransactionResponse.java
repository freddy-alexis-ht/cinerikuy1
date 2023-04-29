package com.cinerikuy.transaction.dto;

import com.cinerikuy.transaction.entity.*;
import lombok.Data;

import java.util.List;

@Data
public class TransactionResponse {
    private long id;
    private String code;
    private String status;
    private int movieTotalPrice;
    private int productTotalPrice;
    private int totalPrice;
    private CustomerData customerData;
    private CinemaData cinemaData;
    private MovieData movieData;
    private List<ProductData> productDataList;
}
