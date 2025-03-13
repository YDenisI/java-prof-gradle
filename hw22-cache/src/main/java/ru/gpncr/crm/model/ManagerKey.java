package ru.gpncr.crm.model;

import java.util.Objects;

public class ManagerKey {
    private final String keyId;
    private final Long managerId;

    public ManagerKey(String keyId, Long clientId) {
        this.keyId = keyId;
        this.managerId = clientId;
    }

    public String getKeyId() {
        return keyId;
    }

    public Long getClientId() {
        return managerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ManagerKey)) return false;
        ManagerKey that = (ManagerKey) o;
        return Objects.equals(managerId, that.managerId) && Objects.equals(keyId, that.keyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyId, managerId);
    }

    @Override
    public String toString() {
        return "ManagerKey{" + "keyId=" + keyId + ", managerId=" + managerId + '}';
    }
}
