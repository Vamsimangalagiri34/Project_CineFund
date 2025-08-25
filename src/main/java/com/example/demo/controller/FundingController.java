package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Funding;
import com.example.demo.service.FundingService;

@RestController
@RequestMapping("/api/v1/funding")
public class FundingController {
	
	@Autowired FundingService fundingService;
	
	/*@GetMapping("/getProjects/{investor_id}")
	public void getProjects(@PathVariable Long investorId) {
		fundingService.getAllInvestmentsOfInvestor(investorId);
	}
	
	@GetMapping("/getProjects/{investor_id}")
	public void getInvestors(@PathVariable Long investorId) {
		fundingService.getAllInvestmentsOfInvestor(investorId);
	}*/
	
//	@GetMapping("/{id}")
//	public ResponseEntity<Optional<Funding>> getTransactionById(@PathVariable Long transactionId) {
//		return new ResponseEntity<Optional<Funding>>(fundingService.getTransaction(transactionId),HttpStatus.OK);
//	}
	@GetMapping("/{id}")
	public ResponseEntity<Optional<Funding>> getTransactionById(@PathVariable("id") Long id) {
	    return new ResponseEntity<>(fundingService.getTransaction(id), HttpStatus.OK);
	}

	
	@PostMapping
	public ResponseEntity<Funding> addFunding(@RequestBody Funding funding){
		return new ResponseEntity<Funding>(fundingService.addFunding(funding),HttpStatus.CREATED);
	}
}