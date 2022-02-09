package com.halrik.eidsprinten.services;

import static com.halrik.eidsprinten.utils.HeatsUtil.sortTeamsWithinHeatByBibAndSortHeatsByHeatNumber;

import com.halrik.eidsprinten.domain.Heat;
import com.halrik.eidsprinten.domain.Team;
import com.halrik.eidsprinten.exception.NotFoundException;
import com.halrik.eidsprinten.exception.ValidationException;
import com.halrik.eidsprinten.model.enums.FinalHeat;
import com.halrik.eidsprinten.model.enums.Gender;
import com.halrik.eidsprinten.model.enums.Group;
import com.halrik.eidsprinten.repository.HeatRepository;
import com.halrik.eidsprinten.repository.TeamRepository;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HeatsService {

    private static final Logger log = LoggerFactory.getLogger(HeatsService.class);

    private static final int MAX_HEAT_SIZE = 9;
    private static final int START_HOUR = 10;
    private static final int START_MINUTE = 00;
    private static final int START_HOUR_RANKED = 13;
    private static final int START_MINUTE_RANKED = 15;
    private static final int MINUTES_BETWEEN_HEATS = 5;

    private TeamRepository teamRepository;
    private HeatRepository heatRepository;

    private final DateTimeFormatter hourMinuteFormatter;

    public HeatsService(TeamRepository teamRepository, HeatRepository heatRepository,
        DateTimeFormatter hourMinuteFormatter) {
        this.teamRepository = teamRepository;
        this.heatRepository = heatRepository;
        this.hourMinuteFormatter = hourMinuteFormatter;
    }

    public List<Heat> getHeatsUnRankedAndSave() {
        deleteHeats(false, true);
        deleteHeats(false, false);
        return heatRepository.saveAll(getHeatsUnRanked());
    }

    public void deleteAllHeats() {
        deleteHeats(false, true);
        deleteHeats(false, false);
        deleteHeats(true, true);
        deleteHeats(true, false);
    }

    public void deleteHeats(boolean isRanked, boolean isProlog) {
        List<Heat> heats = heatRepository.findByRankedHeatAndPrologHeat(isRanked, isProlog);
        heats.forEach(heat -> {
            Set<Team> teams = new HashSet(heat.getTeams());
            teams.forEach(team -> heat.removeTeam(team));
            heatRepository.save(heat);
        });
        heatRepository.deleteAll(heats);
    }

    public List<Heat> getHeatsUnRankedStored() {
        List<Heat> unrankedHeats = heatRepository.findByRankedHeat(false);
        return sortTeamsWithinHeatByBibAndSortHeatsByHeatNumber(unrankedHeats);
    }

    public List<Heat> getHeatsRankedStored() {
        List<Heat> rankedHeats = heatRepository.findByRankedHeat(true);
        return sortTeamsWithinHeatByBibAndSortHeatsByHeatNumber(rankedHeats);
    }

    public List<Heat> getHeatsUnRanked() {
        List<Heat> unRankedHeats = new ArrayList<>();

        List<Team> age8Teams = teamRepository.findByAge(8);
        List<Team> age9Teams = teamRepository.findByAge(9);
        List<Team> age10Teams = teamRepository.findByAge(10);

        LocalDateTime start = getStartTime(START_HOUR, START_MINUTE);

        // add L1 heats for age 8
        int leg = 1;
        start = addUnrankedHeats(start, leg, heatNo(unRankedHeats), unRankedHeats,
            filterByGender(Gender.BOYS, age8Teams));
        start = addUnrankedHeats(start, leg, heatNo(unRankedHeats), unRankedHeats,
            filterByGender(Gender.GIRLS, age8Teams));

        start = start.plusMinutes(5);

        // add L2 heats for age 8
        leg = 2;
        start = addUnrankedHeats(start, leg, heatNo(unRankedHeats), unRankedHeats,
            filterByGender(Gender.BOYS, age8Teams));
        start = addUnrankedHeats(start, leg, heatNo(unRankedHeats), unRankedHeats,
            filterByGender(Gender.GIRLS, age8Teams));

        // add L1 heats for age 9
        leg = 1;
        start = addUnrankedHeats(start, leg, heatNo(unRankedHeats), unRankedHeats,
            filterByGender(Gender.BOYS, age9Teams));
        start = start.plusMinutes(5);
        start = addUnrankedHeats(start, leg, heatNo(unRankedHeats), unRankedHeats,
            filterByGender(Gender.GIRLS, age9Teams));

        // add L2 heats for age 9
        leg = 2;
        start = addUnrankedHeats(start, leg, heatNo(unRankedHeats), unRankedHeats,
            filterByGender(Gender.BOYS, age9Teams));
        start = addUnrankedHeats(start, leg, heatNo(unRankedHeats), unRankedHeats,
            filterByGender(Gender.GIRLS, age9Teams));

        // add L1 heats for age 10
        leg = 1;
        start = addUnrankedHeats(start, leg, heatNo(unRankedHeats), unRankedHeats,
            filterByGender(Gender.BOYS, age10Teams));
        start = addUnrankedHeats(start, leg, heatNo(unRankedHeats), unRankedHeats,
            filterByGender(Gender.GIRLS, age10Teams));

        // add L2 heats for age 10
        leg = 2;
        start = addUnrankedHeats(start, leg, heatNo(unRankedHeats), unRankedHeats,
            filterByGender(Gender.BOYS, age10Teams));
        addUnrankedHeats(start, leg, heatNo(unRankedHeats), unRankedHeats, filterByGender(Gender.GIRLS, age10Teams));

        return sortTeamsWithinHeatByBibAndSortHeatsByHeatNumber(unRankedHeats);
    }

    public List<Heat> getHeatsRankedAndSave() {
        deleteHeats(true, true);
        return heatRepository.saveAll(getHeatsRanked());
    }

    public List<Heat> getHeatsRanked() {
        List<Heat> rankedHeats = new ArrayList<>();

        List<Team> age11Teams = teamRepository.findByAge(11);
        List<Team> age12Teams = teamRepository.findByAge(12);
        List<Team> age13Teams = teamRepository.findByAge(13);
        List<Team> age14Teams = teamRepository.findByAge(14);

        LocalDateTime start = getStartTime(START_HOUR_RANKED, START_MINUTE_RANKED);

        Integer lastHeatNo = getHeatsUnRanked().size();

        // add prolog heats for age 11
        start = addPrologHeats(start, nextHeatNo(lastHeatNo, rankedHeats), rankedHeats,
            filterByGender(Gender.BOYS, age11Teams));
        start = addPrologHeats(start, nextHeatNo(lastHeatNo, rankedHeats), rankedHeats,
            filterByGender(Gender.GIRLS, age11Teams));

        start = start.plusMinutes(5);

        // add empty final heats for age 11
        start = addFinalHeats(start, nextHeatNo(lastHeatNo, rankedHeats), rankedHeats,
            sizeOfPrologHeatsForGroup(Group.BOYS_11, rankedHeats), Group.BOYS_11);
        start = addFinalHeats(start, nextHeatNo(lastHeatNo, rankedHeats), rankedHeats,
            sizeOfPrologHeatsForGroup(Group.GIRLS_11, rankedHeats), Group.GIRLS_11);

        // add prolog heats for age 12
        start = addPrologHeats(start, nextHeatNo(lastHeatNo, rankedHeats), rankedHeats,
            filterByGender(Gender.BOYS, age12Teams));
        start = addPrologHeats(start, nextHeatNo(lastHeatNo, rankedHeats), rankedHeats,
            filterByGender(Gender.GIRLS, age12Teams));

        start = start.plusMinutes(5);

        // add empty final heats for age 12
        start = addFinalHeats(start, nextHeatNo(lastHeatNo, rankedHeats), rankedHeats,
            sizeOfPrologHeatsForGroup(Group.BOYS_12, rankedHeats), Group.BOYS_12);
        start = addFinalHeats(start, nextHeatNo(lastHeatNo, rankedHeats), rankedHeats,
            sizeOfPrologHeatsForGroup(Group.GIRLS_12, rankedHeats), Group.GIRLS_12);

        // add prolog heats for age 13
        start = addPrologHeats(start, nextHeatNo(lastHeatNo, rankedHeats), rankedHeats,
            filterByGender(Gender.BOYS, age13Teams));
        start = addPrologHeats(start, nextHeatNo(lastHeatNo, rankedHeats), rankedHeats,
            filterByGender(Gender.GIRLS, age13Teams));

        start = start.plusMinutes(10);

        // add empty final heats for age 13
        start = addFinalHeats(start, nextHeatNo(lastHeatNo, rankedHeats), rankedHeats,
            sizeOfPrologHeatsForGroup(Group.BOYS_13, rankedHeats), Group.BOYS_13);
        start = addFinalHeats(start, nextHeatNo(lastHeatNo, rankedHeats), rankedHeats,
            sizeOfPrologHeatsForGroup(Group.GIRLS_13, rankedHeats), Group.GIRLS_13);

        // add prolog heats for age 14
        start = addPrologHeats(start, nextHeatNo(lastHeatNo, rankedHeats), rankedHeats,
            filterByGender(Gender.BOYS, age14Teams));
        start = addPrologHeats(start, nextHeatNo(lastHeatNo, rankedHeats), rankedHeats,
            filterByGender(Gender.GIRLS, age14Teams));

        start = start.plusMinutes(10);

        // add empty final heats for age 14
        start = addFinalHeats(start, nextHeatNo(lastHeatNo, rankedHeats), rankedHeats,
            sizeOfPrologHeatsForGroup(Group.BOYS_14, rankedHeats), Group.BOYS_14);
        addFinalHeats(start, nextHeatNo(lastHeatNo, rankedHeats), rankedHeats,
            sizeOfPrologHeatsForGroup(Group.GIRLS_14, rankedHeats), Group.GIRLS_14);

        rankedHeats.forEach(heat ->
            heat.setTeams(heat.getTeams().stream().sorted(Comparator.comparingInt(Team::getBib))
                .collect(Collectors.toCollection(LinkedHashSet::new))));

        return rankedHeats;
    }

    private int sizeOfPrologHeatsForGroup(Group group, List<Heat> heats) {
        return heats.stream()
            .filter(heat -> heat.isPrologHeat() && heat.getGroupName().equals(group.getValue()))
            .collect(Collectors.toList()).size();
    }

    public Heat registerResult(Integer heatNumber, Map<Integer, Integer> resultNumberMap) {
        Optional<Heat> heatOptional = heatRepository.findById(heatNumber);

        Heat heat = heatOptional.orElseThrow(() -> new NotFoundException("Could not find heat " + heatNumber));
        heat.setResult(convertToResultTeamMap(resultNumberMap, heat.getTeams()));

        return heatRepository.save(heat);
    }

    private Map<Integer, Team> convertToResultTeamMap(Map<Integer, Integer> resultNumberMap, Set<Team> teams) {
        Map<Integer, Team> resultTeamMap = new HashMap<>();

        resultNumberMap.forEach((result, bib) -> {
            Team team = teams.stream().filter(t -> t.getBib().equals(bib))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Kunne ikke finne team " + bib + " i heatet!"));
            resultTeamMap.put(result, team);
        });

        return resultTeamMap;
    }

    private LocalDateTime getStartTime(int startHourRanked, int startMinuteRanked) {
        return LocalDateTime.of(2022, Month.FEBRUARY, 13, startHourRanked, startMinuteRanked, 00);
    }

    private int heatNo(List<Heat> heats) {
        return heats.size() + 1;
    }

    private int nextHeatNo(Integer lastHeatNo, List<Heat> heats) {
        lastHeatNo = lastHeatNo + 1 + heats.size();
        return lastHeatNo;
    }

    private List<Team> filterByGender(Gender gender, List<Team> teams) {
        return teams.stream().filter(team -> team.getGenderClass().equals(gender.getValue()))
            .collect(Collectors.toList());
    }

    private LocalDateTime addUnrankedHeats(LocalDateTime startTime, int leg, int heatNumber, List<Heat> heats,
        List<Team> teams) {
        return addHeats(startTime, leg, false, heatNumber, heats, teams);
    }

    private LocalDateTime addPrologHeats(LocalDateTime startTime, int heatNumber, List<Heat> heats, List<Team> teams) {
        return addHeats(startTime, 1, true, heatNumber, heats, teams);
    }

    private LocalDateTime addFinalHeats(LocalDateTime startTime, int heatNumber, List<Heat> heats,
        int numberOfFinalHeatsForCurrentGroup, Group groupName) {
        int heatNumberCounter = heatNumber;

        List<Heat> addHeats = new ArrayList<>();
        while (addHeats.size() < numberOfFinalHeatsForCurrentGroup) {
            Heat heat = new Heat();
            heat.setHeatNumber(heatNumberCounter);
            heat.setHeatName(getFinalHeatName(numberOfFinalHeatsForCurrentGroup, addHeats.size()));
            heat.setStartTime(startTime.format(hourMinuteFormatter));
            heat.setGroupName(groupName.getValue());
            heat.setPrologHeat(false);
            heat.setRankedHeat(true);
            heat.setTeams(new HashSet<>());
            addHeats.add(heat);
            heatNumberCounter++;
            startTime = startTime.plusMinutes(MINUTES_BETWEEN_HEATS);
        }

        heats.addAll(addHeats);
        return startTime;
    }

    private String getFinalHeatName(Integer numberOfFinalHeatsForCurrentGroup, int heatsAdded) {
        switch (numberOfFinalHeatsForCurrentGroup) {
            case 1:
                if (heatsAdded == 0) {
                    return FinalHeat.A_FINAL_HEAT.getValue();
                }
                break;
            case 2:
                if (heatsAdded == 0) {
                    return FinalHeat.B_FINAL_HEAT.getValue();
                }
                if (heatsAdded == 1) {
                    return FinalHeat.A_FINAL_HEAT.getValue();
                }
                break;
            case 3:
                if (heatsAdded == 0) {
                    return FinalHeat.C_FINAL_HEAT.getValue();
                }
                if (heatsAdded == 1) {
                    return FinalHeat.B_FINAL_HEAT.getValue();
                }
                if (heatsAdded == 2) {
                    return FinalHeat.A_FINAL_HEAT.getValue();
                }
                break;
            case 4:
                if (heatsAdded == 0) {
                    return FinalHeat.D_FINAL_HEAT.getValue();
                }
                if (heatsAdded == 1) {
                    return FinalHeat.C_FINAL_HEAT.getValue();
                }
                if (heatsAdded == 2) {
                    return FinalHeat.B_FINAL_HEAT.getValue();
                }
                if (heatsAdded == 3) {
                    return FinalHeat.A_FINAL_HEAT.getValue();
                }
                break;
            case 5:
                if (heatsAdded == 0) {
                    return FinalHeat.E_FINAL_HEAT.getValue();
                }
                if (heatsAdded == 1) {
                    return FinalHeat.D_FINAL_HEAT.getValue();
                }
                if (heatsAdded == 2) {
                    return FinalHeat.C_FINAL_HEAT.getValue();
                }
                if (heatsAdded == 3) {
                    return FinalHeat.B_FINAL_HEAT.getValue();
                }
                if (heatsAdded == 4) {
                    return FinalHeat.A_FINAL_HEAT.getValue();
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + numberOfFinalHeatsForCurrentGroup);
        }
        throw new IllegalStateException("Could not decide final heat name!");
    }

    private LocalDateTime addHeats(LocalDateTime startTime, int leg, boolean isRankedHeat, int heatNumber,
        List<Heat> heats,
        List<Team> teams) {
        int teamsSize = teams.size();
        double numberOfHeatsEachRoundForCurrentGroup =
            teamsSize / MAX_HEAT_SIZE + (teamsSize % MAX_HEAT_SIZE > 0 ? 1 : 0);
        int heatNumberCounter = heatNumber;

        List<Heat> addHeats = new ArrayList<>();
        while (addHeats.size() < numberOfHeatsEachRoundForCurrentGroup) {
            Heat heat = new Heat();
            heat.setHeatNumber(heatNumberCounter);
            if (isRankedHeat) {
                heat.setHeatName("Innledende");
            }
            heat.setStartTime(startTime.format(hourMinuteFormatter));
            heat.setPrologHeat(leg == 1);
            heat.setRankedHeat(isRankedHeat);
            heat.setTeams(new HashSet<>());
            addHeats.add(heat);
            heatNumberCounter++;
            startTime = startTime.plusMinutes(MINUTES_BETWEEN_HEATS);
        }

        sortTeamsByLeg(leg, teams);

        int heatIndex = 0;
        for (Team team : teams) {
            Heat heat = addHeats.get(heatIndex);
            heat.setGroupName(team.getGroupName());
            heat.addTeam(team);
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

    public List<Heat> registerRandomRankedResults(boolean isPrologHeat) {
        List<Heat> savedHeats = new ArrayList<>();

        List<Heat> heats = heatRepository.findByRankedHeatAndPrologHeat(true, isPrologHeat);

        heats.forEach(heat -> {
            Set<Team> teams = heat.getTeams();

            List<Integer> bibs = teams.stream().map(Team::getBib).collect(Collectors.toList());
            Collections.shuffle(bibs);

            Map<Integer, Integer> resultMap = new HashMap<>();
            bibs.forEach(bib -> resultMap.put(resultMap.size() + 1, bib));

            heat.setResult(convertToResultTeamMap(resultMap, teams));

            savedHeats.add(heatRepository.save(heat));
        });

        return savedHeats;
    }

    public List<Heat> deleteRankedResults(boolean isPrologHeat) {
        List<Heat> savedHeats = new ArrayList<>();

        List<Heat> heats = heatRepository.findByRankedHeatAndPrologHeat(true, isPrologHeat);

        heats.forEach(heat -> {
            heat.setResult(null);
            savedHeats.add(heatRepository.save(heat));
        });

        return savedHeats;
    }

}
