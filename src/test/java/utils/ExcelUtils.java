package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExcelUtils {
    private static final String CLONE_FOLDER = "target/test-output/excel-clones/";

    public static String createExcelClone(String originalFilePath) {
        try {
            //Create folder if it does not exist
            Files.createDirectories(Paths.get(CLONE_FOLDER));

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());

            //Get original file name
            String originalFileName = new File(originalFilePath).getName();
            String clonedFileName = originalFileName.replace(".xlsx", "_" + timestamp + ".xlsx");

            //Path's file clone
            String clonedFilePath = CLONE_FOLDER + clonedFileName;

            //Copy original file to cloned file
            Files.copy(Paths.get(originalFilePath), new FileOutputStream(clonedFilePath));

            System.out.println("Clone Excel thành công: " + clonedFilePath);
            return clonedFilePath;
        } catch (IOException e) {
            throw new RuntimeException("Không thể tạo clone của file Excel: " + originalFilePath, e);
        }
    }
}
