package com.halrik.eidsprinten.model.enums;

import java.util.Arrays;

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

    public static Gender valueOfGenderValue(String genderValue) {
        return Arrays.stream(values())
            .filter(gender -> gender.getValue().equals(genderValue))
            .findFirst()
            .orElseThrow(
                () -> new IllegalArgumentException("No Gender enum for " + genderValue));
    }

}
