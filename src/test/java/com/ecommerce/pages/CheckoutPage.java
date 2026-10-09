package com.ecommerce.pages;

import com.microsoft.playwright.Page;

public class CheckoutPage {

    private final Page page;

    private final String cartIcon = "#shopping_cart_container a";
    private final String checkoutButton = "#checkout";
    private final String firstName = "#first-name";
    private final String lastName = "#last-name";
    private final String postalCode = "#postal-code";
    private final String continueButton = "#continue";
    private final String finishButton = "#finish";
    private final String completeHeader = ".complete-header";
    private final String errorMessage = "[data-test='error']";

    public CheckoutPage(Page page) {
        this.page = page;
    }

    public void openCart() {
        page.locator(cartIcon).click();
    }

    public void clickCheckout() {
        page.locator(checkoutButton).click();
    }

    public void enterCustomerDetails(
            String first, String last, String postal) {

        page.locator(firstName).fill(first);
        page.locator(lastName).fill(last);
        page.locator(postalCode).fill(postal);
    }

    public void clickContinue() {
        page.locator(continueButton).click();
    }

    public void clickFinish() {
        page.locator(finishButton).click();
    }

    public String getOrderConfirmation() {
        return page.locator(completeHeader).innerText();
    }

    public String getErrorMessage() {
        return page.locator(errorMessage).innerText();
    }

    public boolean isCheckoutInformationDisplayed() {
        return page.locator(firstName).isVisible()
                && page.locator(lastName).isVisible()
                && page.locator(postalCode).isVisible();
    }

    public boolean isOrderCompleted() {
        return page.locator(completeHeader).isVisible();
    }
}
