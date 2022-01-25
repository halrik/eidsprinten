package com.halrik.eidsprinten.model.enums;

import java.util.Arrays;

public enum Group {
    BOYS_11("G 11 år", Gender.BOYS, 11),
    GIRLS_11("J 11 år", Gender.GIRLS, 11),
    BOYS_12("G 12 år", Gender.BOYS, 12),
    GIRLS_12("J 12 år", Gender.GIRLS, 12),
    BOYS_13("G 13 år", Gender.BOYS, 13),
    GIRLS_13("J 13 år", Gender.GIRLS, 13),
    BOYS_14("G 14 år", Gender.BOYS, 14),
    GIRLS_14("J 14 år", Gender.GIRLS, 14);

    String value;
    Gender gender;
    int age;

    Group(String value, Gender gender, int age) {
        this.value = value;
        this.gender = gender;
        this.age = age;
    }

    public String getValue() {
        return value;
    }

    public Gender getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public static Group valueOf(Gender gender, int age) {
        return Arrays.stream(values())
            .filter(group -> group.getAge() == age && group.getGender().equals(gender))
            .findFirst()
            .orElseThrow(
                () -> new IllegalArgumentException("No GroupName enum for age " + age + " and gender " + gender));
    }
}