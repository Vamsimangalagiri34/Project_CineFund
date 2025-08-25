package com.example.demo.repository;

//import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Funding;
//import com.example.demo.service.List;

public interface FundingRepo extends JpaRepository<Funding,Long> {

//java.util.List<Funding> findByinvestorId(Long investorId);
//java.util.List<Funding> findByprojectId(Long projectId);
//java.util.List<Funding> findByprojectIdAndproducerId(Long projectId, Long producerId);
	
	

}
