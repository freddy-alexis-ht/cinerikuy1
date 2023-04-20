package com.cinerikuy.movie.controller;

import com.cinerikuy.movie.entity.Movie;
import com.cinerikuy.movie.exception.BusinessRuleException;
import com.cinerikuy.movie.repository.MovieRepository;
import com.cinerikuy.movie.service.MovieComm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieComm movieComm;

    @GetMapping()
    public ResponseEntity<List<Movie>> get() {
        List<Movie> list = movieRepository.findAll();
        if(list == null || list.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/id/{id}")
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
    public ResponseEntity<?> post(@RequestBody Movie input) throws BusinessRuleException, UnknownHostException {
        movieComm.validateCinemasExistence(input);
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

    // OTHER METHODS

    @GetMapping("/code/{cinemaCode}")
    public ResponseEntity<List<Movie>> getMoviesByCinemaCode(@PathVariable String cinemaCode) {
        List<Movie> list = movieRepository.findAll()
                .stream().filter(m -> m.getCinemaCodes().contains(cinemaCode))
                .collect(Collectors.toList());
        if(list == null || list.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }
}
