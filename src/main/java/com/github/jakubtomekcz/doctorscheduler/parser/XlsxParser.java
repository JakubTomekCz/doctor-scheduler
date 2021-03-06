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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.jakubtomekcz.doctorscheduler.error.UiMessageException.MessageCode.ERROR_READING_XLSX_FILE;
import static com.github.jakubtomekcz.doctorscheduler.error.UiMessageException.MessageCode.XLSX_FILE_DATE_EXPECTED;
import static com.github.jakubtomekcz.doctorscheduler.error.UiMessageException.MessageCode.XLSX_FILE_PERSON_NAME_TOO_LONG;
import static com.github.jakubtomekcz.doctorscheduler.error.UiMessageException.MessageCode.XLSX_FILE_PREFERENCE_EXPECTED;
import static com.github.jakubtomekcz.doctorscheduler.error.UiMessageException.MessageCode.XLSX_FILE_TABLE_NAME_TOO_LONG;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted;

@Slf4j
public class XlsxParser implements PreferenceTableParser {

    private static final int TABLE_NAME_MAX_LENGTH = 100;
    private static final int PERSON_MAX_LENGTH = 100;

    @Override
    public List<PreferenceTable> parseMultipartFile(MultipartFile multipartFile) {
        List<PreferenceTable> preferenceTables = new ArrayList<>();
        Workbook workbook = createWorkBook(multipartFile);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Optional<PreferenceTable> preferenceTable = readPreferenceTable(rowIterator);
            preferenceTable.ifPresent(preferenceTables::add);
        }
        return preferenceTables;
    }

    private Optional<PreferenceTable> readPreferenceTable(Iterator<Row> rowIterator) {
        PreferenceTable.Builder builder = PreferenceTable.builder();
        Map<Integer, Date> datesRow = null;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (datesRow == null) {
                Optional<String> tableName = readTableName(row);
                tableName.ifPresent(builder::name);
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
        PreferenceTable preferenceTable = builder.build();
        return preferenceTable.isEmpty() ? Optional.empty() : Optional.of(preferenceTable);
    }

    private Workbook createWorkBook(MultipartFile multipartFile) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            return new XSSFWorkbook(inputStream);
        } catch (IOException | IllegalArgumentException e) {
            log.error("Failed to parse xlsx file.", e);
            throw new UiMessageException(ERROR_READING_XLSX_FILE);
        }
    }

    private Optional<String> readTableName(Row row) {
        Cell tableNameCell = row.getCell(0);
        if (tableNameCell != null && tableNameCell.getCellType() == CellType.STRING) {
            String rawName = tableNameCell.getStringCellValue();
            if (rawName.length() > TABLE_NAME_MAX_LENGTH) {
                throw new UiMessageException(XLSX_FILE_TABLE_NAME_TOO_LONG, TABLE_NAME_MAX_LENGTH, rawName.length(),
                        tableNameCell.getRowIndex(), tableNameCell.getColumnIndex());
            }
            return Optional.of(rawName);
        }
        return Optional.empty();
    }

    private Map<Integer, Date> readDatesRow(Row row) {
        Cell cellB = row.getCell(1);
        if (cellB != null && cellB.getCellType() == CellType.NUMERIC && isCellDateFormatted(cellB)) {
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
        return null;
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
