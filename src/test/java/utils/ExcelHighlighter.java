package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.List;

public class ExcelHighlighter {
    public static void highlightRows(String filePath, List<String> usernamesToHighlight) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            CellStyle highlightStyle = workbook.createCellStyle();
            highlightStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            highlightStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            int usernameColIndex = -1;

            Row headerRow = sheet.getRow(0);
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                if ("Username".equalsIgnoreCase(headerRow.getCell(i).getStringCellValue().trim())) {
                    usernameColIndex = i;
                    break;
                }
            }

            if (usernameColIndex == -1) {
                throw new RuntimeException("Column 'Username' not found in Excel.");
            }

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header

                Cell usernameCell = row.getCell(usernameColIndex);

                //Check row is null => Skip
                if (usernameCell == null || usernameCell.getCellType() == CellType.BLANK) continue;

                String username = usernameCell.getStringCellValue().trim();
                if (username.isEmpty()) continue; //Row is nul => Skip

                //Only highlight row appear in list mismatch
                if (usernamesToHighlight.contains(username)) {
                    for (int i = 0; i < row.getLastCellNum(); i++) {
                        Cell cell = row.getCell(i);
                        if (cell == null) cell = row.createCell(i);
                        cell.setCellStyle(highlightStyle);
                    }
                }
            }

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }

            System.out.println("Highlight mismatch done (only data rows affected): " + filePath);

        } catch (IOException e) {
            throw new RuntimeException("Error updating Excel file", e);
        }
    }

}
