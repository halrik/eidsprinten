package com.halrik.eidsprinten.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Date;
import lombok.Data;

@Entity
@Data
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String groupName;
    private Integer age;
    private String genderClass;
    private String firstName;
    private String lastName;
    private String clubName;
    private String teamName;
    private String gender;
    private Date birthDate;
    private Integer leg;
    private String teamLeaderName;
    private String teamLeaderPhone;
    private String teamLeaderEmail;
}
