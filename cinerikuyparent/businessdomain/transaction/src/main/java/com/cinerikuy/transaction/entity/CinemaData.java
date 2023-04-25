package com.cinerikuy.transaction.entity;

import lombok.Data;

import javax.persistence.Embeddable;

@Embeddable
@Data
public class CinemaData {
    private String cinemaCode;
    private String cinemaName;
}
