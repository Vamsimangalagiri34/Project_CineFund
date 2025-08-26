package com.cinefund.fundingservice.repository;

import com.cinefund.fundingservice.entity.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {
    
    List<Investment> findByUserId(Long userId);
    
    List<Investment> findByMovieId(Long movieId);
    
    List<Investment> findByProducerId(Long producerId);
    
    List<Investment> findByStatus(Investment.InvestmentStatus status);
    
    Optional<Investment> findByTransactionId(String transactionId);
    
    @Query("SELECT i FROM Investment i WHERE i.userId = :userId AND i.status = :status")
    List<Investment> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Investment.InvestmentStatus status);
    
    @Query("SELECT i FROM Investment i WHERE i.movieId = :movieId AND i.status = 'CONFIRMED'")
    List<Investment> findConfirmedInvestmentsByMovie(@Param("movieId") Long movieId);
    
    @Query("SELECT i FROM Investment i WHERE i.producerId = :producerId AND i.status = 'CONFIRMED'")
    List<Investment> findConfirmedInvestmentsByProducer(@Param("producerId") Long producerId);
    
    @Query("SELECT SUM(i.amount) FROM Investment i WHERE i.movieId = :movieId AND i.status = 'CONFIRMED'")
    BigDecimal getTotalInvestmentByMovie(@Param("movieId") Long movieId);
    
    @Query("SELECT SUM(i.amount) FROM Investment i WHERE i.userId = :userId AND i.status = 'CONFIRMED'")
    BigDecimal getTotalInvestmentByUser(@Param("userId") Long userId);
    
    @Query("SELECT SUM(i.amount) FROM Investment i WHERE i.producerId = :producerId AND i.status = 'CONFIRMED'")
    BigDecimal getTotalInvestmentByProducer(@Param("producerId") Long producerId);
    
    @Query("SELECT COUNT(i) FROM Investment i WHERE i.movieId = :movieId AND i.status = 'CONFIRMED'")
    Long getInvestorCountByMovie(@Param("movieId") Long movieId);
    
    @Query("SELECT i FROM Investment i WHERE i.returnPaid = false AND i.status = 'CONFIRMED'")
    List<Investment> findUnpaidReturns();
    
    @Query("SELECT i FROM Investment i WHERE i.movieId = :movieId AND i.returnPaid = false AND i.status = 'CONFIRMED'")
    List<Investment> findUnpaidReturnsByMovie(@Param("movieId") Long movieId);
    
    @Query("SELECT DISTINCT i.movieId FROM Investment i WHERE i.userId = :userId AND i.status = 'CONFIRMED'")
    List<Long> getMovieIdsInvestedByUser(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(DISTINCT i.userId) FROM Investment i WHERE i.producerId = :producerId AND i.status = 'CONFIRMED'")
    Long getUniqueInvestorCountByProducer(@Param("producerId") Long producerId);
    
    List<Investment> findByProducerIdAndMovieId(Long producerId, Long movieId);
}
