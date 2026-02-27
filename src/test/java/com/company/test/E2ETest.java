package com.company.test;

import io.qameta.allure.Allure;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.company.page.*;
import com.company.utils.*;

import java.util.Map;

public class E2ETest extends BaseTest {

    @DataProvider(name = "e2eDataProvider")
    public Object[][] getE2EData() {
        return ExcelUtil.getExcelData("src/test/resources/testdata/E2EData.xlsx", "E2E_Flow");
    }

    @Test(dataProvider = "e2eDataProvider", description = "TC_E2E_01: Hoàn tất quy trình mua hàng trọn gói")
    @Severity(SeverityLevel.BLOCKER)
    public void testCompleteOrderE2E(Map<String, String> data) {
        String id = data.get("ID");
        String testCaseName = data.get("Test Case Name");
        String products = data.get("Products");
        
        Allure.getLifecycle().updateTestCase(result -> result.setName(id + " - " + testCaseName));
        LogUtil.info("Bắt đầu kịch bản E2E: " + testCaseName);

        LoginPage loginPage = new LoginPage();
        loginPage.login(PropertyUtil.get("validUsername"), PropertyUtil.get("validPassword"));

        InventoryPage inventoryPage = new InventoryPage();
        String[] productList = products.split("\\|");
        for (String p : productList) {
            inventoryPage.addProductToCart(p.trim());
        }
        inventoryPage.goToCart();

        CartPage cartPage = new CartPage();
        Assert.assertTrue(cartPage.isCartPageDisplayed());
        cartPage.clickCheckout();

        CheckoutPage checkoutPage = new CheckoutPage();
        checkoutPage.enterCheckoutInformation(
            data.get("FirstName"), 
            data.get("LastName"), 
            data.get("ZipCode")
        );

        double subtotal = checkoutPage.getSubtotal();
        double tax = checkoutPage.getTax();
        double actualTotal = checkoutPage.getTotal();
        double expectedTotal = subtotal + tax;

        LogUtil.info("Kiểm tra tính toán tiền: " + subtotal + " + " + tax + " = " + actualTotal);
        Assert.assertEquals(actualTotal, expectedTotal, "Lỗi: Tổng tiền tính toán không khớp!");

        checkoutPage.clickFinish();
        String successMsg = checkoutPage.getSuccessMessage();
        
        AllureReportUtil.saveScreenshotPNGReport();
        
        Assert.assertEquals(successMsg, "Thank you for your order!");
        
        ExcelUtil.setResults(id, "PASS");
    }
}