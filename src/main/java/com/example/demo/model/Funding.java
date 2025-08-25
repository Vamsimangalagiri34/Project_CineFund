package com.example.demo.model;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name="Transactions")
public class Funding {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long transactionId;
	
	private long projectId;
	private long investorId;
	private long producerId;
	private double amount;
	private double returns;
	private double percentage;
	private LocalDate transactionDate;
	
//	public long getTransactionId() {
//		return transactionId;
//	}
//	public void setTransactionId(long transactionId) {
//		this.transactionId = transactionId;
//	}
	public long getProjectId() {
		return projectId;
	}
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
	public long getInvestorId() {
		return investorId;
	}
	public void setInvestorId(long investorId) {
		this.investorId = investorId;
	}
	public long getProducerId() {
		return producerId;
	}
	public void setProducerId(long producerId) {
		this.producerId = producerId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getReturns() {
		return returns;
	}
	public void setReturns(double returns) {
		this.returns = returns;
	}
	public double getPercentage() {
		return percentage;
	}
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
	public LocalDate getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(LocalDate transactionDate) {
		this.transactionDate = transactionDate;
	}
	

}
