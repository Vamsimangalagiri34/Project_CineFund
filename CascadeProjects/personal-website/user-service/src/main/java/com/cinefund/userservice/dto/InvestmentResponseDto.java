package com.cinefund.userservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InvestmentResponseDto {
    private Long id;
    private Long investorId;
    private String investorName;
    private Long movieId;
    private String movieTitle;
    private Long producerId;
    private String producerName;
    private BigDecimal amount;
    private String notes;
    private LocalDateTime investmentDate;
    private String status;

    // Constructors
    public InvestmentResponseDto() {}

    public InvestmentResponseDto(Long id, Long investorId, String investorName, Long movieId, 
                               String movieTitle, Long producerId, String producerName, 
                               BigDecimal amount, String notes, LocalDateTime investmentDate, String status) {
        this.id = id;
        this.investorId = investorId;
        this.investorName = investorName;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.producerId = producerId;
        this.producerName = producerName;
        this.amount = amount;
        this.notes = notes;
        this.investmentDate = investmentDate;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getInvestorId() { return investorId; }
    public void setInvestorId(Long investorId) { this.investorId = investorId; }

    public String getInvestorName() { return investorName; }
    public void setInvestorName(String investorName) { this.investorName = investorName; }

    public Long getMovieId() { return movieId; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }

    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }

    public Long getProducerId() { return producerId; }
    public void setProducerId(Long producerId) { this.producerId = producerId; }

    public String getProducerName() { return producerName; }
    public void setProducerName(String producerName) { this.producerName = producerName; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getInvestmentDate() { return investmentDate; }
    public void setInvestmentDate(LocalDateTime investmentDate) { this.investmentDate = investmentDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
