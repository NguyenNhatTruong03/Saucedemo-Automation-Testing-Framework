package com.company.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ExcelUtil {
    private static Workbook workbook;
    private static Sheet sheet;
    private static String path;

    private static Map<String, Integer> columnMap = new HashMap<>();
    private static Map<String, Integer> rowMap = new HashMap<>();


    public static void setupExcel(String filePath, String sheetName) {
        try {
            path = filePath;
            FileInputStream fis = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheet(sheetName);

            Row headerRow = sheet.getRow(0);
            for (Cell cell : headerRow) {
                columnMap.put(cell.getStringCellValue().trim(), cell.getColumnIndex());
            }

            int idColIndex = columnMap.get("ID");
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    String idValue = new DataFormatter().formatCellValue(row.getCell(idColIndex));
                    rowMap.put(idValue.trim(), i);
                }
            }
            fis.close();
        } catch (Exception e) {
            LogUtil.error("Lỗi cấu hình Excel tại " + filePath + ": " + e.getMessage());
        }
    }


    public static Object[][] getExcelData(String filePath, String sheetName) {
        setupExcel(filePath, sheetName);
        int rowCount = sheet.getLastRowNum();
        Object[][] data = new Object[rowCount][1];

        for (int i = 1; i <= rowCount; i++) {
            Map<String, String> map = new HashMap<>();
            Row row = sheet.getRow(i);
            if (row != null) {
                for (String columnName : columnMap.keySet()) {
                    int colIndex = columnMap.get(columnName);
                    Cell cell = row.getCell(colIndex);
                    String cellValue = new DataFormatter().formatCellValue(cell);
                    map.put(columnName, cellValue);
                }
                data[i - 1][0] = map;
            }
        }
        return data;
    }

 
    public static String getData(String id, String columnName) {
        try {
            int rowIndex = rowMap.get(id);
            int colIndex = columnMap.get(columnName);
            Cell cell = sheet.getRow(rowIndex).getCell(colIndex);
            return new DataFormatter().formatCellValue(cell);
        } catch (Exception e) {
            return "";
        }
    }

 
    public static void setResults(String id, String status) {
        try {
            int rowIndex = rowMap.get(id);
            int colIndex = columnMap.get("Pass/Fail");
            
            Row row = sheet.getRow(rowIndex);
            Cell cell = row.createCell(colIndex);
            cell.setCellValue(status);

            FileOutputStream fos = new FileOutputStream(path);
            workbook.write(fos);
            fos.close();
        } catch (Exception e) {
            LogUtil.error("Lỗi khi ghi kết quả PASS/FAIL cho ID " + id + ": " + e.getMessage());
        }
    }

 
    public static String getCellValue(String id, String columnName, int index) {
        String rawData = getData(id, columnName);
        
        if (rawData.contains("|")) {
            String[] parts = rawData.split("\\|");
            if (index < parts.length) {
                return parts[index].trim();
            }
            return "";
        }
        
        return (index == 0) ? rawData.trim() : "";
    }
}