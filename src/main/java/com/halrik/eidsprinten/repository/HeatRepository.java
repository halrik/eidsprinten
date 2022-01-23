package com.halrik.eidsprinten.repository;

import com.halrik.eidsprinten.domain.Heat;
import com.halrik.eidsprinten.domain.Team;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeatRepository extends JpaRepository<Heat, Integer> {

    List<Heat> findByRankedHeatAndPrologHeat(boolean isRanked, boolean isPrologHeat);

    List<Heat> findByGroupNameAndPrologHeat(String groupName, boolean isPrologHeat);

    List<Heat> findByGroupNameAndHeatName(String groupName, String heatName);

}
