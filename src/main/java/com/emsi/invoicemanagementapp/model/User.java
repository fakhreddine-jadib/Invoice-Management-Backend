package com.emsi.invoicemanagementapp.model;

import com.emsi.invoicemanagementapp.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Represents the User entity for authentication and authorization.
 */
@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}