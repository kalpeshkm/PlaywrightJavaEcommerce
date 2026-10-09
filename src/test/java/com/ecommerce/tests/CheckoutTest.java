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

        LoginPage loginPage = new LoginPage(page);
        loginPage.login("standard_user", "secret_sauce");

        page.waitForURL("**/inventory.html");

        Assert.assertTrue(
                page.locator(".inventory_list").isVisible(),
                "Inventory page is not displayed"
        );

        page.locator(
                "[data-test='add-to-cart-sauce-labs-backpack']"
        ).click();

        Assert.assertEquals(
                page.locator(".shopping_cart_badge").innerText(),
                "1",
                "Product was not added to the cart"
        );

        CheckoutPage checkoutPage = new CheckoutPage(page);
        checkoutPage.openCart();

        Assert.assertTrue(
                page.getByText(
                        "Sauce Labs Backpack",
                        new Page.GetByTextOptions().setExact(true)
                ).isVisible(),
                "Sauce Labs Backpack is missing from the cart"
        );

        checkoutPage.clickCheckout();
        page.waitForURL("**/checkout-step-one.html");

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
        page.waitForURL("**/checkout-step-two.html");

        Assert.assertTrue(
                page.locator(".cart_item").count() > 0,
                "Product is missing from the order overview"
        );

        checkoutPage.clickFinish();
        page.waitForURL("**/checkout-complete.html");

        Assert.assertEquals(
                checkoutPage.getOrderConfirmation(),
                "Thank you for your order!",
                "Order confirmation message does not match"
        );
    }
}

