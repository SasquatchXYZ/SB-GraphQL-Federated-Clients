package com.playground.service;

import com.playground.domain.ClientInput;
import com.playground.entity.Client;
import com.playground.entity.ClientAccountId;
import com.playground.exceptions.ClientAlreadyExistsException;
import com.playground.exceptions.ClientNotFoundException;
import com.playground.repo.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    ClientRepo repo;

    public List<Client> getClients() {
        return updateClientList(repo.findAll());
    }

    public List<Client> getClients(Integer accountId) {
        return updateClientList(repo.findByClientIdAccountIdAccountId(accountId));
    }

    public Client save(ClientInput clientInput) {
        ClientAccountId clientAccountId = ClientAccountId.builder()
                .accountId(clientInput.getAccountId())
                .clientId(clientInput.getClientId())
                .build();

        Client client = Client.builder()
                .clientIdAccountId(clientAccountId)
                .clientId(clientInput.getClientId())
                .accountId(clientInput.getAccountId())
                .firstName(clientInput.getFirstName())
                .lastName(clientInput.getLastName())
                .middleName(clientInput.getMiddleName())
                .country(clientInput.getCountry())
                .build();

        if (repo.findByClientIdAccountId(clientAccountId).isEmpty()) {
            repo.save(client);
        } else {
            throw new ClientAlreadyExistsException("Client " + clientInput.getClientId() + " already exists for Account " + clientInput.getAccountId());
        }
        return client;
    }

    public Boolean delete(Integer clientId, Integer accountId) {
        ClientAccountId clientAccountId = ClientAccountId.builder()
                .accountId(accountId)
                .clientId(clientId)
                .build();

        if (!repo.findByClientIdAccountId(clientAccountId).isEmpty()) {
            repo.delete(repo.findByClientIdAccountId(clientAccountId).get(0));
            return true;
        } else {
            throw new ClientNotFoundException("Client " + clientId + " not exists for Account " + accountId);
        }
    }

    private List<Client> updateClientList(List<Client> clientStream) {
        return clientStream.stream().map(client -> {
            client.setClientId(client.getClientIdAccountId().getClientId());
            client.setAccountId(client.getClientIdAccountId().getAccountId());
            return client;
        }).collect(Collectors.toList());
    }
}
