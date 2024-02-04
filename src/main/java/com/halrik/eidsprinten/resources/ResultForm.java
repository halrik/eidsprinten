package com.halrik.eidsprinten.resources;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResultForm {

    @NotNull
    @Min(1)
    @Max(99)
    private Integer heatNumber;
    @Min(1)
    @Max(999)
    private Integer no1;
    @Min(1)
    @Max(999)
    private Integer no2;
    @Min(1)
    @Max(999)
    private Integer no3;
    @Min(1)
    @Max(999)
    private Integer no4;
    @Min(1)
    @Max(999)
    private Integer no5;
    @Min(1)
    @Max(999)
    private Integer no6;
    @Min(1)
    @Max(999)
    private Integer no7;
    @Min(1)
    @Max(999)
    private Integer no8;
    @Min(1)
    @Max(999)
    private Integer no9;


    public String toString() {
        return "Result(Heath: " + this.heatNumber +
                ", no1: " + this.no1 +
                ", no2: " + this.no2 +
                ", no3: " + this.no3 +
                ", no4: " + this.no4 +
                ", no5: " + this.no5 +
                ", no6: " + this.no6 +
                ", no7: " + this.no7 +
                ", no8: " + this.no8 +
                ", no9: " + this.no9 +
                ")";
    }
}
