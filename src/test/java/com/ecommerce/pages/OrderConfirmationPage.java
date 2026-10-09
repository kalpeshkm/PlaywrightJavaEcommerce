
package com.ecommerce.pages;

import com.microsoft.playwright.Page;

public class OrderConfirmationPage {

    private final Page page;

    private final String confirmationHeader =
            ".complete-header";
    private final String confirmationMessage =
            ".complete-text";
    private final String backToProductsButton =
            "#back-to-products";

    public OrderConfirmationPage(Page page) {
        this.page = page;
    }

    public String getConfirmationHeader() {
        return page.locator(confirmationHeader).innerText();
    }

    public String getConfirmationMessage() {
        return page.locator(confirmationMessage).innerText();
    }

    public boolean isConfirmationDisplayed() {
        return page.locator(confirmationHeader).isVisible();
    }

    public boolean isConfirmationMessageDisplayed() {
        return page.locator(confirmationMessage).isVisible();
    }

    public void backToProducts() {
        page.locator(backToProductsButton).click();
    }
}