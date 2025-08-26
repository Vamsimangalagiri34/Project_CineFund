package com.cinefund.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class UserLoginDto {
    @NotBlank(message = "Username or email is required")
    @JsonProperty("usernameOrEmail")
    private String usernameOrEmail;

    @NotBlank(message = "Password is required")
    @JsonProperty("password")
    private String password;

    // Constructors
    public UserLoginDto() {}

    public UserLoginDto(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }

    // Getters and Setters
    public String getUsernameOrEmail() { return usernameOrEmail; }
    public void setUsernameOrEmail(String usernameOrEmail) { this.usernameOrEmail = usernameOrEmail; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
