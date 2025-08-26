package com.cinefund.movieservice.controller;

import com.cinefund.movieservice.dto.MovieDto;
import com.cinefund.movieservice.dto.MovieResponseDto;
import com.cinefund.movieservice.entity.Movie;
import com.cinefund.movieservice.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "*")
@Tag(name = "Movie Management", description = "APIs for movie creation, listing, and management")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping
    @Operation(summary = "Create a new movie", description = "Create a new movie project for funding")
    public ResponseEntity<?> createMovie(@Valid @RequestBody MovieDto movieDto) {
        try {
            MovieResponseDto movie = movieService.createMovie(movieDto);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Movie created successfully");
            response.put("movie", movie);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get movie by ID", description = "Retrieve movie details by movie ID")
    public ResponseEntity<?> getMovieById(@PathVariable Long id) {
        try {
            MovieResponseDto movie = movieService.getMovieById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("movie", movie);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Get all movies", description = "Retrieve all movies in the system")
    public ResponseEntity<?> getAllMovies(@RequestParam(name = "activeOnly", defaultValue = "false") boolean activeOnly) {
        List<MovieResponseDto> movies = activeOnly ? movieService.getActiveMovies() : movieService.getAllMovies();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("movies", movies);
        response.put("count", movies.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/funding")
    @Operation(summary = "Get movies available for funding", description = "Retrieve movies that are currently accepting investments")
    public ResponseEntity<?> getMoviesForFunding() {
        List<MovieResponseDto> movies = movieService.getMoviesForFunding();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("movies", movies);
        response.put("count", movies.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/producer/{producerId}")
    @Operation(summary = "Get movies by producer", description = "Retrieve all movies by a specific producer")
    public ResponseEntity<?> getMoviesByProducer(@PathVariable Long producerId) {
        List<MovieResponseDto> movies = movieService.getMoviesByProducer(producerId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("movies", movies);
        response.put("count", movies.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get movies by status", description = "Retrieve movies by their current status")
    public ResponseEntity<?> getMoviesByStatus(@PathVariable Movie.MovieStatus status) {
        List<MovieResponseDto> movies = movieService.getMoviesByStatus(status);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("movies", movies);
        response.put("count", movies.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/genre/{genre}")
    @Operation(summary = "Get movies by genre", description = "Retrieve movies by genre")
    public ResponseEntity<?> getMoviesByGenre(@PathVariable String genre) {
        List<MovieResponseDto> movies = movieService.getMoviesByGenre(genre);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("movies", movies);
        response.put("count", movies.size());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update movie", description = "Update movie details")
    public ResponseEntity<?> updateMovie(@PathVariable Long id, @Valid @RequestBody MovieDto movieDto) {
        try {
            MovieResponseDto movie = movieService.updateMovie(id, movieDto);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Movie updated successfully");
            response.put("movie", movie);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update movie status", description = "Update the status of a movie")
    public ResponseEntity<?> updateMovieStatus(@PathVariable Long id, @RequestParam(name = "status") Movie.MovieStatus status) {
        try {
            MovieResponseDto movie = movieService.updateMovieStatus(id, status);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Movie status updated successfully");
            response.put("movie", movie);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}/funding")
    @Operation(summary = "Update raised amount", description = "Update the amount raised for a movie")
    public ResponseEntity<?> updateRaisedAmount(@PathVariable Long id, @RequestParam(name = "amount") BigDecimal amount) {
        try {
            MovieResponseDto movie = movieService.updateRaisedAmount(id, amount);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Raised amount updated successfully");
            response.put("movie", movie);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate movie", description = "Deactivate a movie")
    public ResponseEntity<?> deactivateMovie(@PathVariable Long id) {
        try {
            movieService.deactivateMovie(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Movie deactivated successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate movie", description = "Activate a movie")
    public ResponseEntity<?> activateMovie(@PathVariable Long id) {
        try {
            movieService.activateMovie(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Movie activated successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search movies", description = "Search movies by title, description, genre, director, or cast")
    public ResponseEntity<?> searchMovies(@RequestParam(name = "keyword") String keyword) {
        List<MovieResponseDto> movies = movieService.searchMovies(keyword);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("movies", movies);
        response.put("count", movies.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/budget-range")
    @Operation(summary = "Get movies by budget range", description = "Retrieve movies within a specific budget range")
    public ResponseEntity<?> getMoviesByBudgetRange(@RequestParam(name = "minBudget") BigDecimal minBudget, @RequestParam(name = "maxBudget") BigDecimal maxBudget) {
        List<MovieResponseDto> movies = movieService.getMoviesByBudgetRange(minBudget, maxBudget);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("movies", movies);
        response.put("count", movies.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/producer/{producerId}/funding-count")
    @Operation(summary = "Get active funding movies count by producer", description = "Get count of active funding movies for a producer")
    public ResponseEntity<?> getActiveFundingMoviesCount(@PathVariable Long producerId) {
        Long count = movieService.getActiveFundingMoviesCountByProducer(producerId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
}
