package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

import com.example.demo.model.Funding;
import com.example.demo.repository.FundingRepo;

@Service
public class FundingService {
	
	@Autowired
	private FundingRepo fundingRepo;
	
//	public List<Funding> getAllInvestmentsOfInvestor(Long investorId){
//		return fundingRepo.findByinvestorId(investorId);
//	}
//	
//	public List<Funding> getAllInvestorsOfProject(Long projectId,Long producerId){
//		return fundingRepo.findByprojectIdAndproducerId(projectId, producerId);
//	}
	public Optional<Funding> getTransaction(Long transactionId){
		return fundingRepo.findById(transactionId);
	}
	public Funding addFunding(Funding funding) {
		return fundingRepo.save(funding);
	}
	
	
}
