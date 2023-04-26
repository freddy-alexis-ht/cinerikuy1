package com.cinerikuy.billing.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Billing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String code;
    @ElementCollection
    private List<BillingData> names;
}
