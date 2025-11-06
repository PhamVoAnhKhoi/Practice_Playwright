package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * A generic Excel reader that can be reused for any table structure.
 * Returns data as List<Map<String, String>>.
 * Header row (first row) is used as keys.
 */
public class ExcelReader {

    public static List<Map<String, String>> readSheetAsMap(String filePath) {
        List<Map<String, String>> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (!rowIterator.hasNext()) {
                return data;
            }

            // Extract headers
            Row headerRow = rowIterator.next();
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(getCellValue(cell));
            }

            // Extract data rows
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Map<String, String> rowData = new LinkedHashMap<>();

                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = row.getCell(i);
                    rowData.put(headers.get(i), getCellValue(cell));
                }

                data.add(rowData);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error reading Excel file: " + filePath, e);
        }

        return data;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double val = cell.getNumericCellValue();
                    if (val == (long) val) return String.valueOf((long) val);
                    return String.valueOf(val);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    public static List<SystemUser> toSystemUsers(List<Map<String, String>> excelData) {
        List<SystemUser> users = new ArrayList<>();
        for (Map<String, String> row : excelData) {
            users.add(new SystemUser(
                    row.getOrDefault("Username", ""),
                    row.getOrDefault("User Role", ""),
                    row.getOrDefault("Employee Name", ""),
                    row.getOrDefault("Status", "")
            ));
        }
        return users;
    }

    public static List<SystemUser> readUsersFromExcel(String filePath) {
        List<Map<String, String>> data = readSheetAsMap(filePath);
        return toSystemUsers(data);
    }

}
