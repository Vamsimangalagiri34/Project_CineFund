package com.cinefund.cinefund_movie_service.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
 
@Entity
@Table(name = "movies")
public class Movie {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    private String movieName;
 
    // Cast fields
    private String hero;
    private String heroine;
    private String villain;
    private String director;
    private String producer;
    private String musicDirector;
 
    // Financial details
    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal expectedInvestment;
 
    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal totalInvestment = BigDecimal.ZERO;
 
    private String description;
 
    private LocalDate deadline;
 
    // Whether expected investment reached
    private boolean status = false;
    
   // --- Getters & Setters ---
 
    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public String getMovieName() {
        return movieName;
    }
 
    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }
 
    public String getHero() {
        return hero;
    }
 
    public void setHero(String hero) {
        this.hero = hero;
    }
 
    public String getHeroine() {
        return heroine;
    }
 
    public void setHeroine(String heroine) {
        this.heroine = heroine;
    }
 
    public String getVillain() {
        return villain;
    }
 
    public void setVillain(String villain) {
        this.villain = villain;
    }
 
    public String getDirector() {
        return director;
    }
 
    public void setDirector(String director) {
        this.director = director;
    }
 
    public String getProducer() {
        return producer;
    }
 
    public void setProducer(String producer) {
        this.producer = producer;
    }
 
    public String getMusicDirector() {
        return musicDirector;
    }
 
    public void setMusicDirector(String musicDirector) {
        this.musicDirector = musicDirector;
    }
 
    public BigDecimal getExpectedInvestment() {
        return expectedInvestment;
    }
 
    public void setExpectedInvestment(BigDecimal expectedInvestment) {
        this.expectedInvestment = expectedInvestment;
    }
 
    public BigDecimal getTotalInvestment() {
        return totalInvestment;
    }
 
    public void setTotalInvestment(BigDecimal totalInvestment) {
        this.totalInvestment = totalInvestment;
    }
 
    public String getDescription() {
        return description;
    }
 
    public void setDescription(String description) {
        this.description = description;
    }
 
    public LocalDate getDeadline() {
        return deadline;
    }
 
    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
 
    public boolean isStatus() {
        return status;
    }
 
    public void setStatus(boolean status) {
        this.status = status;
    }
}
 