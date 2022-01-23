package com.halrik.eidsprinten.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HeatAdvancement {

    private Integer result;
    private Integer fromHeatNumber;
    private Integer toHeatNumber;
    private String fromHeatName;
    private String toHeatName;
    private String groupName;

}
