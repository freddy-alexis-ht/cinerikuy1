package com.cinerikuy.cinema.entity;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class MovieData {
    private String movieCode;
    private String movieName;
}
