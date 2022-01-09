package com.halrik.eidsprinten.resources;

import com.halrik.eidsprinten.services.ReportService;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports/")
public class ReportController {

    private static final Logger log = LoggerFactory.getLogger(ReportController.class);

    private ReportService reportService;

    private final DateTimeFormatter dateTimeFormatter;

    public ReportController(ReportService reportService, DateTimeFormatter dateTimeFormatter) {
        this.reportService = reportService;
        this.dateTimeFormatter = dateTimeFormatter;
    }

    @GetMapping(value = "/startlist/unranked", produces = "application/pdf")
    public ResponseEntity<InputStreamResource> startListUnranked() throws IOException {
        byte[] pdfBytes = reportService.generateStartListUnrankedPdf();
        String downloadFileName = "startliste-urangert-" + LocalDateTime.now().format(dateTimeFormatter) + ".pdf";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfBytes);
        return ResponseEntity
            .ok()
            .headers(getHeaders(downloadFileName))
            .contentType(MediaType.APPLICATION_PDF)
            .body(new InputStreamResource(inputStream));
    }

    @GetMapping(value = "/startlist/ranked/prolog", produces = "application/pdf")
    public ResponseEntity<InputStreamResource> startListRankedProlog() throws IOException {
        byte[] pdfBytes = reportService.generateStartListPrologRankedPdf();
        String downloadFileName = "startliste-rangert-prolog" + LocalDateTime.now().format(dateTimeFormatter) + ".pdf";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfBytes);
        return ResponseEntity
            .ok()
            .headers(getHeaders(downloadFileName))
            .contentType(MediaType.APPLICATION_PDF)
            .body(new InputStreamResource(inputStream));
    }

    private HttpHeaders getHeaders(String downloadFileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/pdf");
        headers.add("Content-Disposition", "attachment; filename=" + downloadFileName);
        return headers;
    }

}
