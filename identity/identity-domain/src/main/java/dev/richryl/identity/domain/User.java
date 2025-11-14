package dev.richryl.identity.domain;

import java.util.UUID;

public class User {
    UUID id;
    String externalId;

    public User(UUID id, String externalId) {
        this.id = id;
        this.externalId = externalId;
    }

    public String getExternalId() {
        return externalId;
    }


    public UUID getId() {
        return id;
    }

}
