package com.halrik.eidsprinten.services;

import com.halrik.eidsprinten.domain.Heat;
import com.halrik.eidsprinten.domain.Team;
import com.halrik.eidsprinten.model.enums.Gender;
import com.halrik.eidsprinten.repository.TeamRepository;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HeatsService {

    private static final Logger log = LoggerFactory.getLogger(HeatsService.class);

    private static final int MAX_HEAT_SIZE = 8;
    private static final int START_HOUR = 10;

    private TeamRepository teamRepository;

    private final DateTimeFormatter hourMinuteFormatter;

    public HeatsService(TeamRepository teamRepository, DateTimeFormatter hourMinuteFormatter) {
        this.teamRepository = teamRepository;
        this.hourMinuteFormatter = hourMinuteFormatter;
    }

    public List<Heat> getHeatsUnRanked() {
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

        LocalDateTime startTime = LocalDateTime.of(2021, Month.FEBRUARY, 13, START_HOUR, 00, 00);
        ;

        // add L1 heats for age 8 and 9
        int leg = 1;
        startTime = addHeats(startTime, leg, unRankedHeats, age8BoysTeams);
        startTime = addHeats(startTime, leg, unRankedHeats, age8GirlsTeams);
        startTime = addHeats(startTime, leg, unRankedHeats, age9BoysTeams);
        startTime = addHeats(startTime, leg, unRankedHeats, age9GirlsTeams);

        // add L2 heats for age 8 and 9
        leg = 2;
        startTime = addHeats(startTime, leg, unRankedHeats, age8BoysTeams);
        startTime = addHeats(startTime, leg, unRankedHeats, age8GirlsTeams);
        startTime = addHeats(startTime, leg, unRankedHeats, age9BoysTeams);
        startTime = addHeats(startTime, leg, unRankedHeats, age9GirlsTeams);

        // add L1 heats for age 10
        leg = 1;
        startTime = addHeats(startTime, leg, unRankedHeats, age10BoysTeams);
        startTime = addHeats(startTime, leg, unRankedHeats, age10GirlsTeams);

        // add L2 heats for age 10
        leg = 2;
        startTime = addHeats(startTime, leg, unRankedHeats, age10BoysTeams);
        startTime = addHeats(startTime, leg, unRankedHeats, age10GirlsTeams);

        return unRankedHeats;
    }

    public List<Heat> getHeatsPrologRanked() {
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

        LocalDateTime startTime = LocalDateTime.of(2021, Month.FEBRUARY, 13, START_HOUR, 00, 00);
        ;

        // add L1 heats for age 8 and 9
        int leg = 1;
        startTime = addHeats(startTime, leg, unRankedHeats, age8BoysTeams);
        startTime = addHeats(startTime, leg, unRankedHeats, age8GirlsTeams);
        startTime = addHeats(startTime, leg, unRankedHeats, age9BoysTeams);
        startTime = addHeats(startTime, leg, unRankedHeats, age9GirlsTeams);

        // add L2 heats for age 8 and 9
        leg = 2;
        startTime = addHeats(startTime, leg, unRankedHeats, age8BoysTeams);
        startTime = addHeats(startTime, leg, unRankedHeats, age8GirlsTeams);
        startTime = addHeats(startTime, leg, unRankedHeats, age9BoysTeams);
        startTime = addHeats(startTime, leg, unRankedHeats, age9GirlsTeams);

        // add L1 heats for age 10
        leg = 1;
        startTime = addHeats(startTime, leg, unRankedHeats, age10BoysTeams);
        startTime = addHeats(startTime, leg, unRankedHeats, age10GirlsTeams);

        // add L2 heats for age 10
        leg = 2;
        startTime = addHeats(startTime, leg, unRankedHeats, age10BoysTeams);
        startTime = addHeats(startTime, leg, unRankedHeats, age10GirlsTeams);

        return unRankedHeats;
    }

    private List<Team> filterTeamsByGender(Gender gender, List<Team> teams) {
        return teams.stream().filter(team -> team.getGenderClass().equals(gender.getValue()))
            .collect(Collectors.toList());
    }

    private LocalDateTime addHeats(LocalDateTime startTime, int leg, List<Heat> unRankedHeats,
        List<Team> teams) {
        int teamsSize = teams.size();
        double numberOfHeatsEachRoundForCurrentGroup =
            teamsSize / MAX_HEAT_SIZE + (teamsSize % MAX_HEAT_SIZE > 0 ? 1 : 0);
        int heatNumberCounter = unRankedHeats.size() + 1;

        List<Heat> heats = new ArrayList<>();
        while (heats.size() < numberOfHeatsEachRoundForCurrentGroup) {
            Heat heat = new Heat();
            heat.setHeatNumber(heatNumberCounter);
            heat.setStartTime(startTime.format(hourMinuteFormatter));
            heat.setTeams(new ArrayList<>());
            heats.add(heat);
            heatNumberCounter++;
            startTime = startTime.plusMinutes(5);
        }

        sortTeamsByLeg(leg, teams);

        int heatIndex = 0;
        for (Team team : teams) {
            Heat heat = heats.get(heatIndex);
            heat.setGroupName(team.getGroupName());
            heat.getTeams().add(team);
            heatIndex = heatIndex == heats.size() - 1 ? 0 : heatIndex + 1;
        }

        unRankedHeats.addAll(heats);
        return startTime;
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
