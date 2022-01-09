package com.halrik.eidsprinten.resources;

import com.halrik.eidsprinten.domain.Heat;
import com.halrik.eidsprinten.domain.Participant;
import com.halrik.eidsprinten.domain.Team;
import com.halrik.eidsprinten.excel.ExcelHelper;
import com.halrik.eidsprinten.services.EidsprintenService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/")
public class EidsprintenController {

    private static final Logger log = LoggerFactory.getLogger(EidsprintenController.class);

    private EidsprintenService eidsprintenService;

    public EidsprintenController(EidsprintenService eidsprintenService) {
        this.eidsprintenService = eidsprintenService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseMessage> uploadFile(@RequestPart("file") MultipartFile file) {
        log.info("Uploading file {}", file.getName());

        if (ExcelHelper.hasExcelFormat(file)) {
            try {
                List<Participant> participantList = ExcelHelper.excelToParticipants(file.getInputStream());

                eidsprintenService.saveParticipantsAndTeams(participantList);

                return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage("Uploaded the file successfully: " + file.getOriginalFilename()));
            } catch (Exception e) {
                log.error("Could not upload the file!", e);
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseMessage("Could not upload the file: " + e.getMessage() + "!"));
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Please upload an excel file!"));
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

    @GetMapping(value = "/participants/club/{clubName}")
    public ResponseEntity<List<Participant>> getParticipantsByClub(@PathVariable String clubName) {
        return new ResponseEntity<>(eidsprintenService.getParticipantsByClub(clubName), HttpStatus.OK);
    }

    @GetMapping(value = "/teams/{age}")
    public ResponseEntity<List<Team>> getTeamsByAge(@PathVariable Integer age) {
        return new ResponseEntity<>(eidsprintenService.getTeamsByAge(age), HttpStatus.OK);
    }

    @GetMapping(value = "/heats/unranked")
    public ResponseEntity<List<Heat>> getHeatsUnRanked() {
        return new ResponseEntity<>(eidsprintenService.getHeatsUnRanked(), HttpStatus.OK);
    }

}