package com.halrik.eidsprinten.excel;

import com.halrik.eidsprinten.domain.Participant;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class ExcelHelper {

    private static final Logger log = LoggerFactory.getLogger(ExcelHelper.class);

    private static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static String SHEET = "Deltakerliste Excel";

    private static SimpleDateFormat DATE_FORMAT_DOT = new SimpleDateFormat("dd.MM.yyyy");
    private static SimpleDateFormat DATE_FORMAT_SLASH = new SimpleDateFormat("dd/MM/yyyy");

    private ExcelHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static List<Participant> excelToParticipants(InputStream is) {
        int skipFirstRows = 3;
        int rowNumber = 0;

        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<Participant> participantList = new ArrayList<>();
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                rowNumber++;

                // skip first rows
                if (rowNumber <= skipFirstRows) {
                    continue;
                }

                log.info("Parsing row {}", rowNumber);

                rowToParticipant(currentRow).ifPresent(participant -> participantList.add(participant));
            }

            workbook.close();

            return participantList;
        } catch (IOException | ParseException | IllegalStateException e) {
            log.error("Failed to parse Excel file (row " + rowNumber + ")", e);
            throw new RuntimeException("Failed to parse Excel file (row " + rowNumber + "): " + e.getMessage());
        }
    }

    private static Optional<Participant> rowToParticipant(Row row) throws ParseException {
        Participant participant = new Participant();
        if (row.getCell(0) == null) {
            return Optional.empty();
        }
        participant.setGroupName(row.getCell(0).getStringCellValue().trim());
        participant.setLastName(row.getCell(4).getStringCellValue().trim());
        participant.setFirstName(row.getCell(5).getStringCellValue().trim());

        participant.setGender(row.getCell(6).getStringCellValue());
        try {
            participant.setBirthDate(DATE_FORMAT_DOT.parse(row.getCell(7).getStringCellValue()));
        } catch (Exception ex) {
            participant.setBirthDate(DATE_FORMAT_SLASH.parse(row.getCell(7).getStringCellValue()));
        }
        participant.setClubName(row.getCell(10).getStringCellValue());
        participant.setTeamName(row.getCell(15).getStringCellValue());

        try {
            participant.setLeg(Integer.valueOf(row.getCell(16).getStringCellValue()));
        } catch (IllegalStateException ise) {
            participant.setLeg((int) row.getCell(16).getNumericCellValue());
        }

        if (row.getCell(24) != null) {
            participant.setTeamLeaderName(row.getCell(24).getStringCellValue());
        }

        if (row.getCell(25) != null) {
            try {
                participant.setTeamLeaderPhone(row.getCell(25).getStringCellValue());
            } catch (IllegalStateException ise) {
                participant.setTeamLeaderPhone("" + row.getCell(25).getNumericCellValue());
            }
        }

        if (row.getCell(26) != null) {
            participant.setTeamLeaderEmail(row.getCell(26).getStringCellValue());
        }

        participant.setAge(Integer.valueOf(
            participant.getGroupName()
                .replace("år", "")
                .replace("G", "")
                .replace("J", "")
                .replace(" ", "")));

        participant.setGenderClass(participant.getGroupName().substring(0, 1));

        return Optional.of(participant);
    }

}
