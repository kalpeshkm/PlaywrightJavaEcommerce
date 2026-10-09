
package com.ecommerce.tests;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.CheckoutPage;
import com.ecommerce.pages.LoginPage;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CheckoutTest extends BaseTest {

    @Test
    public void verifySuccessfulCheckout() {

        // Step 1: Login
        LoginPage loginPage = new LoginPage(page);
        loginPage.login("standard_user", "secret_sauce");

        // Step 2: Verify inventory page
        page.waitForURL("**/inventory.html");

        Assert.assertTrue(
                page.locator(".inventory_list").isVisible(),
                "Inventory page is not displayed"
        );

        // Step 3: Add Sauce Labs Backpack to cart
        page.locator(
                "[data-test='add-to-cart-sauce-labs-backpack']"
        ).click();

        // Step 4: Verify cart badge
        Assert.assertEquals(
                page.locator(".shopping_cart_badge").innerText(),
                "1",
                "Product was not added to the cart"
        );

        // Step 5: Open cart
        CheckoutPage checkoutPage = new CheckoutPage(page);
        checkoutPage.openCart();

        // Step 6: Verify product in cart
        Assert.assertTrue(
                page.getByText(
                        "Sauce Labs Backpack",
                        new Page.GetByTextOptions().setExact(true)
                ).isVisible(),
                "Sauce Labs Backpack is missing from the cart"
        );

        // Step 7: Click Checkout
        checkoutPage.clickCheckout();
        page.waitForURL("**/checkout-step-one.html");

        Assert.assertTrue(
                checkoutPage.isCheckoutInformationDisplayed(),
                "Checkout information page is not displayed"
        );

        // Step 8: Enter customer information
        checkoutPage.enterCustomerDetails(
                "Kalpesh",
                "Mali",
                "411001"
        );

        checkoutPage.clickContinue();
        page.waitForURL("**/checkout-step-two.html");

        // Step 9: Verify order overview
        Assert.assertTrue(
                page.locator(".cart_item").count() > 0,
                "Product is missing from the order overview"
        );

        // Step 10: Finish order
        checkoutPage.clickFinish();
        page.waitForURL("**/checkout-complete.html");

        Assert.assertEquals(
                checkoutPage.getOrderConfirmation(),
                "Thank you for your order!",
                "Order confirmation message does not match"
        );
    }
}

