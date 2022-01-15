package com.halrik.eidsprinten.resources;

import com.halrik.eidsprinten.domain.Heat;
import com.halrik.eidsprinten.services.HeatsService;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
public class HeatsController {

    private static final Logger log = LoggerFactory.getLogger(HeatsController.class);

    private HeatsService heatsService;

    public HeatsController(HeatsService heatsService) {
        this.heatsService = heatsService;
    }

    @GetMapping(value = "/heats/unranked")
    public ResponseEntity<List<Heat>> getHeatsUnRanked() {
        return new ResponseEntity<>(heatsService.getHeatsUnRanked(), HttpStatus.OK);
    }

    @GetMapping(value = "/heats/ranked/prolog")
    public ResponseEntity<List<Heat>> getHeatsRankedProlog() throws IOException {
        return new ResponseEntity<>(heatsService.getHeatsRankedProlog(), HttpStatus.OK);
    }


}
