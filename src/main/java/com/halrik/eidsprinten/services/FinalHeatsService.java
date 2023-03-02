package com.halrik.eidsprinten.services;

import static com.halrik.eidsprinten.domain.Result.DNF;
import static com.halrik.eidsprinten.domain.Result.DNS;
import static com.halrik.eidsprinten.utils.HeatsUtil.sortTeamsWithinHeatByBibAndSortHeatsByHeatNumber;

import com.halrik.eidsprinten.domain.Heat;
import com.halrik.eidsprinten.domain.HeatAdvancement;
import com.halrik.eidsprinten.domain.Result;
import com.halrik.eidsprinten.domain.Team;
import com.halrik.eidsprinten.model.enums.FinalHeat;
import com.halrik.eidsprinten.model.enums.Group;
import com.halrik.eidsprinten.repository.HeatRepository;
import com.halrik.eidsprinten.repository.TeamRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class FinalHeatsService {

    private HeatRepository heatRepository;
    private TeamRepository teamRepository;

    public FinalHeatsService(HeatRepository heatRepository,
        TeamRepository teamRepository) {
        this.heatRepository = heatRepository;
        this.teamRepository = teamRepository;
    }

    public List<HeatAdvancement> getAdvancementSetup() {
        List<HeatAdvancement> heatAdvancements = new ArrayList<>();

        fillAdvancementForGroup(heatAdvancements, Group.BOYS_11);
        fillAdvancementForGroup(heatAdvancements, Group.GIRLS_11);
        fillAdvancementForGroup(heatAdvancements, Group.BOYS_12);
        fillAdvancementForGroup(heatAdvancements, Group.GIRLS_12);
        fillAdvancementForGroup(heatAdvancements, Group.MIXED_13);
        fillAdvancementForGroup(heatAdvancements, Group.MIXED_14);

        heatAdvancements.sort(Comparator.comparingInt(HeatAdvancement::getFromHeatNumber));

        return heatAdvancements;
    }

    private void fillAdvancementForGroup(List<HeatAdvancement> heatAdvancements, Group group) {
        List<Heat> prologHeats = heatRepository.findByGroupNameAndPrologHeat(group.getValue(),
            true);

        int numberOfPrologHeats = prologHeats.size();
        int numberOfTeams = getNumberOfTeamsByAgeGroupInPrologHeats(prologHeats);

        if (numberOfTeams == 0) {
            return;
        }

        int maxNumberOfTeamsInOneHeat =
            numberOfTeams / numberOfPrologHeats + (numberOfTeams % numberOfPrologHeats > 0 ? 1 : 0);

        int numberOfTeamsAdvancingToEachFinal = maxNumberOfTeamsInOneHeat / numberOfPrologHeats
            + (maxNumberOfTeamsInOneHeat % numberOfPrologHeats > 0 ? 1 : 0);

        for (int i = 1; i <= numberOfPrologHeats; i++) {
            switch (i) {
                case 1:
                    fillAdvancementToFinalHeat(getFinalHeat(group, FinalHeat.A_FINAL_HEAT), heatAdvancements,
                        prologHeats, numberOfTeamsAdvancingToEachFinal, 1);
                    break;
                case 2:
                    fillAdvancementToFinalHeat(getFinalHeat(group, FinalHeat.B_FINAL_HEAT), heatAdvancements,
                        prologHeats, numberOfTeamsAdvancingToEachFinal, numberOfTeamsAdvancingToEachFinal * 1 + 1);
                    break;
                case 3:
                    fillAdvancementToFinalHeat(getFinalHeat(group, FinalHeat.C_FINAL_HEAT), heatAdvancements,
                        prologHeats, numberOfTeamsAdvancingToEachFinal, numberOfTeamsAdvancingToEachFinal * 2 + 1);
                    break;
                case 4:
                    fillAdvancementToFinalHeat(getFinalHeat(group, FinalHeat.D_FINAL_HEAT), heatAdvancements,
                        prologHeats, numberOfTeamsAdvancingToEachFinal, numberOfTeamsAdvancingToEachFinal * 3 + 1);
                    break;
                case 5:
                    fillAdvancementToFinalHeat(getFinalHeat(group, FinalHeat.E_FINAL_HEAT), heatAdvancements,
                        prologHeats, numberOfTeamsAdvancingToEachFinal, numberOfTeamsAdvancingToEachFinal * 4 + 1);
                    break;
                default:
                    throw new IllegalStateException("Case for " + i + " is not supported!");
            }
        }

        // fix to avoid A final with 9 teams, B final with 9 teams and C final with 3 teams
        // with this we will get A final with 9 teams, B final with 6 teams and C final with 6 teams
        List<HeatAdvancement> boys11ToC = heatAdvancements.stream().filter(
            heatAdvancement -> heatAdvancement.getGroupName().equals(Group.BOYS_11.getValue())
                && heatAdvancement.getResult() == 6).collect(Collectors.toList());

        boys11ToC.forEach(heatAdvancement -> {
            heatAdvancement.setToHeatName("C - finale");
            heatAdvancement.setToHeatNumber(33);
        });
    }

    private Optional<Heat> getFinalHeat(Group group, FinalHeat finalHeat) {
        return heatRepository.findByGroupNameAndHeatName(group.getValue(), finalHeat.getValue()).stream().findFirst();
    }

    private void fillAdvancementToFinalHeat(Optional<Heat> finalHeatOptional, List<HeatAdvancement> heatAdvancements,
        List<Heat> prologHeats, int numberOfTeamsAdvancing, int startFromResult) {

        finalHeatOptional.ifPresent(finalHeat -> prologHeats.forEach(prologHeat -> {
            for (int result = startFromResult; result < startFromResult + numberOfTeamsAdvancing; result++) {
                if (prologHeat.getTeams().size() < result) {
                    break;
                }
                heatAdvancements.add(
                    new HeatAdvancement(result, prologHeat.getHeatNumber(), finalHeat.getHeatNumber(),
                        prologHeat.getHeatName(), finalHeat.getHeatName(), prologHeat.getGroupName(),
                        prologHeat.getTeams().size()));
            }
        }));
    }

    private int getNumberOfTeamsByAgeGroupInPrologHeats(List<Heat> prologHeats) {
        AtomicInteger numberOfTeamsInProlog = new AtomicInteger();
        prologHeats.stream()
            .map(Heat::getTeams)
            .collect(Collectors.toList())
            .forEach(teamList -> numberOfTeamsInProlog.set(numberOfTeamsInProlog.get() + teamList.size()));
        return numberOfTeamsInProlog.get();
    }

    public List<Heat> getHeatsRankedFinalsStored() {
        List<Heat> rankedFinalHeats = heatRepository.findByRankedHeatAndPrologHeat(true, false);
        return sortTeamsWithinHeatByBibAndSortHeatsByHeatNumber(rankedFinalHeats);
    }

    public List<Heat> getHeatsRankedFinalsAndSave() {
        return heatRepository.saveAll(getHeatsRankedFinals());
    }

    public List<Heat> getHeatsRankedFinals() {
        List<Heat> finalHeats = new ArrayList<>();

        List<HeatAdvancement> advancementSetup = getAdvancementSetup();

        finalHeats.addAll(updateFinalHeats(Group.BOYS_11, filterAdvancementSetup(advancementSetup, Group.BOYS_11)));
        finalHeats.addAll(updateFinalHeats(Group.GIRLS_11, filterAdvancementSetup(advancementSetup, Group.GIRLS_11)));
        finalHeats.addAll(updateFinalHeats(Group.BOYS_12, filterAdvancementSetup(advancementSetup, Group.BOYS_12)));
        finalHeats.addAll(updateFinalHeats(Group.GIRLS_12, filterAdvancementSetup(advancementSetup, Group.GIRLS_12)));
        finalHeats.addAll(updateFinalHeats(Group.MIXED_13, filterAdvancementSetup(advancementSetup, Group.MIXED_13)));
        finalHeats.addAll(updateFinalHeats(Group.MIXED_14, filterAdvancementSetup(advancementSetup, Group.MIXED_14)));

        return finalHeats;
    }

    public List<Result> getHeatsRankedResults(Group group) {
        List<Result> resultList = new ArrayList<>();

        List<Heat> finalHeats;
        if (Group.BOYS_13.equals(group) || Group.GIRLS_13.equals(group)) {
            finalHeats = heatRepository.findByGroupNameAndPrologHeat(Group.MIXED_13.getValue(), false);
        } else if (Group.BOYS_14.equals(group) || Group.GIRLS_14.equals(group)) {
            finalHeats = heatRepository.findByGroupNameAndPrologHeat(Group.MIXED_14.getValue(), false);
        } else {
            finalHeats = heatRepository.findByGroupNameAndPrologHeat(group.getValue(), false);
        }

        List<Heat> finalHeatsReverseOrder = finalHeats.stream()
            .sorted((h1, h2) -> Long.compare(h2.getHeatNumber(), h1.getHeatNumber())).collect(Collectors.toList());

        AtomicInteger result = new AtomicInteger(1);
        finalHeatsReverseOrder.forEach(heat -> heat.getResult()
            .forEach((resultInHeat, team) -> {
                resultList.add(new Result(result.toString(), team));

                result.getAndIncrement();
            }));

        List<Heat> prologHeats;
        if (Group.BOYS_13.equals(group) || Group.GIRLS_13.equals(group)) {
            prologHeats = heatRepository.findByGroupNameAndPrologHeat(Group.MIXED_13.getValue(), true);
        } else if (Group.BOYS_14.equals(group) || Group.GIRLS_14.equals(group)) {
            prologHeats = heatRepository.findByGroupNameAndPrologHeat(Group.MIXED_14.getValue(), true);
        } else {
            prologHeats = heatRepository.findByGroupNameAndPrologHeat(group.getValue(), true);
        }

        if (!CollectionUtils.isEmpty(prologHeats)) {
            List<Team> teams = teamRepository.findByGroupName(group.getValue());
            teams.forEach(team -> {
                boolean hasAnyPrologHeatsNoResult = prologHeats.stream()
                    .anyMatch(heat -> heat.getResult().isEmpty());

                if (!hasAnyPrologHeatsNoResult) {
                    Optional<Heat> prologResult = prologHeats.stream()
                        .filter(heat -> heat.getResult().values().contains(team))
                        .findFirst();
                    if (prologResult.isEmpty()) {
                        resultList.add(new Result(DNS, team));
                    } else {
                        boolean hasAnyFinalHeatsNoResult = finalHeatsReverseOrder.stream()
                            .anyMatch(heat -> heat.getResult().isEmpty());
                        if (!hasAnyFinalHeatsNoResult) {
                            Optional<Heat> finalResult = finalHeatsReverseOrder.stream()
                                .filter(heat -> heat.getResult().values().contains(team))
                                .findFirst();
                            if (finalResult.isEmpty()) {
                                resultList.add(new Result(Result.DNF, team));
                            }
                        }
                    }
                }
            });
        }

        if (Group.BOYS_13.equals(group) || Group.GIRLS_13.equals(group) ||
            Group.BOYS_14.equals(group) || Group.GIRLS_14.equals(group)) {
            filterMixedResultList(resultList, group);
        }

        return resultList;
    }

    private void filterMixedResultList(List<Result> resultList, Group group) {
        List<Result> resultsToRemove = resultList.stream().filter(result ->
            !result.getTeam().getGroupName().equals(group.getValue())
        ).collect(Collectors.toList());

        resultsToRemove.forEach(resultToRemove -> resultList.remove(resultToRemove));

        AtomicInteger result = new AtomicInteger(1);
        resultList.forEach(groupResult -> {
            String resultValue = groupResult.getResult();
            if (!DNS.equals(resultValue) && !DNF.equals(resultValue)) {
                groupResult.setResult(result.toString());
                result.getAndIncrement();
            }
        });
    }

    private List<Heat> updateFinalHeats(Group ageGroup, List<HeatAdvancement> advancementSetup) {
        List<Heat> prologHeats = heatRepository.findByGroupNameAndPrologHeat(ageGroup.getValue(), true);
        List<Heat> finalHeats = heatRepository.findByGroupNameAndPrologHeat(ageGroup.getValue(), false);

        advancementSetup.forEach(heatAdvancement -> {
            // put team with result in prolog heat to correct final heat
            Heat fromHeat = prologHeats.stream()
                .filter(heat -> heat.getHeatNumber().equals(heatAdvancement.getFromHeatNumber()))
                .findFirst().orElseThrow(() -> new IllegalStateException("From heat is not present!"));
            Heat toHeat = finalHeats.stream()
                .filter(heat -> heat.getHeatNumber().equals(heatAdvancement.getToHeatNumber()))
                .findFirst().orElseThrow(() -> new IllegalStateException("To heat is not present!"));

            Team teamWithResultInProlog = fromHeat.getResult().get(heatAdvancement.getResult());
            if (teamWithResultInProlog != null) {
                Set<Team> teams = toHeat.getTeams();
                if (teams.stream().noneMatch(team -> team.getId().equals(teamWithResultInProlog.getId()))) {
                    teams.add(teamWithResultInProlog);
                }
            }
        });

        return finalHeats;
    }

    private List<HeatAdvancement> filterAdvancementSetup(List<HeatAdvancement> advancementSetup, Group group) {
        return advancementSetup.stream()
            .filter(heatAdvancement -> group.getValue().equals(heatAdvancement.getGroupName()))
            .collect(Collectors.toList());
    }

}