package com.cinefund.userservice.controller;

import com.cinefund.userservice.dto.InvestmentDto;
import com.cinefund.userservice.dto.InvestmentResponseDto;
import com.cinefund.userservice.dto.UserLoginDto;
import com.cinefund.userservice.dto.UserRegistrationDto;
import com.cinefund.userservice.dto.UserResponseDto;
import com.cinefund.userservice.entity.User;
import com.cinefund.userservice.service.InvestmentService;
import com.cinefund.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@Tag(name = "User Management", description = "APIs for user registration, authentication, and profile management")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private InvestmentService investmentService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Register a new user (investor, producer, or admin)")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        try {
            UserResponseDto user = userService.registerUser(registrationDto);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User registered successfully");
            response.put("user", user);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDto loginDto) {
        System.out.println("Received login request: " + loginDto.getUsernameOrEmail() + " / " + (loginDto.getPassword() != null ? "***" : "null"));
        try {
            String token = userService.loginUser(loginDto);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve user details by user ID")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            UserResponseDto user = userService.getUserById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("user", user);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Get user by username", description = "Retrieve user details by username")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        try {
            UserResponseDto user = userService.getUserByUsername(username);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("user", user);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve all users in the system")
    public ResponseEntity<?> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("users", users);
        response.put("count", users.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/role/{role}")
    @Operation(summary = "Get users by role", description = "Retrieve users by their role (INVESTOR, PRODUCER, ADMIN)")
    public ResponseEntity<?> getUsersByRole(@PathVariable User.Role role) {
        List<UserResponseDto> users = userService.getUsersByRole(role);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("users", users);
        response.put("count", users.size());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Update user profile information")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserRegistrationDto updateDto) {
        try {
            UserResponseDto user = userService.updateUser(id, updateDto);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User updated successfully");
            response.put("user", user);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate user", description = "Deactivate a user account")
    public ResponseEntity<?> deactivateUser(@PathVariable Long id) {
        try {
            userService.deactivateUser(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User deactivated successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate user", description = "Activate a user account")
    public ResponseEntity<?> activateUser(@PathVariable Long id) {
        try {
            userService.activateUser(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User activated successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}/wallet")
    @Operation(summary = "Update wallet balance", description = "Add or subtract amount from user's wallet")
    public ResponseEntity<?> updateWalletBalance(@PathVariable Long id, @RequestParam BigDecimal amount) {
        try {
            UserResponseDto user = userService.updateWalletBalance(id, amount);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Wallet balance updated successfully");
            response.put("user", user);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search users", description = "Search users by name or username")
    public ResponseEntity<?> searchUsers(@RequestParam String keyword) {
        List<UserResponseDto> users = userService.searchUsers(keyword);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("users", users);
        response.put("count", users.size());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{userId}/invest")
    @Operation(summary = "Invest in a movie", description = "Allow user to invest in a movie project")
    public ResponseEntity<?> investInMovie(@PathVariable Long userId, @Valid @RequestBody InvestmentDto investmentDto) {
        try {
            InvestmentResponseDto investment = investmentService.investInMovie(userId, investmentDto);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Investment successful");
            response.put("investment", investment);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
