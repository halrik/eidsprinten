package com.halrik.eidsprinten.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@ToString(exclude = "heats")
@EqualsAndHashCode(exclude = "heats")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer bib;
    private String groupName;
    private Integer age;
    private String genderClass;
    private String clubName;
    private String teamName;
    private String teamLeaderName;
    private String teamLeaderPhone;
    private String teamLeaderEmail;
    private String participantLeg1Name;
    private String participantLeg2Name;

    @ManyToMany(mappedBy = "teams")
    @JsonIgnore
    private Set<Heat> heats;

}
