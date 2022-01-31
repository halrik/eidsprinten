package com.halrik.eidsprinten.services;

import com.halrik.eidsprinten.domain.Heat;
import com.halrik.eidsprinten.domain.HeatAdvancement;
import com.halrik.eidsprinten.domain.Result;
import com.halrik.eidsprinten.domain.Team;
import com.halrik.eidsprinten.model.enums.FinalHeat;
import com.halrik.eidsprinten.model.enums.Group;
import com.halrik.eidsprinten.repository.HeatRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class FinalHeatsService {

    private HeatRepository heatRepository;
    private HeatsService heatsService;

    public FinalHeatsService(HeatRepository heatRepository, HeatsService heatsService) {
        this.heatRepository = heatRepository;
        this.heatsService = heatsService;
    }

    public List<HeatAdvancement> getAdvancementSetup() {
        List<HeatAdvancement> heatAdvancements = new ArrayList<>();

        fillAdvancementForGroup(heatAdvancements, Group.BOYS_11);
        fillAdvancementForGroup(heatAdvancements, Group.GIRLS_11);
        fillAdvancementForGroup(heatAdvancements, Group.BOYS_12);
        fillAdvancementForGroup(heatAdvancements, Group.GIRLS_12);
        fillAdvancementForGroup(heatAdvancements, Group.BOYS_13);
        fillAdvancementForGroup(heatAdvancements, Group.GIRLS_13);
        fillAdvancementForGroup(heatAdvancements, Group.BOYS_14);
        fillAdvancementForGroup(heatAdvancements, Group.GIRLS_14);

        heatAdvancements.sort(Comparator.comparingInt(HeatAdvancement::getFromHeatNumber));

        return heatAdvancements;
    }

    private void fillAdvancementForGroup(List<HeatAdvancement> heatAdvancements, Group group) {
        List<Heat> prologHeats = heatRepository.findByGroupNameAndPrologHeat(group.getValue(),
            true);

        int numberOfPrologHeats = prologHeats.size();
        int numberOfTeams = getNumberOfTeamsByAgeGroupInPrologHeats(prologHeats);

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
                        prologHeat.getHeatName(), finalHeat.getHeatName(), prologHeat.getGroupName()));
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

    public List<Heat> getHeatsRankedFinalsAndSave() {
        heatsService.deleteHeats(true, false);
        return heatRepository.saveAll(getHeatsRankedFinals());
    }

    public List<Heat> getHeatsRankedFinals() {
        List<Heat> finalHeats = new ArrayList<>();

        List<HeatAdvancement> advancementSetup = getAdvancementSetup();

        finalHeats.addAll(updateFinalHeats(Group.BOYS_11, filterAdvancementSetup(advancementSetup, Group.BOYS_11)));
        finalHeats.addAll(updateFinalHeats(Group.GIRLS_11, filterAdvancementSetup(advancementSetup, Group.GIRLS_11)));
        finalHeats.addAll(updateFinalHeats(Group.BOYS_12, filterAdvancementSetup(advancementSetup, Group.BOYS_12)));
        finalHeats.addAll(updateFinalHeats(Group.GIRLS_12, filterAdvancementSetup(advancementSetup, Group.GIRLS_12)));
        finalHeats.addAll(updateFinalHeats(Group.BOYS_13, filterAdvancementSetup(advancementSetup, Group.BOYS_13)));
        finalHeats.addAll(updateFinalHeats(Group.GIRLS_13, filterAdvancementSetup(advancementSetup, Group.GIRLS_13)));
        finalHeats.addAll(updateFinalHeats(Group.BOYS_14, filterAdvancementSetup(advancementSetup, Group.BOYS_14)));
        finalHeats.addAll(updateFinalHeats(Group.GIRLS_14, filterAdvancementSetup(advancementSetup, Group.GIRLS_14)));

        return finalHeats;
    }

    public List<Result> getHeatsRankedResults(Group group) {
        List<Result> resultList = new ArrayList<>();
        List<Heat> finalHeats = heatRepository.findByGroupNameAndPrologHeat(group.getValue(), false);

        AtomicInteger result = new AtomicInteger(1);
        finalHeats.forEach(heat -> heat.getResult()
            .forEach((resultInHeat, team) -> {
                resultList.add(new Result(result.get(), team));
                result.getAndIncrement();
            }));

        return resultList;
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
