package com.cinerikuy.movie.entity;

import com.cinerikuy.movie.service.StringToListConverter;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String code;
    private String name;
    private int price;
    @Convert(converter = StringToListConverter.class)
    private List<String> cinemaCodes;
}
