package com.company.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.company.drivers.DriverManager;

import io.qameta.allure.Attachment;

public class AllureReportUtil {
	
	@Attachment(value = "{0}", type = "text/plain")
	public String saveTextLog(String actionName, String message) {
	    return message;
	}
	
	@Attachment(value = "Page_Screenshot", type = "image/png")
	public static byte[] saveScreenshotPNGReport() {
		return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);

	}
}
