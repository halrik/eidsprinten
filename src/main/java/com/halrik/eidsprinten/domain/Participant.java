package com.halrik.eidsprinten.domain;

import java.util.Date;
import lombok.Data;

@Data
public class Participant {

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
