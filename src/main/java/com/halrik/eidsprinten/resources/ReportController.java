package com.halrik.eidsprinten.resources;

import com.halrik.eidsprinten.model.enums.Group;
import com.halrik.eidsprinten.services.ReportService;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports/")
public class ReportController {

    private ReportService reportService;

    private final DateTimeFormatter dateTimeFormatter;

    public ReportController(ReportService reportService, DateTimeFormatter dateTimeFormatter) {
        this.reportService = reportService;
        this.dateTimeFormatter = dateTimeFormatter;
    }

    @GetMapping(value = "/startlist/unranked", produces = "application/pdf")
    public ResponseEntity<InputStreamResource> startListUnranked() throws IOException {
        byte[] pdfBytes = reportService.generateStartListUnrankedPdf();
        String downloadFileName = "startliste-urangerte-" + LocalDateTime.now().format(dateTimeFormatter) + ".pdf";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfBytes);
        return ResponseEntity
            .ok()
            .headers(getHeaders(downloadFileName))
            .contentType(MediaType.APPLICATION_PDF)
            .body(new InputStreamResource(inputStream));
    }

    @GetMapping(value = "/startlist/ranked", produces = "application/pdf")
    public ResponseEntity<InputStreamResource> startListRankedProlog() throws IOException {
        byte[] pdfBytes = reportService.generateStartListRankedPdf();
        String downloadFileName = "startliste-rangerte-" + LocalDateTime.now().format(dateTimeFormatter) + ".pdf";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfBytes);
        return ResponseEntity
            .ok()
            .headers(getHeaders(downloadFileName))
            .contentType(MediaType.APPLICATION_PDF)
            .body(new InputStreamResource(inputStream));
    }

    @GetMapping(value = "/startlist/ranked/finals/", produces = "application/pdf")
    public ResponseEntity<InputStreamResource> startListRankedFinals(@RequestParam Group group) throws IOException {
        byte[] pdfBytes = reportService.generateStartListRankedFinalsPdf(group);
        String downloadFileName =
            "startliste-rangerte-finaler-" + group + "-" + LocalDateTime.now().format(dateTimeFormatter) + ".pdf";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfBytes);
        return ResponseEntity
            .ok()
            .headers(getHeaders(downloadFileName))
            .contentType(MediaType.APPLICATION_PDF)
            .body(new InputStreamResource(inputStream));
    }

    @GetMapping(value = "/startlist/ranked/results/", produces = "application/pdf")
    public ResponseEntity<InputStreamResource> resultList(@RequestParam Group group) throws IOException {
        byte[] pdfBytes = reportService.generateResultListPdf(group);
        String downloadFileName =
            "resultatliste-rangerte-" + group + "-" + LocalDateTime.now().format(dateTimeFormatter) + ".pdf";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfBytes);
        return ResponseEntity
            .ok()
            .headers(getHeaders(downloadFileName))
            .contentType(MediaType.APPLICATION_PDF)
            .body(new InputStreamResource(inputStream));
    }

    @GetMapping(value = "/advancement-setup", produces = "application/pdf")
    public ResponseEntity<InputStreamResource> advancementSetup() throws IOException {
        byte[] pdfBytes = reportService.generateAdvancementSetupPdf();
        String downloadFileName = "avansement-oversikt-" + LocalDateTime.now().format(dateTimeFormatter) + ".pdf";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfBytes);
        return ResponseEntity
            .ok()
            .headers(getHeaders(downloadFileName))
            .contentType(MediaType.APPLICATION_PDF)
            .body(new InputStreamResource(inputStream));
    }

    @GetMapping(value = "/startnumbers", produces = "application/pdf")
    public ResponseEntity<InputStreamResource> startNumbers() throws IOException {
        byte[] pdfBytes = reportService.generateStartNumbersPdf();
        String downloadFileName = "startnummer-liste-" + LocalDateTime.now().format(dateTimeFormatter) + ".pdf";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfBytes);
        return ResponseEntity
            .ok()
            .headers(getHeaders(downloadFileName))
            .contentType(MediaType.APPLICATION_PDF)
            .body(new InputStreamResource(inputStream));
    }

    @GetMapping(value = "/starttime", produces = "application/pdf")
    public ResponseEntity<InputStreamResource> startTime() throws IOException {
        byte[] pdfBytes = reportService.generateStartTimePdf();
        String downloadFileName = "start-tider-" + LocalDateTime.now().format(dateTimeFormatter) + ".pdf";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfBytes);
        return ResponseEntity
            .ok()
            .headers(getHeaders(downloadFileName))
            .contentType(MediaType.APPLICATION_PDF)
            .body(new InputStreamResource(inputStream));
    }

    @GetMapping(value = "/awardceremony", produces = "application/pdf")
    public ResponseEntity<InputStreamResource> awardCeremony() throws IOException {
        byte[] pdfBytes = reportService.generateAwardCeremonyPdf();
        String downloadFileName = "premieutdeling-" + LocalDateTime.now().format(dateTimeFormatter) + ".pdf";
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
