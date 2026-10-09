package com.ecommerce.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class CheckoutPage {

    private final Page page;

    private final String cartIcon = "[data-test='shopping-cart-link']";
    private final String checkoutButton = "[data-test='checkout']";
    private final String firstName = "[data-test='firstName']";
    private final String lastName = "[data-test='lastName']";
    private final String postalCode = "[data-test='postalCode']";
    private final String continueButton = "[data-test='continue']";
    private final String finishButton = "[data-test='finish']";
    private final String completeHeader = ".complete-header";
    private final String errorMessage = "[data-test='error']";
    private final String cartItem = ".cart_item";

    public CheckoutPage(Page page) {
        this.page = page;
    }

    private void waitForVisible(String selector) {
        page.locator(selector).waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(15000)
        );
    }

    public void openCart() {
        waitForVisible(cartIcon);
        page.locator(cartIcon).click();
        page.waitForURL(
                "**/cart.html",
                new Page.WaitForURLOptions().setTimeout(15000)
        );
    }

    public void clickCheckout() {
        waitForVisible(checkoutButton);

        if (page.locator(cartItem).count() == 0) {
            throw new AssertionError(
                    "Cannot proceed to checkout: the cart is empty."
            );
        }

        page.locator(checkoutButton).click();

        page.waitForURL(
                "**/checkout-step-one.html",
                new Page.WaitForURLOptions().setTimeout(15000)
        );

        waitForVisible(firstName);
    }

    public void enterCustomerDetails(
            String first, String last, String postal) {

        waitForVisible(firstName);
        page.locator(firstName).fill(first);

        waitForVisible(lastName);
        page.locator(lastName).fill(last);

        waitForVisible(postalCode);
        page.locator(postalCode).fill(postal);
    }

    public void clickContinue() {
        waitForVisible(continueButton);
        page.locator(continueButton).click();

        page.waitForURL(
                "**/checkout-step-two.html",
                new Page.WaitForURLOptions().setTimeout(15000)
        );
    }

    public void clickFinish() {
        waitForVisible(finishButton);
        page.locator(finishButton).click();

        page.waitForURL(
                "**/checkout-complete.html",
                new Page.WaitForURLOptions().setTimeout(15000)
        );

        waitForVisible(completeHeader);
    }

    public String getOrderConfirmation() {
        waitForVisible(completeHeader);
        return page.locator(completeHeader).innerText();
    }

    public String getErrorMessage() {
        waitForVisible(errorMessage);
        return page.locator(errorMessage).innerText();
    }

    public boolean isCheckoutInformationDisplayed() {
        if (!page.url().contains("checkout-step-one.html")) {
            return false;
        }

        return page.locator(firstName).isVisible()
                && page.locator(lastName).isVisible()
                && page.locator(postalCode).isVisible();
    }

    public boolean isOrderCompleted() {
        return page.url().contains("checkout-complete.html")
                && page.locator(completeHeader).isVisible();
    }

    public boolean isErrorDisplayed() {
        return page.locator(errorMessage).isVisible();
    }
}
