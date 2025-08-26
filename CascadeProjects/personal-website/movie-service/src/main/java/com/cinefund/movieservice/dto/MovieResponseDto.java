package com.cinefund.movieservice.dto;

import com.cinefund.movieservice.entity.Movie;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MovieResponseDto {
    private Long id;
    private String title;
    private String description;
    private String storyline;
    private BigDecimal budget;
    private BigDecimal raisedAmount;
    private BigDecimal expectedReturnPercentage;
    private Long producerId;
    private String producerName;
    private String directorName;
    private String cast;
    private String genre;
    private LocalDate releaseDate;
    private LocalDate productionStartDate;
    private LocalDate fundingDeadline;
    private Movie.MovieStatus status;
    private String posterUrl;
    private String trailerUrl;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal fundingProgress;
    private boolean fundingComplete;

    // Constructors
    public MovieResponseDto() {}

    public MovieResponseDto(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.description = movie.getDescription();
        this.storyline = movie.getStoryline();
        this.budget = movie.getBudget();
        this.raisedAmount = movie.getRaisedAmount();
        this.expectedReturnPercentage = movie.getExpectedReturnPercentage();
        this.producerId = movie.getProducerId();
        this.producerName = movie.getProducerName();
        this.directorName = movie.getDirectorName();
        this.cast = movie.getCast();
        this.genre = movie.getGenre();
        this.releaseDate = movie.getReleaseDate();
        this.productionStartDate = movie.getProductionStartDate();
        this.fundingDeadline = movie.getFundingDeadline();
        this.status = movie.getStatus();
        this.posterUrl = movie.getPosterUrl();
        this.trailerUrl = movie.getTrailerUrl();
        this.isActive = movie.getIsActive();
        this.createdAt = movie.getCreatedAt();
        this.updatedAt = movie.getUpdatedAt();
        this.fundingProgress = movie.getFundingProgress();
        this.fundingComplete = movie.isFundingComplete();
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

    public Movie.MovieStatus getStatus() { return status; }
    public void setStatus(Movie.MovieStatus status) { this.status = status; }

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

    public BigDecimal getFundingProgress() { return fundingProgress; }
    public void setFundingProgress(BigDecimal fundingProgress) { this.fundingProgress = fundingProgress; }

    public boolean isFundingComplete() { return fundingComplete; }
    public void setFundingComplete(boolean fundingComplete) { this.fundingComplete = fundingComplete; }
}
