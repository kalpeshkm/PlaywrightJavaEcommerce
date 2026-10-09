
package com.ecommerce.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CheckoutValidationPage {

    private final Page page;

    private final Locator firstName;
    private final Locator lastName;
    private final Locator postalCode;
    private final Locator continueButton;
    private final Locator cancelButton;
    private final Locator errorMessage;

    public CheckoutValidationPage(Page page) {
        this.page = page;

        firstName = page.locator("[data-test='firstName']");
        lastName = page.locator("[data-test='lastName']");
        postalCode = page.locator("[data-test='postalCode']");

        continueButton = page.locator("[data-test='continue']");
        cancelButton = page.locator("[data-test='cancel']");

        errorMessage = page.locator("[data-test='error']");
    }

    public void enterFirstName(String value) {
        firstName.fill(value);
    }

    public void enterLastName(String value) {
        lastName.fill(value);
    }

    public void enterPostalCode(String value) {
        postalCode.fill(value);
    }

    public void clickContinue() {
        continueButton.click();
    }

    public void clickCancel() {
        cancelButton.click();
    }

    public String getErrorMessage() {
        return errorMessage.innerText();
    }

    public boolean isErrorDisplayed() {
        return errorMessage.isVisible();
    }

    public boolean isCheckoutInformationPageDisplayed() {
        return page.url().contains("checkout-step-one.html");
    }

    public boolean isCheckoutOverviewPageDisplayed() {
        return page.url().contains("checkout-step-two.html");
    }

    public void fillValidCheckoutInformation() {
        enterFirstName("Kalpesh");
        enterLastName("Mali");
        enterPostalCode("411001");
    }
}