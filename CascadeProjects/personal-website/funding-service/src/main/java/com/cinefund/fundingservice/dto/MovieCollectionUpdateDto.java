package com.cinefund.fundingservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public class MovieCollectionUpdateDto {
    
    @NotNull(message = "Collection amount is required")
    @Positive(message = "Collection amount must be positive")
    private BigDecimal collectionAmount;
    
    @NotNull(message = "Collection date is required")
    private LocalDate collectionDate;
    
    private String notes;
    private Boolean autoDistributeReturns = true;
    
    // Constructors
    public MovieCollectionUpdateDto() {}
    
    public MovieCollectionUpdateDto(BigDecimal collectionAmount, LocalDate collectionDate) {
        this.collectionAmount = collectionAmount;
        this.collectionDate = collectionDate;
    }
    
    // Getters and Setters
    public BigDecimal getCollectionAmount() {
        return collectionAmount;
    }
    
    public void setCollectionAmount(BigDecimal collectionAmount) {
        this.collectionAmount = collectionAmount;
    }
    
    public LocalDate getCollectionDate() {
        return collectionDate;
    }
    
    public void setCollectionDate(LocalDate collectionDate) {
        this.collectionDate = collectionDate;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public Boolean getAutoDistributeReturns() {
        return autoDistributeReturns;
    }
    
    public void setAutoDistributeReturns(Boolean autoDistributeReturns) {
        this.autoDistributeReturns = autoDistributeReturns;
    }
}
