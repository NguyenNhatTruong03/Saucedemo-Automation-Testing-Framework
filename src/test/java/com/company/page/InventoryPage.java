package com.company.page;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.company.utils.LogUtil;

import io.qameta.allure.Step;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryPage extends BasePage {

    private final By productSortDropdown = By.className("product_sort_container");
    private final By inventoryItemName = By.className("inventory_item_name");
    private final By inventoryItemPrice = By.className("inventory_item_price");
    private final By shoppingCartLink = By.className("shopping_cart_link");
    private final By cartBadge = By.className("shopping_cart_badge");

    private By addToCartButton(String productName) {
        return By.xpath("//div[text()='" + productName + "']/ancestor::div[@class='inventory_item_description']//button");
    }


    @Step("Kiểm tra trang Inventory có hiển thị không")
    public boolean isInventoryPageDisplayed() {
        return isElementDisplayed(shoppingCartLink);
    }

    @Step("Sắp xếp sản phẩm theo: {0}")
    public void selectSortOption(String optionText) {
        LogUtil.info("Đang chọn kiểu sắp xếp: " + optionText);
        WebElement dropdown = waitVisibilityElementLocated(productSortDropdown);
        Select select = new Select(dropdown);
        select.selectByVisibleText(optionText);
    }

    @Step("Lấy danh sách tên tất cả sản phẩm")
    public List<String> getAllProductNames() {
        List<WebElement> elements = driver.findElements(inventoryItemName);
        
        return elements.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    @Step("Lấy danh sách giá của tất cả sản phẩm")
    public List<Double> getAllProductPrices() {
        List<WebElement> elements = driver.findElements(inventoryItemPrice);
        
        return elements.stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .collect(Collectors.toList());
    }

    @Step("Thêm sản phẩm '{0}' vào giỏ hàng")
    public void addProductToCart(String productName) {
        LogUtil.info("Thêm vào giỏ: " + productName);
        click(addToCartButton(productName));
    }

    @Step("Lấy số lượng sản phẩm trên icon giỏ hàng")
    public String getCartBadgeCount() {
        if (isElementDisplayed(cartBadge)) {
            return getText(cartBadge);
        }
        return "0";
    }

    @Step("Đi tới trang giỏ hàng (Cart)")
    public void goToCart() {
        click(shoppingCartLink);
    }
}
