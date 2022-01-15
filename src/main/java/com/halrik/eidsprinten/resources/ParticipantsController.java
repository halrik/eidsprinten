package com.halrik.eidsprinten.resources;

import com.halrik.eidsprinten.domain.Participant;
import com.halrik.eidsprinten.services.EidsprintenService;
import com.halrik.eidsprinten.services.HeatsService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/participants")
public class ParticipantsController {

    private static final Logger log = LoggerFactory.getLogger(ParticipantsController.class);

    private EidsprintenService eidsprintenService;

    public ParticipantsController(EidsprintenService eidsprintenService,
        HeatsService heatsService) {
        this.eidsprintenService = eidsprintenService;
    }

    @GetMapping(value = "/club/{clubName}")
    public ResponseEntity<List<Participant>> getParticipantsByClub(@PathVariable String clubName) {
        return new ResponseEntity<>(eidsprintenService.getParticipantsByClub(clubName), HttpStatus.OK);
    }

}