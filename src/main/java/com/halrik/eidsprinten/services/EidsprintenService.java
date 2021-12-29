package com.halrik.eidsprinten.services;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

import com.halrik.eidsprinten.domain.Participant;
import com.halrik.eidsprinten.domain.Team;
import com.halrik.eidsprinten.repository.ParticipantRepository;
import com.halrik.eidsprinten.repository.TeamRepository;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

@Service
public class EidsprintenService {

    private static final Logger log = LoggerFactory.getLogger(EidsprintenService.class);

    private ParticipantRepository participantRepository;
    private TeamRepository teamRepository;

    public EidsprintenService(ParticipantRepository participantRepository,
        TeamRepository teamRepository) {
        this.participantRepository = participantRepository;
        this.teamRepository = teamRepository;
    }

    public void saveParticipantsAndTeams(List<Participant> participantList) {
        log.info("Saving participants and teams");

        List<Participant> savedParticipants = new ArrayList<>();
        participantList.forEach(participant -> saveParticipant(savedParticipants, participant));

        savedParticipants.forEach(savedParticipant -> addToTeam(savedParticipant));
    }

    private void addToTeam(Participant participant) {
        Team team = getTeam(participant);

        log.info("Add participant {} to team {}", participant, team);

        if (participant.getLeg() == 1) {
            team.setParticipantLeg1Id(participant.getId());
            team.setParticipantLeg1Name(participant.getFirstName() + " " + participant.getLastName());
        } else if (participant.getLeg() == 2) {
            team.setParticipantLeg2Id(participant.getId());
            team.setParticipantLeg2Name(participant.getFirstName() + " " + participant.getLastName());
        }

        teamRepository.save(team);
    }

    private Team getTeam(Participant participant) {
        Team matchingTeam = new Team();
        matchingTeam.setTeamName(participant.getTeamName());
        matchingTeam.setGroupName(participant.getGroupName());

        Team team = teamRepository.findOne(Example.of(matchingTeam, ExampleMatcher.matching()
            .withIgnorePaths("id")
            .withMatcher("teamName", ignoreCase())
            .withMatcher("groupName", ignoreCase()))).orElse(matchingTeam);

        team.setClubName(participant.getClubName());
        team.setTeamLeaderName(participant.getTeamLeaderName());
        team.setTeamLeaderPhone(participant.getTeamLeaderPhone());
        team.setTeamLeaderEmail(participant.getTeamLeaderEmail());
        team.setAge(participant.getAge());
        team.setGenderClass(participant.getGenderClass());

        return team;
    }

    private void saveParticipant(List<Participant> savedParticipants, Participant participant) {
        if (participantRepository.exists(Example.of(participant, ExampleMatcher.matching()
            .withIgnorePaths("id")
            .withMatcher("firstName", ignoreCase())
            .withMatcher("lastName", ignoreCase())))) {
            return;
        }

        log.info("Save participant {}", participant);
        savedParticipants.add(participantRepository.save(participant));
    }

    public List<Participant> getParticipantsByClub(String clubName) {
        return participantRepository.findByClubName(clubName);
    }

    public List<Team> getTeamsByAge(Integer age) {
        return teamRepository.findByAge(age);
    }

}
