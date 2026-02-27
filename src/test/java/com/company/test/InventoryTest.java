package com.company.test;

import io.qameta.allure.Allure;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.company.page.InventoryPage;
import com.company.page.LoginPage;
import com.company.utils.ExcelUtil;
import com.company.utils.LogUtil;
import com.company.utils.PropertyUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class InventoryTest extends BaseTest {

    @DataProvider(name = "inventoryDataProvider")
    public Object[][] getInventoryData() {
        return ExcelUtil.getExcelData("src/test/resources/testdata/InventoryData.xlsx", "Inventory");
    }

    @Test(priority = 1, dataProvider = "inventoryDataProvider", description = "Kiểm tra logic sắp xếp sản phẩm")
    @Severity(SeverityLevel.NORMAL)
    public void testProductSorting(Map<String, String> data) {
        String id = data.get("ID");
        String testCaseName = data.get("Test Case Name");
        String sortOption = data.get("Sort Option");

        Allure.getLifecycle().updateTestCase(result -> result.setName(id + " - " + testCaseName));
        LogUtil.info("Thực thi kịch bản sắp xếp: " + sortOption);

        LoginPage loginPage = new LoginPage();
        loginPage.login(PropertyUtil.get("validUsername"), PropertyUtil.get("validPassword"));

        InventoryPage inventoryPage = new InventoryPage();
        
        inventoryPage.selectSortOption(sortOption);

        if (sortOption.contains("Price")) {
            List<Double> actualPrices = inventoryPage.getAllProductPrices();
            List<Double> expectedPrices = new ArrayList<>(actualPrices);
            
            if (sortOption.contains("low to high")) {
                Collections.sort(expectedPrices);
            } else {
                expectedPrices.sort(Collections.reverseOrder());
            }
            Assert.assertEquals(actualPrices, expectedPrices, "Lỗi: Danh sách giá chưa được sắp xếp đúng");
        } else {
            List<String> actualNames = inventoryPage.getAllProductNames();
            List<String> expectedNames = new ArrayList<>(actualNames);
            
            if (sortOption.contains("A to Z")) {
                Collections.sort(expectedNames);
            } else {
                expectedNames.sort(Collections.reverseOrder());
            }
            Assert.assertEquals(actualNames, expectedNames, "Lỗi: Danh sách tên chưa được sắp xếp đúng");
        }

        ExcelUtil.setResults(id, "PASS");
    }

    @Test(priority = 2, description = "Kiểm tra thêm nhiều sản phẩm vào giỏ hàng")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddMultipleProductsToCart() {
        LogUtil.info("Bắt đầu test case: Thêm nhiều sản phẩm");

        new LoginPage().login("standard_user", "secret_sauce");
        InventoryPage inventoryPage = new InventoryPage();

        String p1 = "Sauce Labs Backpack";
        String p2 = "Sauce Labs Bike Light";
        
        inventoryPage.addProductToCart(p1);
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), "1");

        inventoryPage.addProductToCart(p2);
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), "2");

        LogUtil.info("Đã thêm thành công " + inventoryPage.getCartBadgeCount() + " sản phẩm.");
    }
}