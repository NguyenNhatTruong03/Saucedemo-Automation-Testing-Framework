package com.company.test;

import com.company.drivers.DriverManager;
import com.company.utils.PropertyUtil;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
	@BeforeMethod(alwaysRun = true)
	public void setUp() {

		DriverManager.createWebdriver(PropertyUtil.get("DRIVERVERSION"), PropertyUtil.get("BROWSERVERSION"));

		DriverManager.getDriver().get(PropertyUtil.get("URL"));
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		if (DriverManager.getDriver() != null) {
			DriverManager.quitDriver();
		}
	}
}