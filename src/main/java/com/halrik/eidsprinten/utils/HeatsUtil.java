package com.halrik.eidsprinten.utils;

import com.halrik.eidsprinten.domain.Heat;
import com.halrik.eidsprinten.domain.HeatAdvancement;
import com.halrik.eidsprinten.domain.Team;
import com.halrik.eidsprinten.model.enums.Group;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public class HeatsUtil {

    public static List<HeatAdvancement> getAdvancementsForGroup(List<HeatAdvancement> advancements, Group group) {
        return advancements.stream()
            .filter(heatAdvancement -> heatAdvancement.getGroupName().equals(group.getValue()))
            .collect(Collectors.toList());
    }

    public static List<Heat> sortTeamsWithinHeatByBibAndSortHeatsByHeatNumber(List<Heat> heats) {
        heats.forEach(heat ->
            heat.setTeams(heat.getTeams().stream().sorted(Comparator.comparingInt(Team::getBib))
                .collect(Collectors.toCollection(LinkedHashSet::new))));

        return heats.stream().sorted(Comparator.comparingInt(Heat::getHeatNumber)).collect(Collectors.toList());
    }

    public static List<Heat> getHeatsRankedFinalsForGroup(List<Heat> heatsRankedFinals, Group group) {
        return heatsRankedFinals.stream()
            .filter(heat -> heat.getGroupName().equals(group.getValue()))
            .collect(Collectors.toList());
    }

}
