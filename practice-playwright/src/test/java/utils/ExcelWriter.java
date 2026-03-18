package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.List;

public class ExcelWriter {

    public static void writeUsersToExcelIfEmpty(String filePath, List<SystemUser> uiUsers) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            // Check file have had data yet?
            if (sheet.getLastRowNum() > 0) {
                System.out.println("Excel already contains data. No writing needed.");
                return;
            }

            System.out.println("Excel is empty. Writing UI users into Excel...");

            // Take Header row
            Row headerRow = sheet.getRow(0);
            int colCount = headerRow.getLastCellNum();

            int rowIndex = 1; //skip header
            for (SystemUser user : uiUsers) {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(user.getUsername());
                row.createCell(1).setCellValue(user.getUserRole());
                row.createCell(2).setCellValue(user.getEmployeeName());
                row.createCell(3).setCellValue(user.getStatus());
            }

            // Override file gốc
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }

            System.out.println("Successfully wrote UI table users into Excel the original file: " + filePath);

        } catch (IOException e) {
            throw new RuntimeException("Error writing users to Excel: " + filePath, e);
        }
    }
}
