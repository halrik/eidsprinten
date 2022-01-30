package com.halrik.eidsprinten.resources;

import com.halrik.eidsprinten.domain.Team;
import com.halrik.eidsprinten.services.EidsprintenService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
public class TeamsController {

    private static final Logger log = LoggerFactory.getLogger(TeamsController.class);

    private EidsprintenService eidsprintenService;

    public TeamsController(EidsprintenService eidsprintenService) {
        this.eidsprintenService = eidsprintenService;
    }

    @GetMapping(value = "/teams/validate")
    public ResponseEntity<ResponseMessage> validateTeams() {
        try {
            int numberOfTeams = eidsprintenService.validateTeams();

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(numberOfTeams + " teams are valid!"));
        } catch (Exception e) {
            log.error("Teams are not valid!", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                .body(new ResponseMessage("Teams are not valid: " + e.getMessage() + "!"));
        }
    }

    @GetMapping(value = "/{age}")
    public ResponseEntity<List<Team>> getTeamsByAge(@PathVariable Integer age) {
        return new ResponseEntity<>(eidsprintenService.getTeamsByAge(age), HttpStatus.OK);
    }

    @PostMapping(value = "/bibs/allocate")
    public ResponseEntity allocateBibs() {
        eidsprintenService.allocateBibs();
        return ResponseEntity.ok("Bibs allocated");
    }

}
