package com.ecommerce.tests;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.CheckoutValidationPage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CheckoutValidationTest extends BaseTest {

    private CheckoutValidationPage openCheckout(Page page) {

        page.navigate("https://www.saucedemo.com/");

        page.locator("[data-test='username']").fill("standard_user");
        page.locator("[data-test='password']").fill("secret_sauce");
        page.locator("[data-test='login-button']").click();

        try {
            page.waitForURL(
                    "**/inventory.html",
                    new Page.WaitForURLOptions().setTimeout(15000)
            );
        } catch (TimeoutError e) {
            printDebugInfo(page);
            Assert.fail("Login failed. Inventory page was not opened.");
        }

        Locator inventoryList = page.locator(".inventory_list");

        try {
            inventoryList.waitFor(
                    new Locator.WaitForOptions()
                            .setState(WaitForSelectorState.VISIBLE)
                            .setTimeout(15000)
            );
        } catch (TimeoutError e) {
            printDebugInfo(page);
            Assert.fail(
                    "Inventory URL opened, but inventory list is not visible."
            );
        }

        Assert.assertTrue(
                inventoryList.isVisible(),
                "Inventory page should be displayed"
        );

        Locator addToCartButton = page.locator(
                "[data-test='add-to-cart-sauce-labs-backpack']"
        );

        addToCartButton.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(10000)
        );

        addToCartButton.click();

        Assert.assertEquals(
                page.locator(".shopping_cart_badge").innerText(),
                "1",
                "Product was not added to cart"
        );

        page.locator(".shopping_cart_link").click();

        page.waitForURL(
                "**/cart.html",
                new Page.WaitForURLOptions().setTimeout(10000)
        );

        Locator cartItem = page.locator(".cart_item");

        cartItem.first().waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(10000)
        );

        Assert.assertTrue(
                cartItem.count() > 0,
                "Cart should contain a product"
        );

        Locator checkoutButton = page.locator("[data-test='checkout']");

        checkoutButton.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(10000)
        );

        checkoutButton.click();

        page.waitForURL(
                "**/checkout-step-one.html",
                new Page.WaitForURLOptions().setTimeout(10000)
        );

        return new CheckoutValidationPage(page);
    }

    private void printDebugInfo(Page page) {
        System.out.println("Current URL: " + page.url());
        System.out.println("Page title: " + page.title());

        try {
            System.out.println(
                    "Page content: " + page.locator("body").innerText()
            );
        } catch (Exception e) {
            System.out.println(
                    "Unable to read page content: " + e.getMessage()
            );
        }

        Locator errorMessage = page.locator("[data-test='error']");

        if (errorMessage.isVisible()) {
            System.out.println(
                    "Login error: " + errorMessage.innerText()
            );
        }
    }

    @Test
    public void verifyFirstNameRequiredValidation() {

        CheckoutValidationPage checkout = openCheckout(getPage());

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

        CheckoutValidationPage checkout = openCheckout(getPage());

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

        CheckoutValidationPage checkout = openCheckout(getPage());

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

        CheckoutValidationPage checkout = openCheckout(getPage());

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
