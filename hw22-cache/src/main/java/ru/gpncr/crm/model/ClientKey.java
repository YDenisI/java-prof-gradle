package ru.gpncr.crm.model;

import java.util.Objects;

public class ClientKey {
    private final String keyId;
    private final Long clientId;

    public ClientKey(String keyId, Long clientId) {
        this.keyId = keyId;
        this.clientId = clientId;
    }

    public String getKeyId() {
        return keyId;
    }

    public Long getClientId() {
        return clientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientKey)) return false;
        ClientKey that = (ClientKey) o;
        return Objects.equals(clientId, that.clientId) && Objects.equals(keyId, that.keyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyId, clientId);
    }

    @Override
    public String toString() {
        return "ClientKey{" + "keyId=" + keyId + ", clientId=" + clientId + '}';
    }
}
