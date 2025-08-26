package com.cinefund.fundingservice.repository;

import com.cinefund.fundingservice.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    Optional<Transaction> findByTransactionId(String transactionId);
    
    List<Transaction> findByUserId(Long userId);
    
    List<Transaction> findByMovieId(Long movieId);
    
    List<Transaction> findByProducerId(Long producerId);
    
    List<Transaction> findByStatus(Transaction.TransactionStatus status);
    
    List<Transaction> findByType(Transaction.TransactionType type);
    
    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId AND t.type = :type")
    List<Transaction> findByUserIdAndType(@Param("userId") Long userId, @Param("type") Transaction.TransactionType type);
    
    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId AND t.status = :status")
    List<Transaction> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Transaction.TransactionStatus status);
    
    @Query("SELECT t FROM Transaction t WHERE t.createdAt BETWEEN :startDate AND :endDate")
    List<Transaction> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.userId = :userId AND t.type = :type AND t.status = 'SUCCESS'")
    BigDecimal getTotalAmountByUserAndType(@Param("userId") Long userId, @Param("type") Transaction.TransactionType type);
    
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.movieId = :movieId AND t.type = 'INVESTMENT' AND t.status = 'SUCCESS'")
    BigDecimal getTotalInvestmentAmountByMovie(@Param("movieId") Long movieId);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.status = 'PENDING'")
    Long getPendingTransactionCount();
}
