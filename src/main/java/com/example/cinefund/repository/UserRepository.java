package com.example.cinefund.repository;

import com.example.cinefund.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //Optional<User> findByUserIdAndEmail(String userId, String email);
    Optional<User> findByUserId(String userId);
    Optional<User> findByUserIdAndName(String userId, String name);
    


}
