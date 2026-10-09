package com.ecommerce.tests;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.CheckoutPage;
import com.ecommerce.pages.LoginPage;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CheckoutTest extends BaseTest {

    @Test
    public void verifySuccessfulCheckout() {

        LoginPage loginPage = new LoginPage(page);
        loginPage.login("standard_user", "secret_sauce");

        try {
            page.waitForURL(
                    "**/inventory.html",
                    new Page.WaitForURLOptions().setTimeout(15000)
            );
        } catch (TimeoutError e) {
            printDebugInfo();
            Assert.fail("Login failed. Inventory page was not opened.");
        }

        Locator inventoryList = page.locator(".inventory_list");

        inventoryList.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(15000)
        );

        Assert.assertTrue(
                inventoryList.isVisible(),
                "Inventory page is not displayed"
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
                "Product was not added to the cart"
        );

        CheckoutPage checkoutPage = new CheckoutPage(page);
        checkoutPage.openCart();

        page.waitForURL(
                "**/cart.html",
                new Page.WaitForURLOptions().setTimeout(10000)
        );

        Locator cartProduct = page.locator(
                ".cart_item .inventory_item_name"
        );

        cartProduct.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(10000)
        );

        Assert.assertEquals(
                cartProduct.innerText(),
                "Sauce Labs Backpack",
                "Sauce Labs Backpack is missing from the cart"
        );

        checkoutPage.clickCheckout();

        page.waitForURL(
                "**/checkout-step-one.html",
                new Page.WaitForURLOptions().setTimeout(10000)
        );

        Assert.assertTrue(
                checkoutPage.isCheckoutInformationDisplayed(),
                "Checkout information page is not displayed"
        );

        checkoutPage.enterCustomerDetails(
                "Kalpesh",
                "Mali",
                "411001"
        );

        checkoutPage.clickContinue();

        page.waitForURL(
                "**/checkout-step-two.html",
                new Page.WaitForURLOptions().setTimeout(10000)
        );

        Locator orderItem = page.locator(".cart_item");

        orderItem.first().waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(10000)
        );

        Assert.assertTrue(
                orderItem.count() > 0,
                "Product is missing from the order overview"
        );

        Assert.assertEquals(
                orderItem.locator(".inventory_item_name").innerText(),
                "Sauce Labs Backpack",
                "Incorrect product in the order overview"
        );

        checkoutPage.clickFinish();

        page.waitForURL(
                "**/checkout-complete.html",
                new Page.WaitForURLOptions().setTimeout(10000)
        );

        Locator confirmationMessage = page.locator(".complete-header");

        confirmationMessage.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(10000)
        );

        Assert.assertEquals(
                checkoutPage.getOrderConfirmation(),
                "Thank you for your order!",
                "Order confirmation message does not match"
        );
    }

    private void printDebugInfo() {
        System.out.println("Current URL: " + page.url());
        System.out.println("Page title: " + page.title());

        try {
            System.out.println(
                    "Page content: " + page.locator("body").innerText()
            );
        } catch (Exception e) {
            System.out.println(
                    "Could not read page content: " + e.getMessage()
            );
        }
    }
}

