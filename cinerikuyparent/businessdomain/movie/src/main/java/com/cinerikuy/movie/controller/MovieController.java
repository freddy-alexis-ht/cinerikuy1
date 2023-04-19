package com.cinerikuy.movie.controller;

import com.cinerikuy.movie.entity.Movie;
import com.cinerikuy.movie.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping()
    public List<Movie> get() {
        return movieRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> get(@PathVariable long id) {
        return movieRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable long id, @RequestBody Movie input) {
        Movie exist = movieRepository.findById(id).get();
        if(exist != null){
            exist.setCode(input.getCode());
            exist.setName(input.getName());
        }
        Movie obj = movieRepository.save(exist);
        return ResponseEntity.ok(obj);
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody Movie input) {
        Movie save = movieRepository.save(input);
        return ResponseEntity.ok(save);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        Optional<Movie> findById = movieRepository.findById(id);
        if(findById.get() != null)
            movieRepository.delete(findById.get());
        return ResponseEntity.ok().build();
    }
}
