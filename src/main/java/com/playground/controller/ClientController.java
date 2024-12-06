package com.playground.controller;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.DgsEntityFetcher;
import com.playground.domain.Account;
import com.playground.domain.ClientInput;
import com.playground.entity.Client;
import com.playground.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@DgsComponent
@Controller
@Slf4j
public class ClientController {

    @Autowired
    ClientService clientService;

    @QueryMapping(name = "clients")
    public List<Client> getClients() {
        log.info("Getting clients");
        return clientService.getClients();
    }

    @MutationMapping
    public Client addClient(@Argument("client") ClientInput clientInput) {
        log.info("Adding client");
        return clientService.save(clientInput);
    }

    @MutationMapping
    public Boolean deleteClient(@Argument("clientId") Integer clientId, @Argument("accountId") Integer accountId) {
        log.info("Deleting client");
        return clientService.delete(clientId, accountId);
    }

    @DgsData(parentType = "Account", field = "client")
    public List<Client> clients(DgsDataFetchingEnvironment dataFetchingEnvironment) {
        Account account = dataFetchingEnvironment.getSource();

        log.info("Get Clients for Account " + account.accountId());

        return clientService.getClients(account.accountId());
    }

    @DgsEntityFetcher(name = "Account")
    public Account account(Map<String, Object> values) {

        log.info("Fetching Account entities from Client Side : " + values);
        Object accountId = values.get("accountId");

        if (accountId instanceof Number) {
            return new Account(((Number) accountId).intValue(), null);
        } else if (accountId instanceof String) {
            return new Account(Integer.parseInt((String) accountId), null);
        } else {
            throw new IllegalArgumentException("AccountId is not a Number");
        }
    }
}
