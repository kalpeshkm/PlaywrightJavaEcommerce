package com.ecommerce.tests;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.CartValidationPage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CartValidationTest extends BaseTest {

    private static final String BASE_URL =
            "https://www.saucedemo.com/";

    private static final String USERNAME = "standard_user";
    private static final String PASSWORD = "secret_sauce";

    private void waitUntilVisible(Page page, String selector) {
        page.locator(selector).waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(15000)
        );
    }

    private void loginAndAddProduct(Page page) {

        // Open SauceDemo
        page.navigate(BASE_URL);

        // Login
        page.locator("[data-test='username']").fill(USERNAME);
        page.locator("[data-test='password']").fill(PASSWORD);
        page.locator("[data-test='login-button']").click();

        // Wait for inventory page
        try {
            page.waitForURL(
                    "**/inventory.html",
                    new Page.WaitForURLOptions().setTimeout(15000)
            );

            waitUntilVisible(page, ".inventory_list");

        } catch (TimeoutError e) {
            printLoginDebugInfo(page);
            Assert.fail(
                    "Login or inventory loading failed. Current URL: "
                            + page.url()
            );
        }

        // Add Sauce Labs Backpack
        String addButtonSelector =
                "[data-test='add-to-cart-sauce-labs-backpack']";

        waitUntilVisible(page, addButtonSelector);
        page.locator(addButtonSelector).click();

        // Verify cart badge
        waitUntilVisible(page, ".shopping_cart_badge");

        Assert.assertEquals(
                page.locator(".shopping_cart_badge").innerText(),
                "1",
                "Cart badge should display 1"
        );

        // Open cart
        page.locator(".shopping_cart_link").click();

        // Wait for cart page
        try {
            page.waitForURL(
                    "**/cart.html",
                    new Page.WaitForURLOptions().setTimeout(15000)
            );

            waitUntilVisible(page, ".cart_list");

        } catch (TimeoutError e) {
            printLoginDebugInfo(page);
            Assert.fail(
                    "Cart page did not open. Current URL: " + page.url()
            );
        }

        // Wait for the product to appear in the cart
        waitUntilVisible(page, ".cart_item");

        Assert.assertTrue(
                page.locator(".cart_item").count() > 0,
                "Product was not added to the cart"
        );
    }

    private void printLoginDebugInfo(Page page) {

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

        try {
            Path screenshotPath =
                    Paths.get("screenshots", "login-debug.png");

            Files.createDirectories(screenshotPath.getParent());

            page.screenshot(
                    new Page.ScreenshotOptions()
                            .setPath(screenshotPath)
                            .setFullPage(true)
            );

            System.out.println(
                    "Debug screenshot saved: "
                            + screenshotPath.toAbsolutePath()
            );

        } catch (Exception e) {
            System.out.println(
                    "Could not save debug screenshot: " + e.getMessage()
            );
        }
    }

    @Test
    public void verifyProductDetailsInCart() {

        Page page = getPage();
        loginAndAddProduct(page);

        CartValidationPage cart = new CartValidationPage(page);

        Assert.assertTrue(
                cart.isCartPageDisplayed(),
                "Cart page should be displayed"
        );

        Assert.assertEquals(
                cart.getCartItemCount(),
                1,
                "Cart should contain one product"
        );

        Assert.assertEquals(
                cart.getProductName(),
                "Sauce Labs Backpack",
                "Product name should match"
        );

        Assert.assertEquals(
                cart.getProductPrice(),
                "$29.99",
                "Product price should match"
        );

        Assert.assertEquals(
                cart.getProductQuantity(),
                "1",
                "Product quantity should be one"
        );
    }

    @Test
    public void verifyProductCanBeRemovedFromCart() {

        Page page = getPage();
        loginAndAddProduct(page);

        CartValidationPage cart = new CartValidationPage(page);

        cart.removeBackpack();

        Assert.assertEquals(
                cart.getCartItemCount(),
                0,
                "Cart should be empty after removing the product"
        );

        Assert.assertFalse(
                page.locator(".shopping_cart_badge").isVisible(),
                "Cart badge should not be visible after removing the product"
        );
    }

    @Test
    public void verifyCheckoutButtonIsDisplayed() {

        Page page = getPage();
        loginAndAddProduct(page);

        CartValidationPage cart = new CartValidationPage(page);

        waitUntilVisible(page, "[data-test='checkout']");

        Assert.assertTrue(
                cart.isCheckoutButtonDisplayed(),
                "Checkout button should be displayed"
        );
    }
}
