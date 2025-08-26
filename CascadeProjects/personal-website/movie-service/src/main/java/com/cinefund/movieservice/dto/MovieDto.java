package com.cinefund.movieservice.dto;

import com.cinefund.movieservice.entity.Movie;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MovieDto {
    @NotBlank(message = "Movie title is required")
    private String title;

    private String description;
    private String storyline;

    @NotNull(message = "Budget is required")
    @Positive(message = "Budget must be positive")
    private BigDecimal budget;

    private BigDecimal expectedReturnPercentage;

    @NotNull(message = "Producer ID is required")
    private Long producerId;

    private String producerName;
    private String directorName;
    @Column(name = "movie_cast")
    private String cast;

//    private String cast;
    private String genre;
    private LocalDate releaseDate;
    private LocalDate productionStartDate;
    private LocalDate fundingDeadline;
    private String posterUrl;
    private String trailerUrl;

    // Constructors
    public MovieDto() {}

    public MovieDto(String title, String description, BigDecimal budget, Long producerId) {
        this.title = title;
        this.description = description;
        this.budget = budget;
        this.producerId = producerId;
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStoryline() { return storyline; }
    public void setStoryline(String storyline) { this.storyline = storyline; }

    public BigDecimal getBudget() { return budget; }
    public void setBudget(BigDecimal budget) { this.budget = budget; }

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

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

    public String getTrailerUrl() { return trailerUrl; }
    public void setTrailerUrl(String trailerUrl) { this.trailerUrl = trailerUrl; }
}
