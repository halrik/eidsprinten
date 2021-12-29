package com.halrik.eidsprinten.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String groupName;
    private String firstName;
    private String lastName;
    private String club;
    private String team;
    private String gender;
    private Date birthDate;
    private Integer leg;
    private String teamLeaderName;
    private String teamLeaderPhone;
    private String teamLeaderEmail;
}
