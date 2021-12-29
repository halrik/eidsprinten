package com.halrik.eidsprinten.excel;

import com.halrik.eidsprinten.domain.Participant;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    private ExcelHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static List<Participant> excelToParticipants(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<Participant> participantList = new ArrayList<>();

            int skipFirstRows = 3;

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                rowNumber++;

                // skip first rows
                if (rowNumber <= skipFirstRows) {
                    continue;
                }

                log.info("Parsing row {}", rowNumber);

                participantList.add(rowToParticipant(currentRow));
            }

            workbook.close();

            return participantList;
        } catch (IOException | ParseException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    private static Participant rowToParticipant(Row row) throws ParseException {
        Participant participant = new Participant();
        participant.setGroupName(row.getCell(0).getStringCellValue());
        participant.setLastName(row.getCell(4).getStringCellValue());
        participant.setFirstName(row.getCell(5).getStringCellValue());

        participant.setGender(row.getCell(6).getStringCellValue());
        participant.setBirthDate(DATE_FORMAT.parse(row.getCell(7).getStringCellValue()));
        participant.setClub(row.getCell(10).getStringCellValue());
        participant.setTeam(row.getCell(15).getStringCellValue());
        participant.setLeg(Integer.valueOf(row.getCell(16).getStringCellValue()));

        if (row.getCell(23) != null) {
            participant.setTeamLeaderName(row.getCell(23).getStringCellValue());
        }

        if (row.getCell(24) != null) {
            participant.setTeamLeaderPhone(row.getCell(24).getStringCellValue());
        }

        if (row.getCell(25) != null) {
            participant.setTeamLeaderEmail(row.getCell(25).getStringCellValue());
        }

        return participant;
    }

}
