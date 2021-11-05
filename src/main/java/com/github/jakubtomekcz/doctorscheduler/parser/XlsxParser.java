package com.github.jakubtomekcz.doctorscheduler.parser;

import com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType;
import com.github.jakubtomekcz.doctorscheduler.error.UiMessageException;
import com.github.jakubtomekcz.doctorscheduler.model.Date;
import com.github.jakubtomekcz.doctorscheduler.model.Person;
import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTable;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.github.jakubtomekcz.doctorscheduler.error.UiMessageException.MessageCode.ERROR_READING_XLSX_FILE;
import static com.github.jakubtomekcz.doctorscheduler.error.UiMessageException.MessageCode.XLSX_FILE_DATE_EXPECTED;
import static com.github.jakubtomekcz.doctorscheduler.error.UiMessageException.MessageCode.XLSX_FILE_PERSON_NAME_TOO_LONG;
import static com.github.jakubtomekcz.doctorscheduler.error.UiMessageException.MessageCode.XLSX_FILE_PREFERENCE_EXPECTED;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted;

@Slf4j
public class XlsxParser implements PreferenceTableParser {

    private static final int PERSON_MAX_LENGTH = 100;

    @Override
    public PreferenceTable parseMultipartFile(MultipartFile multipartFile) {
        Workbook workbook = createWorkBook(multipartFile);
        Sheet sheet = workbook.getSheetAt(0);
        PreferenceTable.Builder builder = PreferenceTable.builder();
        Map<Integer, Date> datesRow = null;
        for (Row row : sheet) {
            if (datesRow == null) {
                datesRow = readDatesRow(row);
            } else {
                Cell personCell = row.getCell(0);
                if (personCell == null || isBlank(personCell.getStringCellValue())) {
                    break;
                }
                Person person = validateAndGetPerson(personCell);
                for (Map.Entry<Integer, Date> entry : datesRow.entrySet()) {
                    int columnIndex = entry.getKey();
                    Date date = entry.getValue();
                    String cellValue = row.getCell(columnIndex).getStringCellValue();
                    PreferenceType preference;
                    try {
                        preference = PreferenceType.fromString(cellValue);
                    } catch (IllegalArgumentException | NullPointerException e) {
                        throw new UiMessageException(XLSX_FILE_PREFERENCE_EXPECTED, columnIndex, row.getRowNum());
                    }
                    builder.put(person, date, preference);
                }
            }
        }
        return builder.build();
    }

    private Workbook createWorkBook(MultipartFile multipartFile) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            return new XSSFWorkbook(inputStream);
        } catch (IOException | IllegalArgumentException e) {
            log.error("Failed to parse xlsx file.", e);
            throw new UiMessageException(ERROR_READING_XLSX_FILE);
        }
    }

    private Map<Integer, Date> readDatesRow(Row row) {
        Map<Integer, Date> datesRow = new HashMap<>();
        for (Cell cell : row) {
            if (cell.getColumnIndex() == 0) {
                continue;
            } else if (cell.getCellType() == CellType.BLANK) {
                break;
            } else if (cell.getCellType() == CellType.NUMERIC && isCellDateFormatted(cell)) {
                Date date = new Date(cell.getDateCellValue());
                datesRow.put(cell.getColumnIndex(), date);
            } else {
                throw new UiMessageException(XLSX_FILE_DATE_EXPECTED,
                        cell.getRowIndex(), cell.getColumnIndex());
            }
        }
        return datesRow;
    }

    private Person validateAndGetPerson(Cell personCell) {
        String personName = personCell.getStringCellValue();
        if (personName.length() > PERSON_MAX_LENGTH) {
            throw new UiMessageException(XLSX_FILE_PERSON_NAME_TOO_LONG, PERSON_MAX_LENGTH, personName.length(),
                    personCell.getRowIndex(), personCell.getColumnIndex());
        }
        return new Person(personName);
    }
}
