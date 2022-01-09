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

    private static final int MAX_HEAT_SIZE = 9;
    private static final int START_HOUR = 10;
    private static final int START_HOUR_RANKED = 13;
    private static final int MINUTES_BETWEEN_HEATS = 5;

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

        LocalDateTime start = getStartTime(START_HOUR);

        // add L1 heats for age 8 and 9
        int leg = 1;
        start = addHeats(start, leg, heatNo(unRankedHeats), unRankedHeats, filterByGender(Gender.BOYS, age8Teams));
        start = addHeats(start, leg, heatNo(unRankedHeats), unRankedHeats, filterByGender(Gender.GIRLS, age8Teams));
        start = addHeats(start, leg, heatNo(unRankedHeats), unRankedHeats, filterByGender(Gender.BOYS, age9Teams));
        start = addHeats(start, leg, heatNo(unRankedHeats), unRankedHeats, filterByGender(Gender.GIRLS, age9Teams));

        // add L2 heats for age 8 and 9
        leg = 2;
        start = addHeats(start, leg, heatNo(unRankedHeats), unRankedHeats, filterByGender(Gender.BOYS, age8Teams));
        start = addHeats(start, leg, heatNo(unRankedHeats), unRankedHeats, filterByGender(Gender.GIRLS, age8Teams));
        start = addHeats(start, leg, heatNo(unRankedHeats), unRankedHeats, filterByGender(Gender.BOYS, age9Teams));
        start = addHeats(start, leg, heatNo(unRankedHeats), unRankedHeats, filterByGender(Gender.GIRLS, age9Teams));

        // add L1 heats for age 10
        leg = 1;
        start = addHeats(start, leg, heatNo(unRankedHeats), unRankedHeats, filterByGender(Gender.BOYS, age10Teams));
        start = addHeats(start, leg, heatNo(unRankedHeats), unRankedHeats, filterByGender(Gender.GIRLS, age10Teams));

        // add L2 heats for age 10
        leg = 2;
        start = addHeats(start, leg, heatNo(unRankedHeats), unRankedHeats, filterByGender(Gender.BOYS, age10Teams));
        addHeats(start, leg, heatNo(unRankedHeats), unRankedHeats, filterByGender(Gender.GIRLS, age10Teams));

        return unRankedHeats;
    }

    public List<Heat> getHeatsRankedProlog() {
        List<Heat> prologHeats = new ArrayList<>();

        List<Team> age11Teams = teamRepository.findByAge(11);
        List<Team> age12Teams = teamRepository.findByAge(12);
        List<Team> age13Teams = teamRepository.findByAge(13);
        List<Team> age14Teams = teamRepository.findByAge(14);

        LocalDateTime start = getStartTime(START_HOUR_RANKED);

        Integer lastHeatNo = getHeatsUnRanked().size();

        // add prolog heats for age 11
        start = addPrologHeats(start, prologHeatNo(lastHeatNo, prologHeats), prologHeats,
            filterByGender(Gender.BOYS, age11Teams));

        start = addPrologHeats(start, prologHeatNo(lastHeatNo, prologHeats), prologHeats,
            filterByGender(Gender.GIRLS, age11Teams));

        // make room for final heats
        int numberOfAge11Heats = prologHeats.size();
        lastHeatNo = lastHeatNo + numberOfAge11Heats;
        start = start.plusMinutes(numberOfAge11Heats * MINUTES_BETWEEN_HEATS);

        // add prolog heats for age 12
        start = addPrologHeats(start, prologHeatNo(lastHeatNo, prologHeats), prologHeats,
            filterByGender(Gender.BOYS, age12Teams));

        start = addPrologHeats(start, prologHeatNo(lastHeatNo, prologHeats), prologHeats,
            filterByGender(Gender.GIRLS, age12Teams));

        // make room for final heats
        int numberOfAge12Heats = prologHeats.size() - numberOfAge11Heats;
        lastHeatNo = lastHeatNo + numberOfAge12Heats;
        start = start.plusMinutes(numberOfAge12Heats * MINUTES_BETWEEN_HEATS);

        // add prolog heats for age 13
        start = addPrologHeats(start, prologHeatNo(lastHeatNo, prologHeats), prologHeats,
            filterByGender(Gender.BOYS, age13Teams));

        start = addPrologHeats(start, prologHeatNo(lastHeatNo, prologHeats), prologHeats,
            filterByGender(Gender.GIRLS, age13Teams));

        // make room for final heats
        int numberOfAge13Heats = prologHeats.size() - numberOfAge11Heats - numberOfAge12Heats;
        lastHeatNo = lastHeatNo + numberOfAge13Heats;
        start = start.plusMinutes(numberOfAge13Heats * MINUTES_BETWEEN_HEATS);

        // add prolog heats for age 14
        start = addPrologHeats(start, prologHeatNo(lastHeatNo, prologHeats), prologHeats,
            filterByGender(Gender.BOYS, age14Teams));

        addPrologHeats(start, prologHeatNo(lastHeatNo, prologHeats), prologHeats,
            filterByGender(Gender.GIRLS, age14Teams));

        return prologHeats;
    }

    private LocalDateTime getStartTime(int startHourRanked) {
        return LocalDateTime.of(2021, Month.FEBRUARY, 13, startHourRanked, 00, 00);
    }

    private int heatNo(List<Heat> heats) {
        return heats.size() + 1;
    }

    private int prologHeatNo(Integer lastHeatNo, List<Heat> prologHeats) {
        lastHeatNo = lastHeatNo + 1 + prologHeats.size();
        return lastHeatNo;
    }

    private List<Team> filterByGender(Gender gender, List<Team> teams) {
        return teams.stream().filter(team -> team.getGenderClass().equals(gender.getValue()))
            .collect(Collectors.toList());
    }

    private LocalDateTime addPrologHeats(LocalDateTime startTime, int heatNumber, List<Heat> heats, List<Team> teams) {
        return addHeats(startTime, 1, heatNumber, heats, teams);
    }

    private LocalDateTime addHeats(LocalDateTime startTime, int leg, int heatNumber, List<Heat> heats,
        List<Team> teams) {
        int teamsSize = teams.size();
        double numberOfHeatsEachRoundForCurrentGroup =
            teamsSize / MAX_HEAT_SIZE + (teamsSize % MAX_HEAT_SIZE > 0 ? 1 : 0);
        int heatNumberCounter = heatNumber;

        List<Heat> addHeats = new ArrayList<>();
        while (addHeats.size() < numberOfHeatsEachRoundForCurrentGroup) {
            Heat heat = new Heat();
            heat.setHeatNumber(heatNumberCounter);
            heat.setStartTime(startTime.format(hourMinuteFormatter));
            heat.setPrologHeat(leg == 1 ? true : false);
            heat.setTeams(new ArrayList<>());
            addHeats.add(heat);
            heatNumberCounter++;
            startTime = startTime.plusMinutes(MINUTES_BETWEEN_HEATS);
        }

        sortTeamsByLeg(leg, teams);

        int heatIndex = 0;
        for (Team team : teams) {
            Heat heat = addHeats.get(heatIndex);
            heat.setGroupName(team.getGroupName());
            heat.getTeams().add(team);
            heatIndex = heatIndex == addHeats.size() - 1 ? 0 : heatIndex + 1;
        }

        heats.addAll(addHeats);
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
