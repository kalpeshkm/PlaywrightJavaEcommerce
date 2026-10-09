
package com.ecommerce.pages;

import com.microsoft.playwright.Page;

public class InventoryPage {

    private final Page page;

    public InventoryPage(Page page) {
        this.page = page;
    }

    private final String productTitle =
            "[data-test='title']";

    private final String addBackpackButton =
            "[data-test='add-to-cart-sauce-labs-backpack']";

    private final String cartBadge =
            "[data-test='shopping-cart-badge']";

    private final String cartLink =
            "[data-test='shopping-cart-link']";

    public String getPageTitle() {
        return page.locator(productTitle).innerText();
    }

    public void addBackpackToCart() {
        page.locator(addBackpackButton).click();
    }

    public String getCartCount() {
        return page.locator(cartBadge).innerText();
    }

    public void openCart() {
        page.locator(cartLink).click();
    }
}