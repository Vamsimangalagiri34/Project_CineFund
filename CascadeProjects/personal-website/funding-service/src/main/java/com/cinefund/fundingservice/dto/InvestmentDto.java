package com.cinefund.fundingservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class InvestmentDto {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Movie ID is required")
    private Long movieId;

    @NotNull(message = "Producer ID is required")
    private Long producerId;

    @NotNull(message = "Investment amount is required")
    @Positive(message = "Investment amount must be positive")
    private BigDecimal amount;

    private String currency = "INR";
    private String paymentMethod;
    private String userName;
    private String movieTitle;
    private String producerName;
    private BigDecimal expectedReturnPercentage;

    // Constructors
    public InvestmentDto() {}

    public InvestmentDto(Long userId, Long movieId, Long producerId, BigDecimal amount) {
        this.userId = userId;
        this.movieId = movieId;
        this.producerId = producerId;
        this.amount = amount;
    }

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getMovieId() { return movieId; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }

    public Long getProducerId() { return producerId; }
    public void setProducerId(Long producerId) { this.producerId = producerId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }

    public String getProducerName() { return producerName; }
    public void setProducerName(String producerName) { this.producerName = producerName; }

    public BigDecimal getExpectedReturnPercentage() { return expectedReturnPercentage; }
    public void setExpectedReturnPercentage(BigDecimal expectedReturnPercentage) { this.expectedReturnPercentage = expectedReturnPercentage; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
