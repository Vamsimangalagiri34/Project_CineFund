package com.cinefund.movieservice.service;

import com.cinefund.movieservice.dto.MovieDto;
import com.cinefund.movieservice.dto.MovieResponseDto;
import com.cinefund.movieservice.entity.Movie;
import com.cinefund.movieservice.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public MovieResponseDto createMovie(MovieDto movieDto) {
        Movie movie = new Movie();
        movie.setTitle(movieDto.getTitle());
        movie.setDescription(movieDto.getDescription());
        movie.setStoryline(movieDto.getStoryline());
        movie.setBudget(movieDto.getBudget());
        movie.setExpectedReturnPercentage(movieDto.getExpectedReturnPercentage());
        movie.setProducerId(movieDto.getProducerId());
        movie.setProducerName(movieDto.getProducerName());
        movie.setDirectorName(movieDto.getDirectorName());
        movie.setCast(movieDto.getCast());
        movie.setGenre(movieDto.getGenre());
        movie.setReleaseDate(movieDto.getReleaseDate());
        movie.setProductionStartDate(movieDto.getProductionStartDate());
        movie.setFundingDeadline(movieDto.getFundingDeadline());
        movie.setPosterUrl(movieDto.getPosterUrl());
        movie.setTrailerUrl(movieDto.getTrailerUrl());
        movie.setStatus(Movie.MovieStatus.FUNDING);
        movie.setRaisedAmount(BigDecimal.ZERO);
        movie.setIsActive(true);

        Movie savedMovie = movieRepository.save(movie);
        return new MovieResponseDto(savedMovie);
    }

    public MovieResponseDto getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        return new MovieResponseDto(movie);
    }

    public List<MovieResponseDto> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(MovieResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<MovieResponseDto> getActiveMovies() {
        return movieRepository.findByIsActiveTrue().stream()
                .map(MovieResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<MovieResponseDto> getMoviesByProducer(Long producerId) {
        return movieRepository.findActiveMoviesByProducer(producerId).stream()
                .map(MovieResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<MovieResponseDto> getMoviesByStatus(Movie.MovieStatus status) {
        return movieRepository.findActiveMoviesByStatus(status).stream()
                .map(MovieResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<MovieResponseDto> getMoviesForFunding() {
        return movieRepository.findActiveMoviesForFunding(LocalDate.now()).stream()
                .map(MovieResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<MovieResponseDto> getMoviesByGenre(String genre) {
        return movieRepository.findByGenre(genre).stream()
                .filter(movie -> movie.getIsActive())
                .map(MovieResponseDto::new)
                .collect(Collectors.toList());
    }

    public MovieResponseDto updateMovie(Long id, MovieDto movieDto) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        movie.setTitle(movieDto.getTitle());
        movie.setDescription(movieDto.getDescription());
        movie.setStoryline(movieDto.getStoryline());
        movie.setBudget(movieDto.getBudget());
        movie.setExpectedReturnPercentage(movieDto.getExpectedReturnPercentage());
        movie.setDirectorName(movieDto.getDirectorName());
        movie.setCast(movieDto.getCast());
        movie.setGenre(movieDto.getGenre());
        movie.setReleaseDate(movieDto.getReleaseDate());
        movie.setProductionStartDate(movieDto.getProductionStartDate());
        movie.setFundingDeadline(movieDto.getFundingDeadline());
        movie.setPosterUrl(movieDto.getPosterUrl());
        movie.setTrailerUrl(movieDto.getTrailerUrl());

        Movie updatedMovie = movieRepository.save(movie);
        return new MovieResponseDto(updatedMovie);
    }

    public MovieResponseDto updateMovieStatus(Long id, Movie.MovieStatus status) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        
        movie.setStatus(status);
        Movie updatedMovie = movieRepository.save(movie);
        return new MovieResponseDto(updatedMovie);
    }

    public MovieResponseDto updateRaisedAmount(Long movieId, BigDecimal amount) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        
        BigDecimal newAmount = movie.getRaisedAmount().add(amount);
        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Invalid amount: raised amount cannot be negative");
        }
        
        movie.setRaisedAmount(newAmount);
        
        // Auto-update status if funding is complete
        if (movie.isFundingComplete() && movie.getStatus() == Movie.MovieStatus.FUNDING) {
            movie.setStatus(Movie.MovieStatus.PRODUCTION);
        }
        
        Movie updatedMovie = movieRepository.save(movie);
        return new MovieResponseDto(updatedMovie);
    }

    public void deactivateMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        movie.setIsActive(false);
        movieRepository.save(movie);
    }

    public void activateMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        movie.setIsActive(true);
        movieRepository.save(movie);
    }

    public List<MovieResponseDto> searchMovies(String keyword) {
        return movieRepository.searchActiveMovies(keyword).stream()
                .map(MovieResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<MovieResponseDto> getMoviesByBudgetRange(BigDecimal minBudget, BigDecimal maxBudget) {
        return movieRepository.findMoviesByBudgetRange(minBudget, maxBudget).stream()
                .map(MovieResponseDto::new)
                .collect(Collectors.toList());
    }

    public Long getActiveFundingMoviesCountByProducer(Long producerId) {
        return movieRepository.countActiveFundingMoviesByProducer(producerId);
    }

    public List<MovieResponseDto> getExpiredFundingMovies() {
        return movieRepository.findExpiredFundingMovies(LocalDate.now()).stream()
                .map(MovieResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<MovieResponseDto> getFullyFundedMovies() {
        return movieRepository.findFullyFundedMovies().stream()
                .map(MovieResponseDto::new)
                .collect(Collectors.toList());
    }
}
