
package com.ecommerce.pages;

import com.microsoft.playwright.Page;

public class ProductDetailsPage {

    private final Page page;

    private final String productName = ".inventory_details_name";
    private final String productDescription = ".inventory_details_desc";
    private final String productPrice = ".inventory_details_price";
    private final String addToCartButton =
            "button[data-test^='add-to-cart']";
    private final String removeButton =
            "button[data-test^='remove']";
    private final String backButton = "[data-test='back-to-products']";
    private final String cartBadge = ".shopping_cart_badge";

    public ProductDetailsPage(Page page) {
        this.page = page;
    }

    public void openFirstProduct() {
        page.locator(".inventory_item_name").first().click();
    }

    public String getProductName() {
        return page.locator(productName).innerText();
    }

    public String getProductDescription() {
        return page.locator(productDescription).innerText();
    }

    public String getProductPrice() {
        return page.locator(productPrice).innerText();
    }

    public void addToCart() {
        page.locator(addToCartButton).click();
    }

    public boolean isRemoveButtonDisplayed() {
        return page.locator(removeButton).isVisible();
    }

    public String getCartCount() {
        return page.locator(cartBadge).innerText();
    }

    public void goBackToProducts() {
        page.locator(backButton).click();
    }
}