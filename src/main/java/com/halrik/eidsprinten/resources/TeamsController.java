package com.halrik.eidsprinten.resources;

import com.halrik.eidsprinten.domain.Participant;
import com.halrik.eidsprinten.domain.Team;
import com.halrik.eidsprinten.model.enums.Gender;
import com.halrik.eidsprinten.model.enums.Group;
import com.halrik.eidsprinten.services.EidsprintenService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
public class TeamsController {

    private static final Logger log = LoggerFactory.getLogger(TeamsController.class);

    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    private EidsprintenService eidsprintenService;

    public TeamsController(EidsprintenService eidsprintenService) {
        this.eidsprintenService = eidsprintenService;
    }

    @GetMapping(value = "/{age}")
    public ResponseEntity<List<Team>> getTeamsByAge(@PathVariable Integer age) {
        return new ResponseEntity<>(eidsprintenService.getTeamsByAge(age), HttpStatus.OK);
    }

    @PostMapping(value = "/bibs/allocate")
    public ResponseEntity allocateBibs() {
        eidsprintenService.allocateBibs();
        return ResponseEntity.ok("Bibs allocated");
    }

    @DeleteMapping(value = "/{bib}")
    public ResponseEntity<String> deleteTeam(@PathVariable Integer bib) {
        return new ResponseEntity<>(eidsprintenService.deleteTeam(bib) + " team deleted", HttpStatus.OK);
    }

    @PutMapping(value = "/")
    public ResponseEntity addTeam(@RequestParam(required = false) Integer bib,
        @RequestParam String clubName,
        @RequestParam String teamName,
        @RequestParam String participantLeg1FirstName,
        @RequestParam String participantLeg1LastName,
        @RequestParam String participantLeg1BirthDate,
        @RequestParam String participantLeg1GenderAgeShortValue,
        @RequestParam String participantLeg2FirstName,
        @RequestParam String participantLeg2LastName,
        @RequestParam String participantLeg2BirthDate,
        @RequestParam String participantLeg2GenderAgeShortValue,
        @RequestParam(required = false) String teamLeaderName,
        @RequestParam(required = false) String teamLeaderPhone,
        @RequestParam(required = false) String teamLeaderEmail) throws ParseException {

        Group participantLeg1Group = Group.valueOfGenderAgeShortValue(participantLeg1GenderAgeShortValue);
        Group participantLeg2Group = Group.valueOfGenderAgeShortValue(participantLeg2GenderAgeShortValue);

        Gender teamGender =
            participantLeg1Group.getGender().equals(participantLeg2Group.getGender()) ? participantLeg1Group.getGender()
                : Gender.BOYS;
        int teamAge = participantLeg1Group.getAge() >= participantLeg2Group.getAge() ? participantLeg1Group.getAge()
            : participantLeg2Group.getAge();
        Group teamGroup = Group.valueOf(teamGender, teamAge);

        Participant participantLeg1 = new Participant();
        participantLeg1.setLeg(1);
        participantLeg1.setAge(participantLeg1Group.getAge());
        participantLeg1.setGender(participantLeg1Group.getGender().getValue());
        participantLeg1.setGenderClass(teamGroup.getGender().getValue());
        participantLeg1.setGroupName(participantLeg1Group.getValue());
        participantLeg1.setFirstName(participantLeg1FirstName);
        participantLeg1.setLastName(participantLeg1LastName);
        participantLeg1.setBirthDate(DATE_FORMAT.parse(participantLeg1BirthDate));
        participantLeg1.setClubName(clubName);
        participantLeg1.setTeamName(teamName);
        participantLeg1.setTeamLeaderName(teamLeaderName);
        participantLeg1.setTeamLeaderPhone(teamLeaderPhone);
        participantLeg1.setTeamLeaderEmail(teamLeaderEmail);

        Participant participantLeg2 = new Participant();
        participantLeg2.setLeg(2);
        participantLeg2.setAge(participantLeg2Group.getAge());
        participantLeg2.setGender(participantLeg2Group.getGender().getValue());
        participantLeg2.setGenderClass(teamGroup.getGender().getValue());
        participantLeg2.setGroupName(participantLeg2Group.getValue());
        participantLeg2.setFirstName(participantLeg2FirstName);
        participantLeg2.setLastName(participantLeg2LastName);
        participantLeg2.setBirthDate(DATE_FORMAT.parse(participantLeg2BirthDate));
        participantLeg2.setClubName(clubName);
        participantLeg2.setTeamName(teamName);
        participantLeg2.setTeamLeaderName(teamLeaderName);
        participantLeg2.setTeamLeaderPhone(teamLeaderPhone);
        participantLeg2.setTeamLeaderEmail(teamLeaderEmail);

        Team team = new Team();
        team.setBib(bib);
        team.setTeamName(teamName);
        team.setAge(teamGroup.getAge());
        team.setGenderClass(teamGroup.getGender().getValue());
        team.setGroupName(teamGroup.getValue());
        team.setClubName(clubName);
        team.setTeamLeaderName(teamLeaderName);
        team.setTeamLeaderPhone(teamLeaderPhone);
        team.setTeamLeaderEmail(teamLeaderEmail);
        team.setParticipantLeg1Name(participantLeg1FirstName + " " + participantLeg1LastName);
        team.setParticipantLeg2Name(participantLeg2FirstName + " " + participantLeg2LastName);

        team = eidsprintenService.saveTeam(team);

        return ResponseEntity.ok("Team " + team.getId() + " added!");
    }

}
