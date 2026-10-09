
package com.ecommerce.tests;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.CheckoutValidationPage;
import com.microsoft.playwright.Page;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CheckoutValidationTest extends BaseTest {

    private CheckoutValidationPage openCheckout(Page page) {

        // Open SauceDemo login page
        page.navigate("https://www.saucedemo.com/");

        // Login
        page.locator("[data-test='username']")
                .fill("standard_user");

        page.locator("[data-test='password']")
                .fill("secret_sauce");

        page.locator("[data-test='login-button']")
                .click();

        // Verify successful login
        page.waitForURL("**/inventory.html");

        Assert.assertTrue(
                page.locator(".inventory_list").isVisible(),
                "Inventory page should be displayed"
        );

        // Add product to cart
        page.locator("[data-test='add-to-cart-sauce-labs-backpack']")
                .click();

        // Open cart
        page.locator(".shopping_cart_link").click();
        page.waitForURL("**/cart.html");

        // Click checkout
        page.locator("[data-test='checkout']").click();
        page.waitForURL("**/checkout-step-one.html");

        return new CheckoutValidationPage(page);
    }

    @Test
    public void verifyFirstNameRequiredValidation() {

        Page page = getPage();
        CheckoutValidationPage checkout = openCheckout(page);

        checkout.enterFirstName("");
        checkout.enterLastName("Mali");
        checkout.enterPostalCode("411001");
        checkout.clickContinue();

        Assert.assertTrue(
                checkout.isErrorDisplayed(),
                "Error should appear when first name is missing"
        );

        Assert.assertEquals(
                checkout.getErrorMessage(),
                "Error: First Name is required",
                "Incorrect first-name validation message"
        );
    }

    @Test
    public void verifyLastNameRequiredValidation() {

        Page page = getPage();
        CheckoutValidationPage checkout = openCheckout(page);

        checkout.enterFirstName("Kalpesh");
        checkout.enterLastName("");
        checkout.enterPostalCode("411001");
        checkout.clickContinue();

        Assert.assertTrue(
                checkout.isErrorDisplayed(),
                "Error should appear when last name is missing"
        );

        Assert.assertEquals(
                checkout.getErrorMessage(),
                "Error: Last Name is required",
                "Incorrect last-name validation message"
        );
    }

    @Test
    public void verifyPostalCodeRequiredValidation() {

        Page page = getPage();
        CheckoutValidationPage checkout = openCheckout(page);

        checkout.enterFirstName("Kalpesh");
        checkout.enterLastName("Mali");
        checkout.enterPostalCode("");
        checkout.clickContinue();

        Assert.assertTrue(
                checkout.isErrorDisplayed(),
                "Error should appear when postal code is missing"
        );

        Assert.assertEquals(
                checkout.getErrorMessage(),
                "Error: Postal Code is required",
                "Incorrect postal-code validation message"
        );
    }

    @Test
    public void verifyCheckoutWithValidInformation() {

        Page page = getPage();
        CheckoutValidationPage checkout = openCheckout(page);

        Assert.assertTrue(
                checkout.isCheckoutInformationPageDisplayed(),
                "Checkout information page should be displayed"
        );

        checkout.fillValidCheckoutInformation();
        checkout.clickContinue();

        Assert.assertTrue(
                checkout.isCheckoutOverviewPageDisplayed(),
                "Valid information should navigate to checkout overview"
        );
    }
}