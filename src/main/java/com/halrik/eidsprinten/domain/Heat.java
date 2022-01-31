package com.halrik.eidsprinten.domain;

import java.util.Map;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@ToString(exclude = "teams")
@EqualsAndHashCode(exclude = "teams")
public class Heat {

    @Id
    private Integer heatNumber;
    private String heatName;
    private boolean rankedHeat;
    private boolean prologHeat;
    private String groupName;
    private String startTime;

    @ManyToMany
    private Set<Team> teams;

    @ManyToMany
    private Map<Integer, Team> result;

    public void addTeam(Team team) {
        teams.add(team);
        team.getHeats().add(this);
    }

    public void removeTeam(Team team) {
        teams.remove(team);
        result.entrySet().removeIf(entry -> entry.getValue().equals(team));
        team.getHeats().remove(this);
    }

}
