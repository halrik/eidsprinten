package com.halrik.eidsprinten.resources;

import com.halrik.eidsprinten.domain.Heat;
import com.halrik.eidsprinten.exception.ValidationException;
import com.halrik.eidsprinten.services.HeatsService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
public class ResultController {

    private HeatsService heatsService;

    public ResultController(HeatsService heatsService) {
        this.heatsService = heatsService;
    }

    @PostMapping(value = "/heats/ranked/{heatNumber}/result")
    public ResponseEntity updateResult(@PathVariable Integer heatNumber, @RequestParam Integer no1,
        @RequestParam(required = false) Integer no2, @RequestParam(required = false) Integer no3,
        @RequestParam(required = false) Integer no4, @RequestParam(required = false) Integer no5,
        @RequestParam(required = false) Integer no6, @RequestParam(required = false) Integer no7,
        @RequestParam(required = false) Integer no8, @RequestParam(required = false) Integer no9) {

        Map<Integer, Integer> resultMap = new HashMap<>();

        addResult(1, no1, resultMap);
        addResult(2, no2, resultMap);
        addResult(3, no3, resultMap);
        addResult(4, no4, resultMap);
        addResult(5, no5, resultMap);
        addResult(6, no6, resultMap);
        addResult(7, no7, resultMap);
        addResult(8, no8, resultMap);
        addResult(9, no9, resultMap);

        validateResultMap(resultMap);

        Heat heat = heatsService.registerResult(heatNumber, resultMap);

        return ResponseEntity.ok(heat);
    }

    @PostMapping(value = "/heats/ranked/randomresults")
    public ResponseEntity<List<Heat>> registerRandomRankedResults() {
        return new ResponseEntity<>(heatsService.registerRandomRankedResults(true), HttpStatus.OK);
    }

    @PostMapping(value = "/heats/ranked/finals/randomresults")
    public ResponseEntity<List<Heat>> registerRandomRankedFinalsResults() {
        return new ResponseEntity<>(heatsService.registerRandomRankedResults(false), HttpStatus.OK);
    }

    private void addResult(Integer result, Integer teamNumber, Map<Integer, Integer> resultMap) {
        if (teamNumber == null) {
            return;
        }
        resultMap.put(result, teamNumber);
    }

    private void validateResultMap(Map<Integer, Integer> resultMap) {
        Set<Integer> resultPlaceList = resultMap.keySet();
        for (int result = 2; result <= 9; result++) {
            if (resultPlaceList.contains(result) && !resultPlaceList.contains(result - 1)) {
                throw new ValidationException("Result " + (result - 1) + " is missing!");
            }
        }

        if (new HashSet<>(resultMap.values()).size() != resultMap.values().size()) {
            throw new ValidationException("Duplicate teams in result!");
        }
    }

}
