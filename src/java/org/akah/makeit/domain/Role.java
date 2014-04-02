package org.akah.makeit.domain;

public enum Role {
    DRIVER("driver"), GUEST("guest"), OWNER("owner");

    final String value;

    Role(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }

    public String getKey() {
        return name();
    }
}