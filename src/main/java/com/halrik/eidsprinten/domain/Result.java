package com.halrik.eidsprinten.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {

    public final static String DNS = "DNS";
    public final static String DNF = "DNF";

    private String result;
    private Team team;

}
