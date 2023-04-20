package com.cinerikuy.cinema.controller;

import com.cinerikuy.cinema.entity.Cinema;
import com.cinerikuy.cinema.repository.CinemaRepository;
import com.cinerikuy.cinema.service.CinemaComm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cinemas")
public class CinemaController {

    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private CinemaComm cinemaComm;

    @GetMapping()
    public ResponseEntity<List<Cinema>> get() {
        List<Cinema> list = cinemaRepository.findAll();
        if(list == null || list.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Cinema> get(@PathVariable long id) {
        return cinemaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable long id, @RequestBody Cinema input) {
        Cinema exist = cinemaRepository.findById(id).get();
        if(exist != null){
            exist.setCode(input.getCode());
            exist.setName(input.getName());
        }
        Cinema obj = cinemaRepository.save(exist);
        return ResponseEntity.ok(obj);
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody Cinema input) {
        Cinema save = cinemaRepository.save(input);
        return ResponseEntity.ok(save);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        Optional<Cinema> findById = cinemaRepository.findById(id);
        if(findById.get() != null)
            cinemaRepository.delete(findById.get());
        return ResponseEntity.ok().build();
    }

    /** OTHER METHODS */

    @GetMapping("/code/{cinemaCode}")
    public ResponseEntity<Cinema> get(@PathVariable String cinemaCode) {
        Cinema exist = cinemaRepository.findByCode(cinemaCode);
        if(exist == null)
            return ResponseEntity.notFound().build();
        exist.setMovies(cinemaComm.getMovies(cinemaCode));
        return ResponseEntity.ok(exist);
    }

}
