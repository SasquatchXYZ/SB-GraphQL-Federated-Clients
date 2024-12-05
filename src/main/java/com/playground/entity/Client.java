package com.playground.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Client {

    @EmbeddedId
    private ClientAccountId clientIdAccountId;

    @Transient
    private Integer clientId;
    @Transient
    private Integer accountId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String country;
}
