package com.emsi.invoicemanagementapp.repository;

import com.emsi.invoicemanagementapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // This is a crucial method for Spring Security.
    // It will be used to load a user by their username during authentication.
    Optional<User> findByUsername(String username);

}