package com.cinefund.fundingservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class ReturnProcessingDto {
    
    @NotNull(message = "Total revenue is required")
    @Positive(message = "Total revenue must be positive")
    private BigDecimal totalRevenue;
    
    private String notes;
    
    // Constructors
    public ReturnProcessingDto() {}
    
    public ReturnProcessingDto(BigDecimal totalRevenue, String notes) {
        this.totalRevenue = totalRevenue;
        this.notes = notes;
    }
    
    // Getters and Setters
    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }
    
    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
