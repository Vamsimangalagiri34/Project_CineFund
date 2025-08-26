package com.cinefund.fundingservice.service;

import com.cinefund.fundingservice.dto.InvestmentDto;
import com.cinefund.fundingservice.dto.InvestmentResponseDto;
import com.cinefund.fundingservice.entity.Investment;
import com.cinefund.fundingservice.entity.Transaction;
import com.cinefund.fundingservice.repository.InvestmentRepository;
import com.cinefund.fundingservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FundingService {

    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public InvestmentResponseDto createInvestment(InvestmentDto investmentDto) {
        // Generate unique transaction ID
        String transactionId = "TXN_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();

        // Create investment record
        Investment investment = new Investment();
        investment.setUserId(investmentDto.getUserId());
        investment.setMovieId(investmentDto.getMovieId());
        investment.setProducerId(investmentDto.getProducerId());
        investment.setAmount(investmentDto.getAmount());
        investment.setTransactionId(transactionId);
        investment.setCurrency(investmentDto.getCurrency());
        
        // Set optional fields with defaults if null
        investment.setUserName(investmentDto.getUserName() != null ? investmentDto.getUserName() : "User " + investmentDto.getUserId());
        investment.setMovieTitle(investmentDto.getMovieTitle() != null ? investmentDto.getMovieTitle() : "Movie " + investmentDto.getMovieId());
        investment.setProducerName(investmentDto.getProducerName() != null ? investmentDto.getProducerName() : "Producer " + investmentDto.getProducerId());
        
        // Set default expected return percentage if not provided (15%)
        BigDecimal expectedReturnPercentage = investmentDto.getExpectedReturnPercentage() != null ? 
            investmentDto.getExpectedReturnPercentage() : new BigDecimal("15.0");
        investment.setExpectedReturnPercentage(expectedReturnPercentage);
        
        investment.setInvestmentDate(LocalDateTime.now());
        investment.setStatus(Investment.InvestmentStatus.PENDING);

        Investment savedInvestment = investmentRepository.save(investment);

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setUserId(investmentDto.getUserId());
        transaction.setMovieId(investmentDto.getMovieId());
        transaction.setProducerId(investmentDto.getProducerId());
        transaction.setAmount(investmentDto.getAmount());
        transaction.setType(Transaction.TransactionType.INVESTMENT);
        transaction.setPaymentMethod(investmentDto.getPaymentMethod());
        transaction.setDescription("Investment in movie: " + investmentDto.getMovieTitle());
        transaction.setStatus(Transaction.TransactionStatus.PENDING);

        transactionRepository.save(transaction);

        return new InvestmentResponseDto(savedInvestment);
    }

    @Transactional
    public InvestmentResponseDto confirmInvestment(String transactionId) {
        Investment investment = investmentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Investment not found"));

        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Update investment status
        investment.setStatus(Investment.InvestmentStatus.CONFIRMED);
        Investment confirmedInvestment = investmentRepository.save(investment);

        // Update transaction status
        transaction.setStatus(Transaction.TransactionStatus.SUCCESS);
        transaction.setCompletedAt(LocalDateTime.now());
        transactionRepository.save(transaction);

        return new InvestmentResponseDto(confirmedInvestment);
    }

    @Transactional
    public void cancelInvestment(String transactionId, String reason) {
        // Check if investment exists
        Investment investment = investmentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Investment with transaction ID '" + transactionId + "' not found"));

        // Check if investment can be cancelled
        if (investment.getStatus() == Investment.InvestmentStatus.CONFIRMED) {
            throw new IllegalStateException("Cannot cancel a confirmed investment");
        }
        if (investment.getStatus() == Investment.InvestmentStatus.CANCELLED) {
            throw new IllegalStateException("Investment is already cancelled");
        }

        // Update investment status
        investment.setStatus(Investment.InvestmentStatus.CANCELLED);
        investmentRepository.save(investment);

        // Update transaction status if exists, create if doesn't exist
        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElse(null);
        
        if (transaction != null) {
            // Update existing transaction
            transaction.setStatus(Transaction.TransactionStatus.CANCELLED);
            transaction.setFailureReason(reason != null ? reason : "Cancelled by user");
            transaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);
        } else {
            // Create new transaction record for cancellation
            Transaction newTransaction = new Transaction();
            newTransaction.setTransactionId(transactionId);
            newTransaction.setUserId(investment.getUserId());
            newTransaction.setMovieId(investment.getMovieId());
            newTransaction.setProducerId(investment.getProducerId());
            newTransaction.setAmount(investment.getAmount());
            newTransaction.setType(Transaction.TransactionType.INVESTMENT);
            newTransaction.setStatus(Transaction.TransactionStatus.CANCELLED);
            newTransaction.setFailureReason(reason != null ? reason : "Cancelled by user");
            newTransaction.setDescription("Investment cancelled for movie: " + investment.getMovieTitle());
            newTransaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(newTransaction);
        }
    }

    public InvestmentResponseDto getInvestmentById(Long id) {
        Investment investment = investmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Investment not found"));
        return new InvestmentResponseDto(investment);
    }

    public InvestmentResponseDto getInvestmentByTransactionId(String transactionId) {
        Investment investment = investmentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Investment not found"));
        return new InvestmentResponseDto(investment);
    }

    public List<InvestmentResponseDto> getInvestmentsByUser(Long userId) {
        return investmentRepository.findByUserId(userId).stream()
                .map(InvestmentResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<InvestmentResponseDto> getInvestmentsByMovie(Long movieId) {
        return investmentRepository.findByMovieId(movieId).stream()
                .map(InvestmentResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<InvestmentResponseDto> getInvestmentsByProducer(Long producerId) {
        return investmentRepository.findByProducerId(producerId).stream()
                .map(InvestmentResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<InvestmentResponseDto> getConfirmedInvestmentsByMovie(Long movieId) {
        return investmentRepository.findConfirmedInvestmentsByMovie(movieId).stream()
                .map(InvestmentResponseDto::new)
                .collect(Collectors.toList());
    }

    public BigDecimal getTotalInvestmentByMovie(Long movieId) {
        BigDecimal total = investmentRepository.getTotalInvestmentByMovie(movieId);
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal getTotalInvestmentByUser(Long userId) {
        BigDecimal total = investmentRepository.getTotalInvestmentByUser(userId);
        return total != null ? total : BigDecimal.ZERO;
    }

    public Long getInvestorCountByMovie(Long movieId) {
        return investmentRepository.getInvestorCountByMovie(movieId);
    }

    public List<Long> getMovieIdsInvestedByUser(Long userId) {
        return investmentRepository.getMovieIdsInvestedByUser(userId);
    }

    @Transactional
    public void processReturns(Long movieId, BigDecimal totalRevenue) {
        List<Investment> investments = investmentRepository.findUnpaidReturnsByMovie(movieId);
        
        BigDecimal totalInvestment = getTotalInvestmentByMovie(movieId);
        if (totalInvestment.compareTo(BigDecimal.ZERO) == 0) {
            throw new RuntimeException("No investments found for this movie");
        }

        for (Investment investment : investments) {
            // Calculate proportional return
            BigDecimal investmentRatio = investment.getAmount().divide(totalInvestment, 6, BigDecimal.ROUND_HALF_UP);
            BigDecimal returnAmount = totalRevenue.multiply(investmentRatio);
            
            // Update investment with return amount and status
            investment.setActualReturnAmount(returnAmount);
            investment.setReturnPaid(true);
            investment.setReturnPaymentDate(LocalDateTime.now());
            investment.setStatus(Investment.InvestmentStatus.RETURN_PAID);
            investmentRepository.save(investment);

            // Create payout transaction
            String payoutTransactionId = "PAYOUT_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
            Transaction payoutTransaction = new Transaction();
            payoutTransaction.setTransactionId(payoutTransactionId);
            payoutTransaction.setUserId(investment.getUserId());
            payoutTransaction.setMovieId(movieId);
            payoutTransaction.setProducerId(investment.getProducerId());
            payoutTransaction.setAmount(returnAmount);
            payoutTransaction.setType(Transaction.TransactionType.PAYOUT);
            payoutTransaction.setStatus(Transaction.TransactionStatus.SUCCESS);
            payoutTransaction.setDescription("Return payment for movie: " + investment.getMovieTitle());
            payoutTransaction.setCompletedAt(LocalDateTime.now());
            transactionRepository.save(payoutTransaction);
        }
    }

    public List<InvestmentResponseDto> getUnpaidReturns() {
        return investmentRepository.findUnpaidReturns().stream()
                .map(InvestmentResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<InvestmentResponseDto> getUnpaidReturnsByMovie(Long movieId) {
        return investmentRepository.findUnpaidReturnsByMovie(movieId).stream()
                .map(InvestmentResponseDto::new)
                .collect(Collectors.toList());
    }

    public BigDecimal getTotalInvestmentByProducer(Long producerId) {
        BigDecimal total = investmentRepository.getTotalInvestmentByProducer(producerId);
        return total != null ? total : BigDecimal.ZERO;
    }

    public Long getUniqueInvestorCountByProducer(Long producerId) {
        return investmentRepository.getUniqueInvestorCountByProducer(producerId);
    }

    public List<InvestmentResponseDto> getInvestmentsByProducerAndMovie(Long producerId, Long movieId) {
        return investmentRepository.findByProducerIdAndMovieId(producerId, movieId).stream()
                .map(InvestmentResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Map<String, Object> updateMovieCollectionAndDistributeReturns(Long producerId, Long movieId, BigDecimal collectionAmount, LocalDate collectionDate, String notes, Boolean autoDistribute) {
        // Get all confirmed investments for this movie
        List<Investment> investments = investmentRepository.findByMovieIdAndStatus(movieId, Investment.InvestmentStatus.CONFIRMED);
        
        if (investments.isEmpty()) {
            throw new RuntimeException("No confirmed investments found for this movie");
        }
        
        // Calculate total investment amount
        BigDecimal totalInvestment = investments.stream()
            .map(Investment::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Check if there's profit (collection > total investment)
        BigDecimal profit = collectionAmount.subtract(totalInvestment);
        
        Map<String, Object> result = new HashMap<>();
        result.put("movieId", movieId);
        result.put("producerId", producerId);
        result.put("collectionAmount", collectionAmount);
        result.put("totalInvestment", totalInvestment);
        result.put("profit", profit);
        result.put("collectionDate", collectionDate);
        result.put("notes", notes);
        
        if (profit.compareTo(BigDecimal.ZERO) > 0 && autoDistribute) {
            // There's profit, distribute returns proportionally
            int investmentsProcessed = 0;
            
            for (Investment investment : investments) {
                // Calculate investor's share of profit based on their investment proportion
                BigDecimal investmentRatio = investment.getAmount().divide(totalInvestment, 6, BigDecimal.ROUND_HALF_UP);
                BigDecimal investorProfit = profit.multiply(investmentRatio);
                BigDecimal totalReturn = investment.getAmount().add(investorProfit);
                
                // Update investment with return amount and status
                investment.setActualReturnAmount(totalReturn);
                investment.setReturnPaid(true);
                investment.setReturnPaymentDate(LocalDateTime.now());
                investment.setStatus(Investment.InvestmentStatus.RETURN_PAID);
                investmentRepository.save(investment);
                
                // Create payout transaction
                String payoutTransactionId = "PAYOUT_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
                Transaction payoutTransaction = new Transaction();
                payoutTransaction.setTransactionId(payoutTransactionId);
                payoutTransaction.setUserId(investment.getUserId());
                payoutTransaction.setMovieId(movieId);
                payoutTransaction.setProducerId(producerId);
                payoutTransaction.setAmount(totalReturn);
                payoutTransaction.setType(Transaction.TransactionType.PAYOUT);
                payoutTransaction.setStatus(Transaction.TransactionStatus.COMPLETED);
                payoutTransaction.setTransactionDate(LocalDateTime.now());
                transactionRepository.save(payoutTransaction);
                
                investmentsProcessed++;
            }
            
            result.put("returnsDistributed", true);
            result.put("investmentsProcessed", investmentsProcessed);
            result.put("message", "Collection updated and returns distributed successfully");
        } else if (profit.compareTo(BigDecimal.ZERO) <= 0) {
            result.put("returnsDistributed", false);
            result.put("message", "Collection updated but no profit to distribute. Loss: " + profit.abs());
        } else {
            result.put("returnsDistributed", false);
            result.put("message", "Collection updated. Auto-distribution disabled.");
        }
        
        result.put("processedAt", LocalDateTime.now());
        return result;
    }

    public Map<String, Object> processReturnsForProducer(Long producerId, Long movieId, BigDecimal totalRevenue, String notes) {
        // Verify producer owns this movie
        List<Investment> investments = investmentRepository.findByProducerIdAndMovieId(producerId, movieId);
        if (investments.isEmpty()) {
            throw new RuntimeException("No investments found for producer " + producerId + " and movie " + movieId);
        }

        // Process returns for this specific movie
        processReturns(movieId, totalRevenue);

        // Prepare response data
        Map<String, Object> result = new HashMap<>();
        result.put("movieId", movieId);
        result.put("producerId", producerId);
        result.put("totalRevenue", totalRevenue);
        result.put("investmentsProcessed", investments.size());
        result.put("notes", notes);
        result.put("processedAt", LocalDateTime.now());
        
        return result;
    }

    @Transactional
    public Map<String, Object> processReturnsForAllProducerMovies(Long producerId, Map<String, Object> returnData) {
        List<Investment> allInvestments = investmentRepository.findByProducerId(producerId);
        if (allInvestments.isEmpty()) {
            throw new RuntimeException("No investments found for producer " + producerId);
        }

        // Group investments by movie
        Map<Long, List<Investment>> investmentsByMovie = allInvestments.stream()
                .collect(Collectors.groupingBy(Investment::getMovieId));

        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> processedMovies = new ArrayList<>();
        BigDecimal totalRevenueProcessed = BigDecimal.ZERO;
        int totalInvestmentsProcessed = 0;

        // Process returns for each movie
        for (Map.Entry<Long, List<Investment>> entry : investmentsByMovie.entrySet()) {
            Long movieId = entry.getKey();
            List<Investment> movieInvestments = entry.getValue();
            
            // Get revenue for this movie from returnData
            String movieKey = "movie_" + movieId;
            if (returnData.containsKey(movieKey)) {
                BigDecimal movieRevenue = new BigDecimal(returnData.get(movieKey).toString());
                
                // Process returns for this movie
                processReturns(movieId, movieRevenue);
                
                Map<String, Object> movieResult = new HashMap<>();
                movieResult.put("movieId", movieId);
                movieResult.put("revenue", movieRevenue);
                movieResult.put("investmentsProcessed", movieInvestments.size());
                movieResult.put("movieTitle", movieInvestments.get(0).getMovieTitle());
                processedMovies.add(movieResult);
                
                totalRevenueProcessed = totalRevenueProcessed.add(movieRevenue);
                totalInvestmentsProcessed += movieInvestments.size();
            }
        }

        result.put("producerId", producerId);
        result.put("moviesProcessed", processedMovies);
        result.put("totalMovies", processedMovies.size());
        result.put("totalRevenueProcessed", totalRevenueProcessed);
        result.put("totalInvestmentsProcessed", totalInvestmentsProcessed);
        result.put("processedAt", LocalDateTime.now());
        
        return result;
    }

    public Map<String, Object> getReturnSummaryForProducer(Long producerId) {
        List<Investment> allInvestments = investmentRepository.findByProducerId(producerId);
        
        if (allInvestments.isEmpty()) {
            Map<String, Object> summary = new HashMap<>();
            summary.put("producerId", producerId);
            summary.put("totalInvestments", 0);
            summary.put("totalInvestmentAmount", BigDecimal.ZERO);
            summary.put("paidReturns", 0);
            summary.put("unpaidReturns", 0);
            summary.put("totalReturnsPaid", BigDecimal.ZERO);
            summary.put("pendingReturns", BigDecimal.ZERO);
            return summary;
        }

        // Calculate summary statistics
        long totalInvestments = allInvestments.size();
        BigDecimal totalInvestmentAmount = allInvestments.stream()
                .map(Investment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        long paidReturns = allInvestments.stream()
                .mapToLong(inv -> inv.getReturnPaid() != null && inv.getReturnPaid() ? 1 : 0)
                .sum();
        
        long unpaidReturns = totalInvestments - paidReturns;
        
        BigDecimal totalReturnsPaid = allInvestments.stream()
                .filter(inv -> inv.getReturnPaid() != null && inv.getReturnPaid())
                .map(inv -> inv.getActualReturnAmount() != null ? inv.getActualReturnAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Group by movie for detailed breakdown
        Map<Long, List<Investment>> investmentsByMovie = allInvestments.stream()
                .collect(Collectors.groupingBy(Investment::getMovieId));
        
        List<Map<String, Object>> movieSummaries = new ArrayList<>();
        for (Map.Entry<Long, List<Investment>> entry : investmentsByMovie.entrySet()) {
            Long movieId = entry.getKey();
            List<Investment> movieInvestments = entry.getValue();
            
            Map<String, Object> movieSummary = new HashMap<>();
            movieSummary.put("movieId", movieId);
            movieSummary.put("movieTitle", movieInvestments.get(0).getMovieTitle());
            movieSummary.put("totalInvestments", movieInvestments.size());
            movieSummary.put("totalAmount", movieInvestments.stream()
                    .map(Investment::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            movieSummary.put("paidReturns", movieInvestments.stream()
                    .mapToLong(inv -> inv.getReturnPaid() != null && inv.getReturnPaid() ? 1 : 0)
                    .sum());
            movieSummary.put("unpaidReturns", movieInvestments.size() - (long) movieSummary.get("paidReturns"));
            
            movieSummaries.add(movieSummary);
        }

        Map<String, Object> summary = new HashMap<>();
        summary.put("producerId", producerId);
        summary.put("producerName", allInvestments.get(0).getProducerName());
        summary.put("totalInvestments", totalInvestments);
        summary.put("totalInvestmentAmount", totalInvestmentAmount);
        summary.put("paidReturns", paidReturns);
        summary.put("unpaidReturns", unpaidReturns);
        summary.put("totalReturnsPaid", totalReturnsPaid);
        summary.put("movieBreakdown", movieSummaries);
        summary.put("generatedAt", LocalDateTime.now());
        
        return summary;
    }
}
