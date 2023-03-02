package com.halrik.eidsprinten.model.enums;

import java.util.Arrays;

public enum Group {
    MIXED_7_AND_GIRLS_8("Felles 7 år/J 8 år", Gender.MIXED, 7),
    BOYS_8("G 8 år", Gender.BOYS, 8),
    GIRLS_8("J 8 år", Gender.GIRLS, 8),
    BOYS_9("G 9 år", Gender.BOYS, 9),
    GIRLS_9("J 9 år", Gender.GIRLS, 9),
    BOYS_10("G 10 år", Gender.BOYS, 10),
    GIRLS_10("J 10 år", Gender.GIRLS, 10),
    BOYS_11("G 11 år", Gender.BOYS, 11),
    GIRLS_11("J 11 år", Gender.GIRLS, 11),
    BOYS_12("G 12 år", Gender.BOYS, 12),
    GIRLS_12("J 12 år", Gender.GIRLS, 12),
    BOYS_13("G 13 år", Gender.BOYS, 13),
    GIRLS_13("J 13 år", Gender.GIRLS, 13),
    BOYS_14("G 14 år", Gender.BOYS, 14),
    GIRLS_14("J 14 år", Gender.GIRLS, 14),
    MIXED_13("G 13 år/J 13 år", Gender.MIXED, 13),
    MIXED_14("G 14 år/J 14 år", Gender.MIXED, 14);

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
                () -> new IllegalArgumentException("No Group enum for age " + age + " and gender " + gender));
    }

    public static Group valueOfGenderAgeShortValue(String genderAgeShortValue) {
        return valueOf(Gender.valueOfGenderValue(genderAgeShortValue.substring(0, 1)),
            Integer.valueOf(genderAgeShortValue.substring(1)));
    }
}
