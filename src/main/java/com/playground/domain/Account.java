package com.playground.domain;

import com.playground.entity.Client;

import java.util.List;

public record Account(
        Integer accountId,
        List<Client> client
) {
}
