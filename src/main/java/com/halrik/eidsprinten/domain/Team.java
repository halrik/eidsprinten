package com.halrik.eidsprinten.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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

    private Long participantLeg1Id;
    private String participantLeg1Name;

    private Long participantLeg2Id;
    private String participantLeg2Name;

    @ManyToMany(mappedBy = "teams")
    @JsonIgnore
    private Set<Heat> heats;

}
