package com.example.cinefund.controller;

import com.example.cinefund.model.User;
import com.example.cinefund.service.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cinefund/user/v")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

 
    
    @PostMapping("/login")
    public String loginUser(@RequestBody User users) {
        Optional<User> user = userService.loginUser(
            users.getUserId(),
            users.getPassword(),
            users.getName()
            
        );
        return user.isPresent() ? "Login successful" : "Invalid credentials";
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> user = userService.updateUser(id, updatedUser);
        return user.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @GetMapping("/users")
    public ResponseEntity<List<User>> retriveall(){
    	return new ResponseEntity<>(userService.getAll(),HttpStatus.OK);
    }


}
