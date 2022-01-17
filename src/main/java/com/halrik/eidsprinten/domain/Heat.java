package com.halrik.eidsprinten.domain;

import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class Heat {

    @Id
    private Integer heatNumber;
    private boolean rankedHeat;
    private boolean prologHeat;
    private String groupName;
    private String startTime;
    @ManyToMany
    private List<Team> teams;
    @ManyToMany
    private Map<Integer, Team> result;
}
