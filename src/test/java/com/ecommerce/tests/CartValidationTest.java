
package com.ecommerce.tests;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.CartValidationPage;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CartValidationTest extends BaseTest {
    private void loginAndAddProduct(Page page) {

        // Open SauceDemo login page
        page.navigate("https://www.saucedemo.com/");

        // Enter credentials
        page.locator("[data-test='username']")
                .fill("standard_user");

        page.locator("[data-test='password']")
                .fill("secret_sauce");

        // Click login
        page.locator("[data-test='login-button']")
                .click();

        // Wait for inventory page
        try {
            page.waitForURL(
                    "**/inventory.html",
                    new Page.WaitForURLOptions().setTimeout(15000)
            );
        } catch (TimeoutError e) {
            printLoginDebugInfo(page);
            Assert.fail("Login failed. Current URL: " + page.url());
            return;
        }

        // Wait for inventory list to become visible
        try {
            page.locator(".inventory_list").waitFor(
                    new com.microsoft.playwright.Locator.WaitForOptions()
                            .setState(
                                    com.microsoft.playwright.options.WaitForSelectorState.VISIBLE
                            )
                            .setTimeout(15000)
            );
        } catch (TimeoutError e) {
            printLoginDebugInfo(page);
            Assert.fail(
                    "Inventory page opened, but inventory list is not visible. URL: "
                            + page.url()
            );
            return;
        }

        // Add backpack to cart
        page.locator("[data-test='add-to-cart-sauce-labs-backpack']")
                .click();

        // Verify cart badge
        Assert.assertEquals(
                page.locator(".shopping_cart_badge").innerText(),
                "1",
                "Cart badge should display 1"
        );

        // Open cart
        page.locator(".shopping_cart_link").click();

        // Wait for cart page
        page.waitForURL(
                "**/cart.html",
                new Page.WaitForURLOptions().setTimeout(10000)
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
            System.out.println("Could not read page content: " + e.getMessage());
        }

        try {
            Path screenshotPath = Paths.get(
                    "screenshots", "login-debug.png"
            );

            Files.createDirectories(screenshotPath.getParent());

            page.screenshot(
                    new Page.ScreenshotOptions()
                            .setPath(screenshotPath)
                            .setFullPage(true)
            );

            System.out.println(
                    "Login debug screenshot saved: "
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

        Assert.assertTrue(
                cart.isCheckoutButtonDisplayed(),
                "Checkout button should be displayed"
        );
    }
}