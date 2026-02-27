package com.company.page;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.company.drivers.DriverManager;
import com.company.utils.LogUtil;
import com.company.utils.PropertyUtil;

import io.qameta.allure.Step;

public class BasePage {
	protected WebDriver driver;
	protected WebDriverWait wait;

	public BasePage() {
		this.driver = DriverManager.getDriver();
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(PropertyUtil.get("explicitWait"))));
	}

	@Step("Click element {0}")
	protected void click(By locator) {
		LogUtil.info("Clicking on element: " + locator.toString());
		//AllureReportUtil.saveScreenshotPNGReport();
		waitElementClickable(locator).click();
	}

	@Step("Set text {1} on element {0}")
	protected void enterText(By locator, String text) {
		LogUtil.info("Entering text '" + text + "' into element: " + locator.toString());
		WebElement element = waitVisibilityElementLocated(locator);
		element.clear();
		element.sendKeys(text);
	}

	@Step("Get text from element {0}")
	protected String getText(By locator) {
		LogUtil.info("Getting text from element: " + locator.toString());
		//AllureReportUtil.saveScreenshotPNGReport();
		return waitVisibilityElementLocated(locator).getText();
	}

	@Step("Check element {0} is displayed")
	protected boolean isElementDisplayed(By locator) {
		LogUtil.info("Checking if element is displayed: " + locator.toString());
		try {
			return waitVisibilityElementLocated(locator).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}
	//@Step("Get title page")
	protected String getTitle() {
		String title = this.driver.getTitle();
		LogUtil.info("Get title page: " + title);
		return title;
	}
	
	protected boolean enableClick(By locator) {
		WebElement element = waitVisibilityElementLocated(locator);
		boolean element_enable = element.isEnabled();
		LogUtil.info("element " + locator.toString() + " enable click : " + element_enable);
		return element_enable;
	}
	
	protected void backPage() {
		LogUtil.info("Quay lại trang trước");
		this.driver.navigate().back();
	}

	protected WebElement waitElementClickable(By locator) {
		return wait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	protected WebElement waitVisibilityElementLocated(By locator) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
}
