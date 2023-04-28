package com.cinerikuy.transaction.entity;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class CustomerData {
    private String customerDni;
    private String customerUsername;
    private String customerFirstName;
    private String customerLastName;
}
