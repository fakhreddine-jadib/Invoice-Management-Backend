package com.emsi.invoicemanagementapp.service;

import com.emsi.invoicemanagementapp.dto.ClientRequestDTO;
import com.emsi.invoicemanagementapp.model.Client;
import com.emsi.invoicemanagementapp.repository.ClientRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Client> getClientWithInvoices(Long id) {
        Optional<Client> clientOpt = clientRepository.findById(id);
        clientOpt.ifPresent(client -> Hibernate.initialize(client.getInvoices()));
        return clientOpt;
    }

    @Transactional
    public Client createClient(ClientRequestDTO clientRequest) {
        Client client = new Client();
        client.setCompanyName(clientRequest.getCompanyName());
        client.setIceFiscalId(clientRequest.getIceFiscalId());
        client.setAddress(clientRequest.getAddress());
        client.setEmail(clientRequest.getEmail());
        client.setPhone(clientRequest.getPhone());
        client.setDefaultPaymentMethod(clientRequest.getDefaultPaymentMethod());
        client.setTaxStatus(clientRequest.getTaxStatus()); // <-- Add this line
        return clientRepository.save(client);
    }

    @Transactional
    public Client updateClient(Long id, ClientRequestDTO clientRequest) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));

        client.setCompanyName(clientRequest.getCompanyName());
        client.setIceFiscalId(clientRequest.getIceFiscalId());
        client.setAddress(clientRequest.getAddress());
        client.setEmail(clientRequest.getEmail());
        client.setPhone(clientRequest.getPhone());
        client.setDefaultPaymentMethod(clientRequest.getDefaultPaymentMethod());
        client.setTaxStatus(clientRequest.getTaxStatus()); // <-- Add this line
        return clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}