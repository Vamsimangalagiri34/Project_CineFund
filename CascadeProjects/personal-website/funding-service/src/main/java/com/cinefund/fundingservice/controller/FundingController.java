package com.cinefund.fundingservice.controller;

import com.cinefund.fundingservice.dto.InvestmentDto;
import com.cinefund.fundingservice.dto.InvestmentResponseDto;
import com.cinefund.fundingservice.dto.ReturnProcessingDto;
import com.cinefund.fundingservice.dto.MovieCollectionUpdateDto;
import com.cinefund.fundingservice.service.FundingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/funding")
@CrossOrigin(origins = "*")
@Tag(name = "Funding Management", description = "APIs for investment and funding operations")
public class FundingController {

    @Autowired
    private FundingService fundingService;

    @PostMapping("/invest")
    @Operation(summary = "Create investment", description = "Create a new investment in a movie")
    public ResponseEntity<?> createInvestment(@Valid @RequestBody InvestmentDto investmentDto) {
        try {
            InvestmentResponseDto investment = fundingService.createInvestment(investmentDto);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Investment created successfully");
            response.put("investment", investment);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/confirm/{transactionId}")
    @Operation(summary = "Confirm investment", description = "Confirm a pending investment")
    public ResponseEntity<?> confirmInvestment(@PathVariable("transactionId") String transactionId) {
        try {
            InvestmentResponseDto investment = fundingService.confirmInvestment(transactionId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Investment confirmed successfully");
            response.put("investment", investment);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/cancel/{transactionId}")
    @Operation(summary = "Cancel investment", description = "Cancel a pending investment by transaction ID")
    public ResponseEntity<Map<String, Object>> cancelInvestment(
            @PathVariable("transactionId") String transactionId,
            @RequestParam(value = "reason", required = false) String reason) {

        Map<String, Object> response = new HashMap<>();
        try {
            fundingService.cancelInvestment(transactionId, reason);
            response.put("success", true);
            response.put("message", "Investment cancelled successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IllegalStateException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", "Failed to cancel investment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping("/investment/{id}")
    @Operation(summary = "Get investment by ID", description = "Retrieve investment details by ID")
    public ResponseEntity<?> getInvestmentById(@PathVariable("id") Long id) {
        try {
            InvestmentResponseDto investment = fundingService.getInvestmentById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("investment", investment);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/transaction/{transactionId}")
    @Operation(summary = "Get investment by transaction ID", description = "Retrieve investment details by transaction ID")
    public ResponseEntity<?> getInvestmentByTransactionId(@PathVariable("transactionId") String transactionId) {
        try {
            InvestmentResponseDto investment = fundingService.getInvestmentByTransactionId(transactionId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("investment", investment);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get investments by user", description = "Retrieve all investments made by a user")
    public ResponseEntity<?> getInvestmentsByUser(@PathVariable("userId") Long userId) {
        List<InvestmentResponseDto> investments = fundingService.getInvestmentsByUser(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("investments", investments);
        response.put("count", investments.size());
        response.put("totalAmount", fundingService.getTotalInvestmentByUser(userId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/movie/{movieId}")
    @Operation(summary = "Get investments by movie", description = "Retrieve all investments for a movie")
    public ResponseEntity<?> getInvestmentsByMovie(@PathVariable("movieId") Long movieId) {
        List<InvestmentResponseDto> investments = fundingService.getInvestmentsByMovie(movieId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("investments", investments);
        response.put("count", investments.size());
        response.put("totalAmount", fundingService.getTotalInvestmentByMovie(movieId));
        response.put("investorCount", fundingService.getInvestorCountByMovie(movieId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/movie/{movieId}/confirmed")
    @Operation(summary = "Get confirmed investments by movie", description = "Retrieve confirmed investments for a movie")
    public ResponseEntity<?> getConfirmedInvestmentsByMovie(@PathVariable("movieId") Long movieId) {
        List<InvestmentResponseDto> investments = fundingService.getConfirmedInvestmentsByMovie(movieId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("investments", investments);
        response.put("count", investments.size());
        response.put("totalAmount", fundingService.getTotalInvestmentByMovie(movieId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/producer/{producerId}")
    @Operation(summary = "Get investments by producer", description = "Retrieve all investments for a producer's movies")
    public ResponseEntity<?> getInvestmentsByProducer(@PathVariable("producerId") Long producerId) {
        List<InvestmentResponseDto> investments = fundingService.getInvestmentsByProducer(producerId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("investments", investments);
        response.put("count", investments.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/movies")
    @Operation(summary = "Get movies invested by user", description = "Get list of movie IDs that user has invested in")
    public ResponseEntity<?> getMovieIdsInvestedByUser(@PathVariable("userId") Long userId) {
        List<Long> movieIds = fundingService.getMovieIdsInvestedByUser(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("movieIds", movieIds);
        response.put("count", movieIds.size());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/returns/{movieId}")
    @Operation(summary = "Process returns", description = "Process returns for investors of a movie")
    public ResponseEntity<?> processReturns(@PathVariable("movieId") Long movieId, @RequestParam BigDecimal totalRevenue) {
        try {
            fundingService.processReturns(movieId, totalRevenue);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Returns processed successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/returns/unpaid")
    @Operation(summary = "Get unpaid returns", description = "Get all investments with unpaid returns")
    public ResponseEntity<?> getUnpaidReturns() {
        List<InvestmentResponseDto> investments = fundingService.getUnpaidReturns();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("investments", investments);
        response.put("count", investments.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/returns/unpaid/movie/{movieId}")
    @Operation(summary = "Get unpaid returns by movie", description = "Get unpaid returns for a specific movie")
    public ResponseEntity<?> getUnpaidReturnsByMovie(@PathVariable("movieId") Long movieId) {
        List<InvestmentResponseDto> investments = fundingService.getUnpaidReturnsByMovie(movieId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("investments", investments);
        response.put("count", investments.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/producer/{producerId}/investors")
    @Operation(summary = "Get all investors for producer's movies", description = "Get detailed list of investors for all movies by a producer")
    public ResponseEntity<?> getInvestorsForProducer(@PathVariable("producerId") Long producerId) {
        List<InvestmentResponseDto> investments = fundingService.getInvestmentsByProducer(producerId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("investments", investments);
        response.put("count", investments.size());
        response.put("totalInvestmentAmount", fundingService.getTotalInvestmentByProducer(producerId));
        response.put("uniqueInvestorCount", fundingService.getUniqueInvestorCountByProducer(producerId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/producer/{producerId}/movie/{movieId}/investors")
    @Operation(summary = "Get investors for specific movie by producer", description = "Get detailed list of investors for a specific movie by producer")
    public ResponseEntity<?> getInvestorsForProducerMovie(@PathVariable("producerId") Long producerId, @PathVariable("movieId") Long movieId) {
        List<InvestmentResponseDto> investments = fundingService.getInvestmentsByProducerAndMovie(producerId, movieId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("investments", investments);
        response.put("count", investments.size());
        response.put("totalAmount", fundingService.getTotalInvestmentByMovie(movieId));
        response.put("investorCount", fundingService.getInvestorCountByMovie(movieId));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/producer/{producerId}/movie/{movieId}/collection")
    @Operation(summary = "Update movie collection and distribute returns", description = "Producer updates movie collection at specific date and automatically distributes returns if profitable")
    public ResponseEntity<?> updateMovieCollection(
            @PathVariable("producerId") Long producerId, 
            @PathVariable("movieId") Long movieId, 
            @Valid @RequestBody MovieCollectionUpdateDto collectionData) {
        try {
            Map<String, Object> result = fundingService.updateMovieCollectionAndDistributeReturns(
                producerId, movieId, collectionData.getCollectionAmount(), 
                collectionData.getCollectionDate(), collectionData.getNotes(), 
                collectionData.getAutoDistributeReturns());
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", result);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/producer/{producerId}/movie/{movieId}/returns")
    @Operation(summary = "Process returns for producer's movie", description = "Producer processes returns for investors of their movie")
    public ResponseEntity<?> processReturnsForProducerMovie(
            @PathVariable("producerId") Long producerId, 
            @PathVariable("movieId") Long movieId, 
            @Valid @RequestBody ReturnProcessingDto returnData) {
        try {
            Map<String, Object> result = fundingService.processReturnsForProducer(producerId, movieId, returnData.getTotalRevenue(), returnData.getNotes());
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Returns processed successfully for all investors");
            response.put("data", result);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/producer/{producerId}/returns/bulk")
    @Operation(summary = "Process returns for all producer's movies", description = "Producer processes returns for all their movies")
    public ResponseEntity<?> processReturnsForAllProducerMovies(
            @PathVariable("producerId") Long producerId,
            @RequestBody Map<String, Object> returnData) {
        try {
            Map<String, Object> result = fundingService.processReturnsForAllProducerMovies(producerId, returnData);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Returns processed successfully for all movies");
            response.put("data", result);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/producer/{producerId}/returns/summary")
    @Operation(summary = "Get return summary for producer", description = "Get summary of returns paid/unpaid for producer's movies")
    public ResponseEntity<?> getReturnSummaryForProducer(@PathVariable("producerId") Long producerId) {
        Map<String, Object> summary = fundingService.getReturnSummaryForProducer(producerId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("summary", summary);
        return ResponseEntity.ok(response);
    }
}
