package com.halrik.eidsprinten.domain;

public enum Gender {
    BOYS("G"),
    GIRLS("J");

    String value;

    Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
