package com.emsi.invoicemanagementapp.repository;

import com.emsi.invoicemanagementapp.model.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingRepository extends JpaRepository<Setting, String> {
    // Spring Data JPA provides all the necessary methods like findById, save, etc.
}