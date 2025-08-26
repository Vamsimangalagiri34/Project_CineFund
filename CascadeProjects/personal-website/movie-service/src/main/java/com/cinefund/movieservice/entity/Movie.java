package com.cinefund.movieservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Movie title is required")
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String storyline;

    @NotNull(message = "Budget is required")
    @Positive(message = "Budget must be positive")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal budget;

    @Column(name = "raised_amount", precision = 19, scale = 2)
    private BigDecimal raisedAmount = BigDecimal.ZERO;

    @Column(name = "expected_return_percentage", precision = 5, scale = 2)
    private BigDecimal expectedReturnPercentage;

    @NotNull(message = "Producer ID is required")
    @Column(name = "producer_id", nullable = false)
    private Long producerId;

    @Column(name = "producer_name")
    private String producerName;

    @Column(name = "director_name")
    private String directorName;

//    @Column(columnDefinition = "TEXT")
    @Column(name = "movie_cast")
    private String cast;

//    private String cast;

    @Column
    private String genre;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "production_start_date")
    private LocalDate productionStartDate;

    @Column(name = "funding_deadline")
    private LocalDate fundingDeadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovieStatus status = MovieStatus.FUNDING;

    @Column(name = "poster_url")
    private String posterUrl;

    @Column(name = "trailer_url")
    private String trailerUrl;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Movie() {}

    public Movie(String title, String description, BigDecimal budget, Long producerId, String producerName) {
        this.title = title;
        this.description = description;
        this.budget = budget;
        this.producerId = producerId;
        this.producerName = producerName;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStoryline() { return storyline; }
    public void setStoryline(String storyline) { this.storyline = storyline; }

    public BigDecimal getBudget() { return budget; }
    public void setBudget(BigDecimal budget) { this.budget = budget; }

    public BigDecimal getRaisedAmount() { return raisedAmount; }
    public void setRaisedAmount(BigDecimal raisedAmount) { this.raisedAmount = raisedAmount; }

    public BigDecimal getExpectedReturnPercentage() { return expectedReturnPercentage; }
    public void setExpectedReturnPercentage(BigDecimal expectedReturnPercentage) { this.expectedReturnPercentage = expectedReturnPercentage; }

    public Long getProducerId() { return producerId; }
    public void setProducerId(Long producerId) { this.producerId = producerId; }

    public String getProducerName() { return producerName; }
    public void setProducerName(String producerName) { this.producerName = producerName; }

    public String getDirectorName() { return directorName; }
    public void setDirectorName(String directorName) { this.directorName = directorName; }

    public String getCast() { return cast; }
    public void setCast(String cast) { this.cast = cast; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    public LocalDate getProductionStartDate() { return productionStartDate; }
    public void setProductionStartDate(LocalDate productionStartDate) { this.productionStartDate = productionStartDate; }

    public LocalDate getFundingDeadline() { return fundingDeadline; }
    public void setFundingDeadline(LocalDate fundingDeadline) { this.fundingDeadline = fundingDeadline; }

    public MovieStatus getStatus() { return status; }
    public void setStatus(MovieStatus status) { this.status = status; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

    public String getTrailerUrl() { return trailerUrl; }
    public void setTrailerUrl(String trailerUrl) { this.trailerUrl = trailerUrl; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public enum MovieStatus {
        FUNDING, PRODUCTION, POST_PRODUCTION, RELEASED, CANCELLED
    }

    // Helper methods
    public BigDecimal getFundingProgress() {
        if (budget == null || budget.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return raisedAmount.divide(budget, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
    }

    public boolean isFundingComplete() {
        return raisedAmount.compareTo(budget) >= 0;
    }
}
