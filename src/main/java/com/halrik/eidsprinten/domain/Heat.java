package com.halrik.eidsprinten.domain;

import java.util.List;
import lombok.Data;

@Data
public class Heat {

    private Integer heatNumber;
    private List<Team> teams;
}
