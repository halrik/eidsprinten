package com.halrik.eidsprinten.services;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

import com.halrik.eidsprinten.domain.Heat;
import com.halrik.eidsprinten.domain.Participant;
import com.halrik.eidsprinten.domain.Team;
import com.halrik.eidsprinten.model.enums.Gender;
import com.halrik.eidsprinten.repository.ParticipantRepository;
import com.halrik.eidsprinten.repository.TeamRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
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

        validateTeams(participantList);

        List<Participant> savedParticipants = new ArrayList<>();

        participantList.forEach(participant -> saveParticipant(savedParticipants, participant));

        savedParticipants.forEach(savedParticipant -> addToTeam(savedParticipant));
    }

    public int validateTeams() {
        return validateTeams(participantRepository.findAll());
    }

    private int validateTeams(List<Participant> participantList) {
        log.info("Validating teams");

        // groupBy age, gender class and team and check that all teams have 2 participants
        Map<Integer, Map<String, Map<String, List<Participant>>>> ageGenderClassTeamMap = participantList.stream()
            .collect(
                Collectors.groupingBy(participant -> participant.getAge(),
                    Collectors.groupingBy(participant -> participant.getGenderClass(),
                        Collectors.groupingBy(participant -> participant.getTeamName())
                    )));

        AtomicInteger numberOfTeams = new AtomicInteger();
        List<String> ageGenderTeamsWithMissingParticipants = new ArrayList<>();
        ageGenderClassTeamMap.forEach((age, genderTeamMap) ->
            genderTeamMap.forEach((gender, teamMap) ->
                teamMap.forEach((teamName, teamParticipants) -> {
                    numberOfTeams.getAndIncrement();
                    if (teamParticipants.size() != 2) {
                        ageGenderTeamsWithMissingParticipants.add(age + " " + gender + " " + teamName);
                    }
                })));

        if (ageGenderTeamsWithMissingParticipants.size() > 0) {
            throw new IllegalStateException(
                ageGenderTeamsWithMissingParticipants.size() + " teams are missing participants "
                    + ageGenderTeamsWithMissingParticipants);
        }

        return numberOfTeams.get();
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

    public List<Heat> getHeatsUnRanked() {
        int maxHeatSize = 9;

        List<Heat> unRankedHeats = new ArrayList<>();

        List<Team> age8Teams = teamRepository.findByAge(8);
        List<Team> age9Teams = teamRepository.findByAge(9);
        List<Team> age10Teams = teamRepository.findByAge(10);

        List<Team> age8BoysTeams = filterTeamsByGender(Gender.BOYS, age8Teams);
        List<Team> age8GirlsTeams = filterTeamsByGender(Gender.GIRLS, age8Teams);
        List<Team> age9BoysTeams = filterTeamsByGender(Gender.BOYS, age9Teams);
        List<Team> age9GirlsTeams = filterTeamsByGender(Gender.GIRLS, age9Teams);
        List<Team> age10BoysTeams = filterTeamsByGender(Gender.BOYS, age10Teams);
        List<Team> age10GirlsTeams = filterTeamsByGender(Gender.GIRLS, age10Teams);

        // add L1 heats
        int leg = 1;
        addHeats(maxHeatSize, leg, unRankedHeats, age8BoysTeams);
        addHeats(maxHeatSize, leg, unRankedHeats, age8GirlsTeams);
        addHeats(maxHeatSize, leg, unRankedHeats, age9BoysTeams);
        addHeats(maxHeatSize, leg, unRankedHeats, age9GirlsTeams);
        addHeats(maxHeatSize, leg, unRankedHeats, age10BoysTeams);
        addHeats(maxHeatSize, leg, unRankedHeats, age10GirlsTeams);

        // add L2 heats
        leg = 2;
        addHeats(maxHeatSize, leg, unRankedHeats, age8BoysTeams);
        addHeats(maxHeatSize, leg, unRankedHeats, age8GirlsTeams);
        addHeats(maxHeatSize, leg, unRankedHeats, age9BoysTeams);
        addHeats(maxHeatSize, leg, unRankedHeats, age9GirlsTeams);
        addHeats(maxHeatSize, leg, unRankedHeats, age10BoysTeams);
        addHeats(maxHeatSize, leg, unRankedHeats, age10GirlsTeams);

        return unRankedHeats;
    }

    private List<Team> filterTeamsByGender(Gender gender, List<Team> teams) {
        return teams.stream().filter(team -> team.getGenderClass().equals(gender.getValue()))
            .collect(Collectors.toList());
    }

    private void addHeats(int maxHeatSize, int leg, List<Heat> unRankedHeats, List<Team> teams) {
        int teamsSize = teams.size();
        double numberOfHeatsEachRoundForCurrentGroup = teamsSize / maxHeatSize + (teamsSize % maxHeatSize > 0 ? 1 : 0);
        int heatNumberCounter = unRankedHeats.size() + 1;

        List<Heat> heats = new ArrayList<>();
        while (heats.size() < numberOfHeatsEachRoundForCurrentGroup) {
            Heat heat = new Heat();
            heat.setHeatNumber(heatNumberCounter);
            heat.setTeams(new ArrayList<>());
            heatNumberCounter++;
            heats.add(heat);
        }

        sortTeamsByLeg(leg, teams);

        int heatIndex = 0;
        for (Team team : teams) {
            heats.get(heatIndex).getTeams().add(team);
            heatIndex = heatIndex == heats.size() - 1 ? 0 : heatIndex + 1;
        }

        unRankedHeats.addAll(heats);
    }

    private void sortTeamsByLeg(int leg, List<Team> teams) {
        List<Team> teamsWithMissingParticipants = teams.stream()
            .filter(team -> team.getParticipantLeg1Name() == null || team.getParticipantLeg2Name() == null)
            .collect(Collectors.toList());

        if (!teamsWithMissingParticipants.isEmpty()) {
            log.error("Found teams with missing participants {}", teamsWithMissingParticipants);
            throw new IllegalStateException("Found teams with missing participants!");
        }

        if (leg == 1) {
            teams.sort(Comparator.comparing(Team::getParticipantLeg1Name));
        } else {
            teams.sort(Comparator.comparing(Team::getParticipantLeg2Name));
        }
    }

}