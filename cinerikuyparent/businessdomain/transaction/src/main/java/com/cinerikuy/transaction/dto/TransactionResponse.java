package com.cinerikuy.transaction.dto;

import com.cinerikuy.transaction.entity.CustomerData;
import com.cinerikuy.transaction.entity.MovieData;
import com.cinerikuy.transaction.entity.ProductData;
import com.cinerikuy.transaction.entity.StatusEnum;
import lombok.Data;

import java.util.List;

@Data
public class TransactionResponse {
    private long id;
    private String code;
    private String status;
    private CustomerData customer;
    private String cinemaCode;
    private String cinemaName;
    private MovieData movie;
    private List<ProductData> products;
}
