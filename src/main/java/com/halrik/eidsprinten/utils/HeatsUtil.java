package com.halrik.eidsprinten.utils;

import com.halrik.eidsprinten.domain.HeatAdvancement;
import com.halrik.eidsprinten.model.enums.Group;
import java.util.List;
import java.util.stream.Collectors;

public class HeatsUtil {

    public static List<HeatAdvancement> getAdvancementsForGroup(List<HeatAdvancement> advancements, Group group) {
        return advancements.stream()
            .filter(heatAdvancement -> heatAdvancement.getGroupName().equals(group.getValue()))
            .collect(Collectors.toList());
    }

}
