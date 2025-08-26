package com.cinefund.fundingservice.dto;

import com.cinefund.fundingservice.entity.Investment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InvestmentResponseDto {
    private Long id;
    private Long userId;
    private Long movieId;
    private Long producerId;
    private BigDecimal amount;
    private String transactionId;
    private Investment.InvestmentStatus status;
    private String userName;
    private String movieTitle;
    private String producerName;
    private BigDecimal expectedReturnPercentage;
    private BigDecimal actualReturnAmount;
    private Boolean returnPaid;
    private LocalDateTime returnPaymentDate;
    private LocalDateTime investmentDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal expectedReturn;

    // Constructors
    public InvestmentResponseDto() {}

    public InvestmentResponseDto(Investment investment) {
        this.id = investment.getId();
        this.userId = investment.getUserId();
        this.movieId = investment.getMovieId();
        this.producerId = investment.getProducerId();
        this.amount = investment.getAmount();
        this.transactionId = investment.getTransactionId();
        this.status = investment.getStatus();
        this.userName = investment.getUserName();
        this.movieTitle = investment.getMovieTitle();
        this.producerName = investment.getProducerName();
        this.expectedReturnPercentage = investment.getExpectedReturnPercentage();
        this.actualReturnAmount = investment.getActualReturnAmount();
        this.returnPaid = investment.getReturnPaid();
        this.returnPaymentDate = investment.getReturnPaymentDate();
        this.investmentDate = investment.getInvestmentDate();
        this.createdAt = investment.getCreatedAt();
        this.updatedAt = investment.getUpdatedAt();
        this.expectedReturn = investment.calculateExpectedReturn();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getMovieId() { return movieId; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }

    public Long getProducerId() { return producerId; }
    public void setProducerId(Long producerId) { this.producerId = producerId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public Investment.InvestmentStatus getStatus() { return status; }
    public void setStatus(Investment.InvestmentStatus status) { this.status = status; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }

    public String getProducerName() { return producerName; }
    public void setProducerName(String producerName) { this.producerName = producerName; }

    public BigDecimal getExpectedReturnPercentage() { return expectedReturnPercentage; }
    public void setExpectedReturnPercentage(BigDecimal expectedReturnPercentage) { this.expectedReturnPercentage = expectedReturnPercentage; }

    public BigDecimal getActualReturnAmount() { return actualReturnAmount; }
    public void setActualReturnAmount(BigDecimal actualReturnAmount) { this.actualReturnAmount = actualReturnAmount; }

    public Boolean getReturnPaid() { return returnPaid; }
    public void setReturnPaid(Boolean returnPaid) { this.returnPaid = returnPaid; }

    public LocalDateTime getReturnPaymentDate() { return returnPaymentDate; }
    public void setReturnPaymentDate(LocalDateTime returnPaymentDate) { this.returnPaymentDate = returnPaymentDate; }

    public LocalDateTime getInvestmentDate() { return investmentDate; }
    public void setInvestmentDate(LocalDateTime investmentDate) { this.investmentDate = investmentDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public BigDecimal getExpectedReturn() { return expectedReturn; }
    public void setExpectedReturn(BigDecimal expectedReturn) { this.expectedReturn = expectedReturn; }
}
