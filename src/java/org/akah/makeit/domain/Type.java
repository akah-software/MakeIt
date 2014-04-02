package org.akah.makeit.domain;

public enum Type {
    PUBLIC("Public"), PRIVATE("Private");

    final String value;

    Type(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }

    public String getKey() {
        return name();
    }
};
