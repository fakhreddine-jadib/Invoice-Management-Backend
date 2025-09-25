package com.emsi.invoicemanagementapp.repository;

import com.emsi.invoicemanagementapp.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Client entity.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    // Spring Data JPA will automatically provide methods like:
    // - save(Client client)
    // - findById(Long id)
    // - findAll()
    // - deleteById(Long id)
    // ... and many more.

    // You can also define custom query methods here if needed.
    // For example, to find a client by their email:
    // Optional<Client> findByEmail(String email);
}