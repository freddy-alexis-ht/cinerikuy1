package com.cinerikuy.cinema.repository;

import com.cinerikuy.cinema.entity.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long> {
    Cinema findByCode(String code);
}
