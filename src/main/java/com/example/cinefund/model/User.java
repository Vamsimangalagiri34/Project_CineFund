package com.example.cinefund.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String userId;
    
    private String name;
    private String email;
    
    private Long accountNumber;
   
    private String ifscCode;

    private String role = "USER";
    private String password;


    public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	// Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getAccountNumber() { return accountNumber; }
    public void setAccountNumber(Long accountNumber) { this.accountNumber = accountNumber; }

    public String getIfscCode() { return ifscCode; }
    public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}

//Register
//{
//	  "userId": "1",
//	  "name": "uma",
//	  "email":"uma@example.com",
//	  "accountNumber":479027,
//	  "ifscCode":"hns7642j2w",
//	  "password": "123797",
//	  "role":"USER"
//	}
//
//Login
//userId
//name
//password

