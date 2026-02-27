package com.company.test;

import io.qameta.allure.Allure;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.company.page.CartPage;
import com.company.page.InventoryPage;
import com.company.page.LoginPage;
import com.company.utils.AllureReportUtil;
import com.company.utils.ExcelUtil;
import com.company.utils.LogUtil;
import com.company.utils.PropertyUtil;

import java.util.List;
import java.util.Map;

public class CartTest extends BaseTest {

    @DataProvider(name = "cartDataProvider")
    public Object[][] getCartData() {
        return ExcelUtil.getExcelData("src/test/resources/testdata/CartData.xlsx", "Cart");
    }

    @Test(priority = 1, dataProvider = "cartDataProvider", description = "Xác thực sản phẩm trong giỏ hàng")
    @Severity(SeverityLevel.CRITICAL)
    public void testVerifyProductsInCart(Map<String, String> data) {
        String id = data.get("ID");
        String testCaseName = data.get("Test Case Name");
        String productsToBuy = data.get("Products");

        Allure.getLifecycle().updateTestCase(result -> result.setName(id + " - " + testCaseName));
        LogUtil.info("Bắt đầu kiểm tra giỏ hàng cho: " + id);

        new LoginPage().login(PropertyUtil.get("validUsername"), PropertyUtil.get("validPassword"));
        
        InventoryPage inventoryPage = new InventoryPage();
        String[] productList = productsToBuy.split("\\|");
        
        for (String productName : productList) {
            inventoryPage.addProductToCart(productName.trim());
        }

        inventoryPage.goToCart();
        CartPage cartPage = new CartPage();
        
        List<String> actualItems = cartPage.getCartItemNames();
        LogUtil.info("Sản phẩm thực tế trong giỏ: " + actualItems);

        for (String expectedProduct : productList) {
            Assert.assertTrue(actualItems.contains(expectedProduct.trim()), 
                "LỖI: Sản phẩm '" + expectedProduct + "' không tìm thấy trong giỏ hàng!");
        }

        AllureReportUtil.saveScreenshotPNGReport();
        ExcelUtil.setResults(id, "PASS");
    }

    @Test(priority = 2, description = "Kiểm tra tính năng xóa sản phẩm khỏi giỏ hàng")
    public void testRemoveProductFromCart() {
        new LoginPage().login("standard_user", "secret_sauce");
        
        InventoryPage inventoryPage = new InventoryPage();
        String product = "Sauce Labs Backpack";
        inventoryPage.addProductToCart(product);
        inventoryPage.goToCart();

        CartPage cartPage = new CartPage();
        Assert.assertTrue(cartPage.getCartItemNames().contains(product));

        cartPage.removeItemFromCart(product);
        
        Assert.assertFalse(cartPage.getCartItemNames().contains(product), "Lỗi: Sản phẩm vẫn còn trong giỏ sau khi xóa!");
        LogUtil.info("Xóa sản phẩm thành công.");
    }
}