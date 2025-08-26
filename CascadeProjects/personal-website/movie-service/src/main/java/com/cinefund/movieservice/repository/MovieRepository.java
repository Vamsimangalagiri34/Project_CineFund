package com.cinefund.movieservice.repository;

import com.cinefund.movieservice.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    
    List<Movie> findByProducerId(Long producerId);
    
    List<Movie> findByStatus(Movie.MovieStatus status);
    
    List<Movie> findByIsActiveTrue();
    
    List<Movie> findByGenre(String genre);
    
    @Query("SELECT m FROM Movie m WHERE m.status = :status AND m.isActive = true")
    List<Movie> findActiveMoviesByStatus(@Param("status") Movie.MovieStatus status);
    
    @Query("SELECT m FROM Movie m WHERE m.fundingDeadline >= :currentDate AND m.status = 'FUNDING' AND m.isActive = true")
    List<Movie> findActiveMoviesForFunding(@Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT m FROM Movie m WHERE m.fundingDeadline < :currentDate AND m.status = 'FUNDING' AND m.isActive = true")
    List<Movie> findExpiredFundingMovies(@Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT m FROM Movie m WHERE m.raisedAmount >= m.budget AND m.status = 'FUNDING' AND m.isActive = true")
    List<Movie> findFullyFundedMovies();
    
    @Query("SELECT m FROM Movie m WHERE (m.title LIKE %:keyword% OR m.description LIKE %:keyword% OR m.genre LIKE %:keyword% OR m.directorName LIKE %:keyword% OR m.cast LIKE %:keyword%) AND m.isActive = true")
    List<Movie> searchActiveMovies(@Param("keyword") String keyword);
    
    @Query("SELECT m FROM Movie m WHERE m.budget BETWEEN :minBudget AND :maxBudget AND m.isActive = true")
    List<Movie> findMoviesByBudgetRange(@Param("minBudget") BigDecimal minBudget, @Param("maxBudget") BigDecimal maxBudget);
    
    @Query("SELECT m FROM Movie m WHERE m.producerId = :producerId AND m.isActive = true ORDER BY m.createdAt DESC")
    List<Movie> findActiveMoviesByProducer(@Param("producerId") Long producerId);
    
    @Query("SELECT COUNT(m) FROM Movie m WHERE m.producerId = :producerId AND m.status = 'FUNDING' AND m.isActive = true")
    Long countActiveFundingMoviesByProducer(@Param("producerId") Long producerId);
}
