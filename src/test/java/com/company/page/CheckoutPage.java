package com.company.page;

import org.openqa.selenium.By;

import com.company.utils.LogUtil;

import io.qameta.allure.Step;

public class CheckoutPage extends BasePage {

    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By zipCodeInput = By.id("postal-code");
    private final By continueButton = By.id("continue");

    private final By subtotalLabel = By.className("summary_subtotal_label");
    private final By taxLabel = By.className("summary_tax_label");
    private final By totalLabel = By.className("summary_total_label");
    private final By finishButton = By.id("finish");

    private final By completeHeader = By.className("complete-header");

    @Step("Nhập thông tin giao hàng: {0} {1}, {2}")
    public void enterCheckoutInformation(String firstName, String lastName, String zipCode) {
        LogUtil.info("Nhập thông tin khách hàng: " + firstName + " " + lastName);
        enterText(firstNameInput, firstName);
        enterText(lastNameInput, lastName);
        enterText(zipCodeInput, zipCode);
        click(continueButton);
    }

    @Step("Lấy giá trị Item Total (chưa thuế)")
    public double getSubtotal() {
        String text = getText(subtotalLabel);
        return Double.parseDouble(text.split("\\$")[1]);
    }

    @Step("Lấy giá trị Tax")
    public double getTax() {
        String text = getText(taxLabel);
        return Double.parseDouble(text.split("\\$")[1]);
    }

    @Step("Lấy giá trị Total (sau thuế)")
    public double getTotal() {
        String text = getText(totalLabel);
        return Double.parseDouble(text.split("\\$")[1]);
    }

    @Step("Nhấn nút Finish để hoàn tất đơn hàng")
    public void clickFinish() {
        LogUtil.info("Nhấn nút Finish");
        click(finishButton);
    }

    @Step("Lấy thông báo hoàn tất đơn hàng")
    public String getSuccessMessage() {
        return getText(completeHeader);
    }
}