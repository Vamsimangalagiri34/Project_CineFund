package com.cinefund.userservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class InvestmentDto {
    @NotNull(message = "Movie ID is required")
    private Long movieId;

    @NotNull(message = "Investment amount is required")
    @DecimalMin(value = "1.0", message = "Investment amount must be at least 1")
    private BigDecimal amount;

    private String notes;

    // Constructors
    public InvestmentDto() {}

    public InvestmentDto(Long movieId, BigDecimal amount, String notes) {
        this.movieId = movieId;
        this.amount = amount;
        this.notes = notes;
    }

    // Getters and Setters
    public Long getMovieId() { return movieId; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
