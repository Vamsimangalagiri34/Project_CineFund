package com.example.cinefund.service;

import com.example.cinefund.model.User;
import com.example.cinefund.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

//    public User registerUser(User user) {
//        return userRepository.save(user);
//    }

@Autowired
private BCryptPasswordEncoder passwordEncoder;

public User registerUser(User user) {
    Optional<User> existingUser = userRepository.findByUserId(user.getUserId());
    if (existingUser.isPresent()) {
        throw new RuntimeException("User ID already exists");
    }

    // Encrypt the password before saving
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    return userRepository.save(user);
}


public Optional<User> loginUser(String userId, String rawPassword, String name) {
    Optional<User> userOpt = userRepository.findByUserId(userId);
    if (userOpt.isPresent()) {
        User user = userOpt.get();
        boolean passwordMatches = passwordEncoder.matches(rawPassword, user.getPassword());
        boolean nameMatches = user.getName().equalsIgnoreCase(name);

        if (passwordMatches && nameMatches) {
            return Optional.of(user);
        }
    }
    return Optional.empty();
}

public Optional<User> getUserById(Long id) {
    return userRepository.findById(id);
}

public List<User> getAll() {
	return userRepository.findAll();
	
}

public Optional<User> updateUser(Long id, User updatedUser) {
    Optional<User> existingUserOpt = userRepository.findById(id);
    if (existingUserOpt.isPresent()) {
        User existingUser = existingUserOpt.get();

        // Update fields
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setAccountNumber(updatedUser.getAccountNumber());
        existingUser.setIfscCode(updatedUser.getIfscCode());
        existingUser.setRole(updatedUser.getRole());

        // If password is updated, encode it
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        userRepository.save(existingUser);
        return Optional.of(existingUser);
    }
    return Optional.empty();
}



}
