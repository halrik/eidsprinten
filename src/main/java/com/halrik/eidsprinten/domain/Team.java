package com.halrik.eidsprinten.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
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

    @ManyToMany
    private List<Heat> heats = new ArrayList<>();
}
