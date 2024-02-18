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
    public static final int INDEX_GROUP_NAME = 14;
    public static final int INDEX_LAST_NAME = 1;
    public static final int INDEX_FIRST_NAME = 0;
    public static final int INDEX_GENDER = 8;
    public static final int INDEX_TEAM_NAME = 11;
    public static final int INDEX_LEG = 12;
    public static final int INDEX_TEAM_LEADER_NAME = 20;
    public static final int INDEX_TEAM_LEADER_EMAIL = 21;

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
        int skipFirstRows = 1;
        int rowNumber = 0;

        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheetAt(0);
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
        participant.setGroupName(row.getCell(INDEX_GROUP_NAME).getStringCellValue().trim());
        participant.setLastName(row.getCell(INDEX_LAST_NAME).getStringCellValue().trim());
        participant.setFirstName(row.getCell(INDEX_FIRST_NAME).getStringCellValue().trim());

        participant.setGender(row.getCell(INDEX_GENDER).getStringCellValue());
        try {
            participant.setBirthDate(DATE_FORMAT_DOT.parse(row.getCell(6).getStringCellValue()));
        } catch (Exception ex) {
            participant.setBirthDate(DATE_FORMAT_SLASH.parse(row.getCell(6).getStringCellValue()));
        }
        participant.setClubName(extractClubName(row.getCell(INDEX_TEAM_NAME).getStringCellValue()));
        participant.setTeamName(row.getCell(INDEX_TEAM_NAME).getStringCellValue());

        try {
            participant.setLeg(Integer.valueOf(row.getCell(INDEX_LEG).getStringCellValue()));
        } catch (IllegalStateException ise) {
            participant.setLeg((int) row.getCell(INDEX_LEG).getNumericCellValue());
        }

        if (row.getCell(INDEX_TEAM_LEADER_NAME) != null) {
            participant.setTeamLeaderName(row.getCell(INDEX_TEAM_LEADER_NAME).getStringCellValue());
        }

        /*
        if (row.getCell(25) != null) {
            try {
                participant.setTeamLeaderPhone(row.getCell(25).getStringCellValue());
            } catch (IllegalStateException ise) {
                participant.setTeamLeaderPhone("" + row.getCell(25).getNumericCellValue());
            }
        }
         */

        if (row.getCell(INDEX_TEAM_LEADER_EMAIL) != null) {
            participant.setTeamLeaderEmail(row.getCell(INDEX_TEAM_LEADER_EMAIL).getStringCellValue());
        }

        participant.setAge(Integer.valueOf(
            participant.getGroupName()
                .replace("år", "")
                .replace("G", "")
                .replace("J", "")
                .replace("Felles", "")
                .replace(" ", "")));

        participant.setGenderClass(participant.getGroupName().substring(0, 1));

        return Optional.of(participant);
    }

    private static String extractClubName(String teamName) {
        return teamName.split(" -")[0];
    }

}
