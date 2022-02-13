package com.halrik.eidsprinten.services;

import com.halrik.eidsprinten.domain.Heat;
import com.halrik.eidsprinten.domain.HeatAdvancement;
import com.halrik.eidsprinten.domain.Result;
import com.halrik.eidsprinten.domain.Team;
import com.halrik.eidsprinten.model.enums.Group;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.svgsupport.BatikSVGDrawer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReportService {

    private final EidsprintenService eidsprintenService;
    private final HeatsService heatsService;
    private final FinalHeatsService finalHeatsService;
    private final TemplateService templateService;
    private final PdfRendererBuilder pdfBuilder;

    public ReportService(EidsprintenService eidsprintenService,
        HeatsService heatsService, FinalHeatsService finalHeatsService,
        TemplateService templateService) {
        this.eidsprintenService = eidsprintenService;
        this.heatsService = heatsService;
        this.finalHeatsService = finalHeatsService;
        this.templateService = templateService;

        pdfBuilder = new PdfRendererBuilder()
            .useFastMode()
            .useFont(() -> getFontInputStream("OpenSans-Regular.ttf"), "Open Sans")
            .useSVGDrawer(new BatikSVGDrawer());

    }

    public byte[] generateStartListUnrankedPdf() throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            List<Heat> heatsUnRanked = heatsService.getHeatsUnRankedStored();

            pdfBuilder.withHtmlContent(templateService.getStartListHeatsHtml(heatsUnRanked, "Urangerte"), "")
                .toStream(outputStream)
                .run();

            return outputStream.toByteArray();
        }
    }

    public byte[] generateStartListRankedPdf() throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            List<Heat> heatsRanked = heatsService.getHeatsRankedStored();

            pdfBuilder.withHtmlContent(templateService.getStartListHeatsHtml(heatsRanked, "Rangerte"), "")
                .toStream(outputStream)
                .run();

            return outputStream.toByteArray();
        }
    }

    public byte[] generateStartListRankedFinalsPdf(Group group) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            List<Heat> heatsRankedFinals = finalHeatsService.getHeatsRankedFinals().stream()
                .filter(heat -> heat.getGroupName().equals(group.getValue())).collect(Collectors.toList());
            heatsRankedFinals.sort(Comparator.comparingInt(Heat::getHeatNumber));
            pdfBuilder.withHtmlContent(
                    templateService.getStartListFinalsHtml(heatsRankedFinals, "Finaler - " + group.getValue()), "")
                .toStream(outputStream)
                .run();

            return outputStream.toByteArray();
        }
    }

    public byte[] generateResultListPdf(Group group) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            List<Result> resultList = finalHeatsService.getHeatsRankedResults(group);
            pdfBuilder.withHtmlContent(
                    templateService.getResultListHtml(resultList, group.getValue()), "")
                .toStream(outputStream)
                .run();

            return outputStream.toByteArray();
        }
    }

    public byte[] generateStartNumbersPdf() throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Map<String, List<Team>> teamsGroupedByClub = eidsprintenService.getTeamsGroupedByClub();
            pdfBuilder.withHtmlContent(
                    templateService.getStartNumbersHtml(teamsGroupedByClub), "")
                .toStream(outputStream)
                .run();

            return outputStream.toByteArray();
        }
    }

    public byte[] generateAdvancementSetupPdf() throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            List<HeatAdvancement> advancementSetup = finalHeatsService.getAdvancementSetup();

            pdfBuilder.withHtmlContent(templateService.getAdvancementsSetupHtml(advancementSetup), "")
                .toStream(outputStream)
                .run();

            return outputStream.toByteArray();
        }
    }

    public byte[] generateStartTimePdf() throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Map<String, String> startTimeMap = heatsService.getStartTimeMap();
            pdfBuilder.withHtmlContent(
                    templateService.getStartTimeHtml(startTimeMap), "")
                .toStream(outputStream)
                .run();

            return outputStream.toByteArray();
        }
    }

    public byte[] generateAwardCeremonyPdf() throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Map<String, String> awardCeremonyTimeMap = heatsService.getAwardCeremonyTimeMap();
            pdfBuilder.withHtmlContent(
                    templateService.getAwardCeremonyHtml(awardCeremonyTimeMap), "")
                .toStream(outputStream)
                .run();

            return outputStream.toByteArray();
        }
    }


    protected InputStream getFontInputStream(String fileName) {
        return ReportService.class.getResourceAsStream("/fonts/" + fileName);
    }

}
