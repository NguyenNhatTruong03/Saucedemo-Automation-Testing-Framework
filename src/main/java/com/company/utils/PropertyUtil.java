package com.company.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtil {
    private static final Properties properties = new Properties();

    static {
        try {
        	
        	FileInputStream file = new FileInputStream("src/test/resources/config/config.properties");
            properties.load(file);
        } catch (IOException e) {
            System.err.println("[Error] Không tìm thấy file config.properties: " + e.getMessage());
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}