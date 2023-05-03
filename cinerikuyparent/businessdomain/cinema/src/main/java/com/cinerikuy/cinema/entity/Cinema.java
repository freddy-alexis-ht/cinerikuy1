package com.cinerikuy.cinema.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Data
public class Cinema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String code;
    private String name;
    private String day;
    private int price;
    @ElementCollection
    private Map<String, Integer> dayPrice;
    @ElementCollection
    private List<MovieData> movies;

}
