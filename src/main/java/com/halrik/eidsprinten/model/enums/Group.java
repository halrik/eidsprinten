package com.halrik.eidsprinten.model.enums;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum Group {
    MIXED_7_AND_GIRLS_8("Felles 7 år/J 8 år", Gender.MIXED, 7, "F7J8"),
    BOYS_8("G 8 år", Gender.BOYS, 8, "G8"),
    GIRLS_8("J 8 år", Gender.GIRLS, 8, "J8"),
    BOYS_9("G 9 år", Gender.BOYS, 9, "G9"),
    GIRLS_9("J 9 år", Gender.GIRLS, 9, "J9"),
    BOYS_10("G 10 år", Gender.BOYS, 10, "G10"),
    GIRLS_10("J 10 år", Gender.GIRLS, 10, "J10"),
    BOYS_11("G 11 år", Gender.BOYS, 11, "G11"),
    GIRLS_11("J 11 år", Gender.GIRLS, 11, "J11"),
    BOYS_12("G 12 år", Gender.BOYS, 12, "G12"),
    GIRLS_12("J 12 år", Gender.GIRLS, 12, "J12"),
    BOYS_13("G 13 år", Gender.BOYS, 13, "G13"),
    GIRLS_13("J 13 år", Gender.GIRLS, 13, "J13"),
    BOYS_14("G 14 år", Gender.BOYS, 14, "G14"),
    GIRLS_14("J 14 år", Gender.GIRLS, 14, "J14"),
    MIXED_GIRLS_11_12("J 11 år/J 12 år", Gender.GIRLS, 12, "J11J12"),
    MIXED_BOYS_11_12("G 11 år/G 12 år", Gender.BOYS, 12, "G11G12"),
    MIXED_GIRLS_13_14("J 13 år/J 14 år", Gender.GIRLS, 14, "J13J14"),
    MIXED_BOYS_13_14("G 13 år/G 14 år", Gender.BOYS, 14, "G13G14");

    String value;
    Gender gender;
    int age;
    String groupCode;

    Group(String value, Gender gender, int age, String groupCode) {
        this.value = value;
        this.gender = gender;
        this.age = age;
        this.groupCode = groupCode;
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

    public static Group valueOfGroupCode(String groupCode) {
        return Arrays.stream(values())
            .filter(group -> group.getGroupCode().equals(groupCode))
            .findFirst()
            .orElseThrow(
                () -> new IllegalArgumentException("No Group enum for groupCode " + groupCode));
    }

}
