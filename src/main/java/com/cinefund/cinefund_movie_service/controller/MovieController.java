package com.cinefund.cinefund_movie_service.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cinefund.cinefund_movie_service.model.Movie;
import com.cinefund.cinefund_movie_service.service.MovieService;

import java.util.List;
 
@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {
 
    @Autowired
    private MovieService movieService;
 
    // POST - create new movie
    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        return ResponseEntity.ok(movieService.createMovie(movie));
    }
 
    // GET - fetch all movies
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id){
    	return movieService.getMovieById(id)
    			.map(ResponseEntity::ok).
    			orElse(ResponseEntity.notFound().build());
    }
    
    
    
}