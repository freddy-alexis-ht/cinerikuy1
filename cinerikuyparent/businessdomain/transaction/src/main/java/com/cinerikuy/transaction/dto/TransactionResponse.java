package com.cinerikuy.transaction.dto;

import lombok.Data;

@Data
public class TransactionResponse {
    private long id;
    private String code;
    // private String dni;
    private String cinemaCode;
    private String cinemaName;
    //private String movieCode;
    //private List<String> products;
}
