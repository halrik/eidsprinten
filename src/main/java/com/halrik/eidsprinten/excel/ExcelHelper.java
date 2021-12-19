package com.halrik.eidsprinten.excel;

import com.halrik.eidsprinten.domain.Participant;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class ExcelHelper {

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String SHEET = "Deltakerliste Excel";

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

                // skip first rows
                if (rowNumber < skipFirstRows) {
                    rowNumber++;
                    continue;
                }

                Participant participant = new Participant();
                participant.setGroup(currentRow.getCell(0).getStringCellValue());
                participant.setLastName(currentRow.getCell(4).getStringCellValue());
                participant.setFirstName(currentRow.getCell(5).getStringCellValue());
                participantList.add(participant);
            }

            workbook.close();

            return participantList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

}
