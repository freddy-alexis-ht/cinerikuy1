package com.cinerikuy.transaction.dto;

import lombok.Data;

@Data
public class TransactionRequest {
    private String code;
    // private String dni;
    private String cinemaCode;
    //private String movieCode;
    //private List<String> products;
}
