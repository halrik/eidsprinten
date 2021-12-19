package com.halrik.eidsprinten.domain;

import java.util.Date;
import lombok.Data;

@Data
public class Participant {

    private String group;
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
