package com.halrik.eidsprinten.resources;

import static javax.servlet.http.HttpServletResponse.SC_OK;

import com.halrik.eidsprinten.services.ReportService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(value = "/startlist/unranked", produces = "application/pdf")
    @ApiOperation(value = "Generates a PDF .",
        responseReference = "ResponseRepresentationOfOfferRepresentation"
    )
    @ApiResponses({
        @ApiResponse(code = SC_OK, message = "Successfully created PDF")
    })
    public ResponseEntity<InputStreamResource> startListUnranked() throws IOException {
        byte[] pdfBytes = reportService.generatePdf();

        String dateTimePattern = "dd-MM-yyyy-HHmm";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimePattern);
        String downloadFileName = "startlist-unranked-" + LocalDateTime.now().format(dateTimeFormatter) + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/pdf");
        headers.add("Content-Disposition", "attachment; filename=" + downloadFileName);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfBytes);
        return ResponseEntity
            .ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_PDF)
            .body(new InputStreamResource(inputStream));


    }

}
