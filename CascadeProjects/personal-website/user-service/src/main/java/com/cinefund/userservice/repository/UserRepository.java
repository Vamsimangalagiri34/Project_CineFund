package com.cinefund.userservice.repository;

import com.cinefund.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByName(String name);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByNameOrEmail(String name, String email);
    
    Boolean existsByName(String name);
    
    Boolean existsByEmail(String email);
    
    List<User> findByRole(User.Role role);
    
    List<User> findByIsActiveTrue();
    
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.isActive = true")
    List<User> findActiveUsersByRole(@Param("role") User.Role role);
    
    @Query("SELECT u FROM User u WHERE (u.firstName LIKE %:keyword% OR u.lastName LIKE %:keyword% OR u.name LIKE %:keyword%) AND u.isActive = true")
    List<User> searchActiveUsers(@Param("keyword") String keyword);
}
