package com.halrik.eidsprinten.model.enums;

public enum FinalHeat {
    A_FINAL_HEAT("A - finale"),
    B_FINAL_HEAT("B - finale"),
    C_FINAL_HEAT("C - finale"),
    D_FINAL_HEAT("D - finale"),
    E_FINAL_HEAT("E - finale");

    String value;

    FinalHeat(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
