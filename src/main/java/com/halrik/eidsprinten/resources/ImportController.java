package com.halrik.eidsprinten.resources;

import com.halrik.eidsprinten.domain.Participant;
import com.halrik.eidsprinten.excel.ExcelHelper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/excel")
public class ImportController {

    private static final Logger log = LoggerFactory.getLogger(ImportController.class);

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseMessage> uploadFile(@RequestPart("file") MultipartFile file) {
        log.info("Uploading file {}", file.getName());

        if (ExcelHelper.hasExcelFormat(file)) {
            try {
                List<Participant> participantList = ExcelHelper.excelToParticipants(file.getInputStream());

                return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage("Uploaded the file successfully: " + file.getOriginalFilename()));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseMessage("Could not upload the file: " + file.getOriginalFilename() + "!"));
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Please upload an excel file!"));
    }
}