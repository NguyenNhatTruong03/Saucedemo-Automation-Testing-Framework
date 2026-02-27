package com.company.test;

import io.qameta.allure.Allure;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.company.page.InventoryPage;
import com.company.page.LoginPage;
import com.company.utils.ExcelUtil;
import com.company.utils.LogUtil;

import java.util.Map;

public class LoginTest extends BaseTest {

    @DataProvider(name = "loginDataProvider")
    public Object[][] getLoginData() {
        return ExcelUtil.getExcelData("src/test/resources/testdata/LoginData.xlsx", "Sheet1");
    }

    @Test(dataProvider = "loginDataProvider", description = "Kiểm thử chức năng đăng nhập với dữ liệu từ Excel")
    public void testLoginFlow(Map<String, String> data) {
        String id = data.get("ID");
        String testCaseName = data.get("Test Case Name");
        String inputData = data.get("Input Data");
        String expectedResult = data.get("Expected Result");

        Allure.getLifecycle().updateTestCase(result -> result.setName(id + " - " + testCaseName));
        LogUtil.info("Đang chạy: " + id + " - " + testCaseName);

        String user = inputData.contains("|") ? inputData.split("\\|")[0].trim() : inputData.trim();
        String pass = inputData.contains("|") ? inputData.split("\\|")[1].trim() : "";

        LoginPage loginPage = new LoginPage();
        loginPage.login(user, pass);

        if (id.equalsIgnoreCase("TC01")) {
            InventoryPage inventoryPage = new InventoryPage();
            Assert.assertTrue(inventoryPage.isInventoryPageDisplayed(), 
                "Lỗi: Không hiển thị trang Inventory sau khi đăng nhập thành công!");
        } else {
            String actualError = loginPage.getErrorMessage();
            Assert.assertEquals(actualError, expectedResult, 
                "Lỗi: Thông báo lỗi hiển thị không khớp với mong đợi!");
        }

        ExcelUtil.setResults(id, "PASS");
    }
}