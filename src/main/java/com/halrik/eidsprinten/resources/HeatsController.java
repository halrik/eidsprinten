package com.halrik.eidsprinten.resources;

import com.halrik.eidsprinten.domain.Heat;
import com.halrik.eidsprinten.domain.HeatAdvancement;
import com.halrik.eidsprinten.services.FinalHeatsService;
import com.halrik.eidsprinten.services.HeatsService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/heats")
public class HeatsController {

    private HeatsService heatsService;
    private FinalHeatsService finalHeatsService;

    public HeatsController(HeatsService heatsService,
        FinalHeatsService finalHeatsService) {
        this.heatsService = heatsService;
        this.finalHeatsService = finalHeatsService;
    }

    @GetMapping(value = "/unranked")
    public ResponseEntity<List<Heat>> getHeatsUnRanked() {
        return new ResponseEntity<>(heatsService.getHeatsUnRanked(), HttpStatus.OK);
    }

    @GetMapping(value = "/unranked/save")
    public ResponseEntity<List<Heat>> getHeatsUnRankedAndSave() {
        return new ResponseEntity<>(heatsService.getHeatsUnRankedAndSave(), HttpStatus.OK);
    }

    @GetMapping(value = "/ranked/")
    public ResponseEntity<List<Heat>> getHeatsRanked() {
        return new ResponseEntity<>(heatsService.getHeatsRanked(), HttpStatus.OK);
    }

    @GetMapping(value = "/ranked/save")
    public ResponseEntity<List<Heat>> getHeatsRankedAndSave() {
        return new ResponseEntity<>(heatsService.getHeatsRankedAndSave(), HttpStatus.OK);
    }

    @GetMapping(value = "/ranked/advancement")
    public ResponseEntity<List<HeatAdvancement>> getHeatsRankedAdvancement() {
        return new ResponseEntity<>(finalHeatsService.getAdvancementSetup(), HttpStatus.OK);
    }

    @GetMapping(value = "/ranked/finals")
    public ResponseEntity<List<Heat>> getHeatsRankedFinals() {
        return new ResponseEntity<>(finalHeatsService.getHeatsRankedFinals(), HttpStatus.OK);
    }

    @GetMapping(value = "/ranked/finals/save")
    public ResponseEntity<List<Heat>> getHeatsRankedFinalsAndSave() {
        return new ResponseEntity<>(finalHeatsService.getHeatsRankedFinalsAndSave(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/")
    public ResponseEntity<String> deleteAllHeats() {
        heatsService.deleteAllHeats();
        return new ResponseEntity<>("All heats deleted", HttpStatus.OK);
    }

}
