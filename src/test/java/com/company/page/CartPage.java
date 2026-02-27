package com.company.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.company.utils.LogUtil;

import io.qameta.allure.Step;
import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends BasePage {

    private final By cartItemName = By.className("inventory_item_name");
    private final By checkoutButton = By.id("checkout");
    private final By continueShoppingButton = By.id("continue-shopping");
    private final By cartTitle = By.className("title");

    private By removeButton(String productName) {
        return By.xpath("//div[text()='" + productName + "']/ancestor::div[@class='cart_item']//button");
    }


    @Step("Kiểm tra trang Cart có hiển thị không")
    public boolean isCartPageDisplayed() {
        return isElementDisplayed(checkoutButton) && getText(cartTitle).equals("Your Cart");
    }

    @Step("Lấy danh sách tên sản phẩm có trong giỏ hàng")
    public List<String> getCartItemNames() {
    	
        List<WebElement> elements = driver.findElements(cartItemName);
        
        return elements.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    @Step("Xóa sản phẩm '{0}' khỏi giỏ hàng")
    public void removeItemFromCart(String productName) {
        LogUtil.info("Đang xóa sản phẩm: " + productName);
        click(removeButton(productName));
    }

    @Step("Nhấn nút Checkout để tiến hành thanh toán")
    public void clickCheckout() {
        LogUtil.info("Tiến hành Checkout");
        click(checkoutButton);
    }

    @Step("Nhấn nút Continue Shopping để quay lại danh sách sản phẩm")
    public void clickContinueShopping() {
        click(continueShoppingButton);
    }
}