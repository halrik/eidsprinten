package com.halrik.eidsprinten.services;

import com.halrik.eidsprinten.domain.Heat;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.svgsupport.BatikSVGDrawer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReportService {

    private final HeatsService heatsService;
    private final TemplateService templateService;
    private final PdfRendererBuilder pdfBuilder;

    public ReportService(HeatsService heatsService, TemplateService templateService) {
        this.heatsService = heatsService;
        this.templateService = templateService;

        pdfBuilder = new PdfRendererBuilder()
            .useFastMode()
            .useFont(() -> getFontInputStream("OpenSans-Regular.ttf"), "Open Sans")
            .useSVGDrawer(new BatikSVGDrawer());

    }

    public byte[] generateStartListUnrankedPdf() throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            List<Heat> heatsUnRanked = heatsService.getHeatsUnRanked();

            pdfBuilder.withHtmlContent(templateService.getStartListHeatsHtml(heatsUnRanked, "Urangerte"), "")
                .toStream(outputStream)
                .run();

            return outputStream.toByteArray();
        }
    }

    public byte[] generateStartListRankedPdf() throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            List<Heat> heatsRanked = heatsService.getHeatsRanked();

            pdfBuilder.withHtmlContent(templateService.getStartListHeatsHtml(heatsRanked, "Rangerte"), "")
                .toStream(outputStream)
                .run();

            return outputStream.toByteArray();
        }
    }

    protected InputStream getFontInputStream(String fileName) {
        return ReportService.class.getResourceAsStream("/fonts/" + fileName);
    }


}
