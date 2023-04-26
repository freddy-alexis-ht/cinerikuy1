package com.cinerikuy.billing.entity;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class BillingData {
    private String firstName;
    private String lastName;
}
