package com.cinerikuy.transaction.dto;

import com.cinerikuy.transaction.entity.ProductData;
import lombok.Data;

import java.util.List;

@Data
public class TransactionResponse {
    private long id;
    private String code;
    // private String dni;
    private String cinemaCode;
    private String cinemaName;
    //private String movieCode;
    private List<ProductData> products;
}
