package com.cinefund.cinefund_movie_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cinefund.cinefund_movie_service.model.Movie;
import com.cinefund.cinefund_movie_service.repository.MovieRepository;

import java.util.List;
import java.util.Optional;
 
@Service
public class MovieService {
 
    @Autowired
    private MovieRepository movieRepository;
 
    // Create movie (producer posts details)
    public Movie createMovie(Movie movie) {
        return movieRepository.save(movie);
    }
 
    // Fetch all movies
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }
 
    public Optional<Movie> getMovieById(Long id){
    	return movieRepository.findById(id);
    }
    
}
 