package com.cinefund.fundingservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "investments")
public class Investment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String currency="INR";

    @NotNull(message = "User ID is required")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull(message = "Movie ID is required")
    @Column(name = "movie_id", nullable = false)
    private Long movieId;

    @NotNull(message = "Producer ID is required")
    @Column(name = "producer_id", nullable = false)
    private Long producerId;

    @NotNull(message = "Investment amount is required")
    @Positive(message = "Investment amount must be positive")
    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "transaction_id", unique = true)
    private String transactionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvestmentStatus status = InvestmentStatus.PENDING;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "movie_title")
    private String movieTitle;

    @Column(name = "producer_name")
    private String producerName;

    @Column(name = "expected_return_percentage", precision = 5, scale = 2)
    private BigDecimal expectedReturnPercentage;

    @Column(name = "actual_return_amount", precision = 19, scale = 2)
    private BigDecimal actualReturnAmount = BigDecimal.ZERO;

    @Column(name = "return_paid")
    private Boolean returnPaid = false;

    @Column(name = "return_payment_date")
    private LocalDateTime returnPaymentDate;

    @NotNull
    @Column(name = "investment_date", nullable = false)
    private LocalDateTime investmentDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Investment() {}

    public Investment(Long userId, Long movieId, Long producerId, BigDecimal amount, String transactionId) {
        this.userId = userId;
        this.movieId = movieId;
        this.producerId = producerId;
        this.amount = amount;
        this.transactionId = transactionId;
        this.currency="INR";
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

    public InvestmentStatus getStatus() { return status; }
    public void setStatus(InvestmentStatus status) { this.status = status; }

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

    public void setCurrency(String currency) {
        this.currency=currency;
    }

    public enum InvestmentStatus {
        PENDING, CONFIRMED, CANCELLED, REFUNDED, RETURN_PAID
    }

    // Helper methods
    public BigDecimal calculateExpectedReturn() {
        if (expectedReturnPercentage != null && amount != null) {
            return amount.add(amount.multiply(expectedReturnPercentage.divide(new BigDecimal(100))));
        }
        return amount;
    }
}
