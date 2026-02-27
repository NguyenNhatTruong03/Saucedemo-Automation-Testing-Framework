package com.company.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.company.drivers.DriverManager;

public class ScreenShotsUtil {

    public static void takeScreenshot(String fileName) {
        captureScreenshot(fileName, "DefaultSuite");
    }

    public static void captureScreenshot(String fileName, String suiteName) {
        try {
            WebDriver driver = DriverManager.getDriver();
            if (driver == null) {
                System.err.println("[Error] WebDriver null");
                return;
            }

            String baseDir = PropertyUtil.get("screenshot.path");
            if (baseDir == null || baseDir.isEmpty()) {
                baseDir = "screenshots";
            }


            String safeSuiteName = sanitizeName(suiteName);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
//            String threadId = String.valueOf(Thread.currentThread().getId());
            
            Path folderPath = Paths.get(baseDir, safeSuiteName);
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }

//            String finalFileName = String.format("%s_Tid%s_%s.png", sanitizeName(fileName), threadId, timestamp);
            String finalFileName = String.format("%s_Tid%s_%s.png", sanitizeName(fileName), timestamp);
            Path targetPath = folderPath.resolve(finalFileName);

            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(srcFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("[Success] Lưu tại: " + targetPath.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("[Error] Lỗi trong quá trình lưu file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[Error] Lỗi trong quá trình Screenshot: " + e.getMessage());
        }
    }

    private static String sanitizeName(String name) {
        if (name == null) return "unknown";
        return name.toLowerCase()
                .replaceAll("[^a-z0-9]", "_")
                .replaceAll("_+", "_");
    }
}