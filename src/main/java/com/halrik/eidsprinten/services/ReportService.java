package com.halrik.eidsprinten.services;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.svgsupport.BatikSVGDrawer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReportService {

    private final TemplateService templateService;
    private final PdfRendererBuilder pdfBuilder;

    public ReportService(TemplateService templateService) {
        this.templateService = templateService;

        pdfBuilder = new PdfRendererBuilder()
            .useFastMode()
            .useFont(() -> getFontInputStream("OpenSans-Regular.ttf"), "Open Sans")
            .useSVGDrawer(new BatikSVGDrawer());

    }

    public byte[] generateStartListUnrankedPdf() throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            pdfBuilder.withHtmlContent(templateService.getStartListUnrankedHtml(), "")
                .toStream(outputStream)
                .run();

            return outputStream.toByteArray();
        }
    }

    protected InputStream getFontInputStream(String fileName) {
        return ReportService.class.getResourceAsStream("/fonts/" + fileName);
    }


}
