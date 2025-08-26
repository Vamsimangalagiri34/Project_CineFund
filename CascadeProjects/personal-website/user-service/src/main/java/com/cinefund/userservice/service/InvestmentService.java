package com.cinefund.userservice.service;

import com.cinefund.userservice.dto.InvestmentDto;
import com.cinefund.userservice.dto.InvestmentResponseDto;
import com.cinefund.userservice.entity.User;
import com.cinefund.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class InvestmentService {

    @Autowired
    private UserRepository userRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String FUNDING_SERVICE_URL = "http://localhost:8083";
    private final String MOVIE_SERVICE_URL = "http://localhost:8082";

    public InvestmentResponseDto investInMovie(Long investorId, InvestmentDto investmentDto) {
        // 1. Validate investor exists and has sufficient balance
        User investor = userRepository.findById(investorId)
                .orElseThrow(() -> new RuntimeException("Investor not found"));

        if (investor.getWalletBalance().compareTo(investmentDto.getAmount()) < 0) {
            throw new RuntimeException("Insufficient wallet balance");
        }

        // 2. Get movie details to find producer
        Map<String, Object> movieDetails = getMovieDetails(investmentDto.getMovieId());
        if (movieDetails == null) {
            throw new RuntimeException("Movie not found");
        }

        Long producerId = ((Number) movieDetails.get("producerId")).longValue();
        String movieTitle = (String) movieDetails.get("title");

        // 3. Get producer details
        User producer = userRepository.findById(producerId)
                .orElseThrow(() -> new RuntimeException("Producer not found"));

        // 4. Create investment record in Funding Service
        Map<String, Object> investmentData = new HashMap<>();
        investmentData.put("investorId", investorId);
        investmentData.put("investorName", investor.getFirstName() + " " + investor.getLastName());
        investmentData.put("movieId", investmentDto.getMovieId());
        investmentData.put("movieTitle", movieTitle);
        investmentData.put("producerId", producerId);
        investmentData.put("producerName", producer.getFirstName() + " " + producer.getLastName());
        investmentData.put("amount", investmentDto.getAmount());
        investmentData.put("notes", investmentDto.getNotes());

        InvestmentResponseDto investmentResponse = createInvestmentRecord(investmentData);

        // 5. Deduct amount from investor's wallet
        BigDecimal newBalance = investor.getWalletBalance().subtract(investmentDto.getAmount());
        investor.setWalletBalance(newBalance);
        userRepository.save(investor);

        // 6. Update movie raised amount
        updateMovieRaisedAmount(investmentDto.getMovieId(), investmentDto.getAmount());

        return investmentResponse;
    }

    private Map<String, Object> getMovieDetails(Long movieId) {
        try {
            String url = MOVIE_SERVICE_URL + "/api/movies/" + movieId;
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                if ((Boolean) responseBody.get("success")) {
                    return (Map<String, Object>) responseBody.get("movie");
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get movie details: " + e.getMessage());
        }
    }

    private InvestmentResponseDto createInvestmentRecord(Map<String, Object> investmentData) {
        try {
            String url = FUNDING_SERVICE_URL + "/api/funding/invest";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(investmentData, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                if ((Boolean) responseBody.get("success")) {
                    Map<String, Object> investment = (Map<String, Object>) responseBody.get("investment");
                    return mapToInvestmentResponseDto(investment);
                }
            }
            throw new RuntimeException("Failed to create investment record");
        } catch (Exception e) {
            throw new RuntimeException("Failed to create investment record: " + e.getMessage());
        }
    }

    private void updateMovieRaisedAmount(Long movieId, BigDecimal amount) {
        try {
            String url = MOVIE_SERVICE_URL + "/api/movies/" + movieId + "/funding?amount=" + amount;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> request = new HttpEntity<>(headers);
            restTemplate.exchange(url, HttpMethod.PUT, request, Map.class);
        } catch (Exception e) {
            // Log error but don't fail the investment
            System.err.println("Failed to update movie raised amount: " + e.getMessage());
        }
    }

    private InvestmentResponseDto mapToInvestmentResponseDto(Map<String, Object> investment) {
        InvestmentResponseDto dto = new InvestmentResponseDto();
        dto.setId(((Number) investment.get("id")).longValue());
        dto.setInvestorId(((Number) investment.get("investorId")).longValue());
        dto.setInvestorName((String) investment.get("investorName"));
        dto.setMovieId(((Number) investment.get("movieId")).longValue());
        dto.setMovieTitle((String) investment.get("movieTitle"));
        dto.setProducerId(((Number) investment.get("producerId")).longValue());
        dto.setProducerName((String) investment.get("producerName"));
        dto.setAmount(new BigDecimal(investment.get("amount").toString()));
        dto.setNotes((String) investment.get("notes"));
        dto.setStatus((String) investment.get("status"));
        return dto;
    }
}
