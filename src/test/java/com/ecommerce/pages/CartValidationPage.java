package com.ecommerce.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CartValidationPage {

    private final Page page;

    private final Locator cartItems;
    private final Locator productName;
    private final Locator productPrice;
    private final Locator productQuantity;
    private final Locator checkoutButton;
    private final Locator continueShoppingButton;
    private final Locator removeBackpackButton;

    public CartValidationPage(Page page) {
        this.page = page;

        cartItems = page.locator(".cart_item");
        productName = page.locator(".inventory_item_name");
        productPrice = page.locator(".inventory_item_price");
        productQuantity = page.locator(".cart_quantity");

        checkoutButton = page.locator("[data-test='checkout']");
        continueShoppingButton =
                page.locator("[data-test='continue-shopping']");

        removeBackpackButton =
                page.locator("[data-test='remove-sauce-labs-backpack']");
    }

    public int getCartItemCount() {
        return cartItems.count();
    }

    public String getProductName() {
        return productName.first().innerText();
    }

    public String getProductPrice() {
        return productPrice.first().innerText();
    }

    public String getProductQuantity() {
        return productQuantity.first().innerText();
    }

    public boolean isProductDisplayed() {
        return productName.count() > 0;
    }

    public void removeBackpack() {
        removeBackpackButton.click();
    }

    public void clickCheckout() {
        checkoutButton.click();
    }

    public void clickContinueShopping() {
        continueShoppingButton.click();
    }

    public boolean isCheckoutButtonDisplayed() {
        return checkoutButton.isVisible();
    }

    public boolean isContinueShoppingButtonDisplayed() {
        return continueShoppingButton.isVisible();
    }

    public boolean isCartPageDisplayed() {
        return page.url().contains("/cart.html");
    }
}