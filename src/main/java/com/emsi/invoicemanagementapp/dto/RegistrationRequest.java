package com.emsi.invoicemanagementapp.dto;

import com.emsi.invoicemanagementapp.enums.Role;
import lombok.Data;

@Data
public class RegistrationRequest {
    private String username;
    private String email;
    private String password;
    private Role role;
}