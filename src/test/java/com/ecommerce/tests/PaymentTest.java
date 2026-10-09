package com.ecommerce.tests;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.LoginPage;
import com.ecommerce.pages.CheckoutPage;
import com.ecommerce.pages.PaymentPage;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.WaitForSelectorState;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PaymentTest extends BaseTest {

    @Test
    public void verifyPaymentAndOrderSummary() {

        LoginPage loginPage = new LoginPage(page);
        loginPage.login("standard_user", "secret_sauce");

        page.waitForURL(
                "**/inventory.html",
                new Page.WaitForURLOptions().setTimeout(15000)
        );

        page.locator(".inventory_list").waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(15000)
        );

        Assert.assertTrue(
                page.locator(".inventory_list").isVisible(),
                "Inventory page was not displayed"
        );

        Locator addToCartButton =
                page.locator("[data-test='add-to-cart-sauce-labs-backpack']");

        addToCartButton.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(10000)
        );

        addToCartButton.click();

        Assert.assertEquals(
                page.locator(".shopping_cart_badge").innerText(),
                "1",
                "Cart should contain one product"
        );

        page.locator(".shopping_cart_link").click();

        page.waitForURL(
                "**/cart.html",
                new Page.WaitForURLOptions().setTimeout(10000)
        );

        Locator cartItem =
                page.locator(".cart_item");

        try {
            cartItem.first().waitFor(
                    new Locator.WaitForOptions()
                            .setState(WaitForSelectorState.VISIBLE)
                            .setTimeout(10000)
            );
        } catch (TimeoutError e) {
            Assert.fail(
                    "No product appeared in the cart. Current URL: "
                            + page.url()
                            + ". Page content: "
                            + page.locator("body").innerText()
            );
        }

        Assert.assertTrue(
                cartItem.count() > 0,
                "No product found in cart"
        );

        Assert.assertEquals(
                cartItem.locator(".inventory_item_name").innerText(),
                "Sauce Labs Backpack",
                "Unexpected product found in cart"
        );

        Locator checkoutButton =
                page.locator("[data-test='checkout']");

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

        CheckoutPage checkoutPage = new CheckoutPage(page);

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

        PaymentPage paymentPage = new PaymentPage(page);

        Assert.assertTrue(
                paymentPage.isOrderSummaryDisplayed(),
                "Order summary is not displayed"
        );

        Assert.assertTrue(
                paymentPage.getSubtotal().contains("Item total:"),
                "Item subtotal is missing"
        );

        Assert.assertTrue(
                paymentPage.getTax().contains("Tax:"),
                "Tax information is missing"
        );

        Assert.assertTrue(
                paymentPage.getTotal().contains("Total:"),
                "Order total is missing"
        );

        Assert.assertFalse(
                paymentPage.getPaymentInformation().trim().isEmpty(),
                "Payment information is missing"
        );

        Assert.assertFalse(
                paymentPage.getShippingInformation().trim().isEmpty(),
                "Shipping information is missing"
        );

        paymentPage.finishOrder();

        page.waitForURL(
                "**/checkout-complete.html",
                new Page.WaitForURLOptions().setTimeout(10000)
        );

        Assert.assertEquals(
                page.locator(".complete-header").innerText(),
                "Thank you for your order!",
                "Order confirmation is incorrect"
        );
    }
}

