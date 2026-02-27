package com.company.drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverManager {
	private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

	private DriverManager() {
	}

	public static void createWebdriver(String driverVer, String browserVer) {
		if (driverThread.get() == null) {
			setupManager(driverVer, browserVer);

			ChromeOptions options = new ChromeOptions();
			//options.addArguments("--headless=new");
			options.addArguments("--disable-gpu", 
					"--window-size=1920,1080", 
					"--no-sandbox", 
					"--disable-dev-shm-usage");

			driverThread.set(new ChromeDriver(options));
		}
	}

	private static void setupManager(String driverVer, String browserVer) {
	    if (driverVer != null && !driverVer.isEmpty()) {
	        WebDriverManager.chromedriver().driverVersion(driverVer).setup();
	    } else if (browserVer != null && !browserVer.isEmpty()) {
	        WebDriverManager.chromedriver().browserVersion(browserVer).setup();
	    } else {
	        WebDriverManager.chromedriver().setup();
	    }
	}

	public static WebDriver getDriver() {
		return driverThread.get();
	}

	public static void quitDriver() {
		if (driverThread.get() != null) {
			driverThread.get().quit();
			driverThread.remove();
		}
	}
}
