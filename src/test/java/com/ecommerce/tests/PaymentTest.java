
package com.ecommerce.tests;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.LoginPage;
import com.ecommerce.pages.CheckoutPage;
import com.ecommerce.pages.PaymentPage;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.WaitForSelectorState;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PaymentTest extends BaseTest {

    @Test
    public void verifyPaymentAndOrderSummary() {

        // Step 1: Login
        LoginPage loginPage = new LoginPage(page);
        loginPage.login("standard_user", "secret_sauce");

        page.waitForURL("**/inventory.html");

        Assert.assertTrue(
                page.locator(".inventory_list").isVisible(),
                "Inventory page was not displayed"
        );

        // Step 2: Add first product to cart
        Locator addToCartButton =
                page.locator(".inventory_item button").first();

        addToCartButton.click();

        // Step 3: Open cart
        page.locator("#shopping_cart_container a").click();

        page.waitForURL("**/cart.html");

        // Step 4: Verify cart
        Assert.assertTrue(
                page.locator(".cart_item").count() > 0,
                "No product found in cart"
        );

        // Step 5: Find and click Checkout
        Locator checkoutButton = page.locator("#checkout");

        checkoutButton.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
        );

        checkoutButton.click();

        page.waitForURL("**/checkout-step-one.html");

        // Step 6: Enter customer details
        CheckoutPage checkoutPage = new CheckoutPage(page);

        checkoutPage.enterCustomerDetails(
                "Kalpesh",
                "Mali",
                "411001"
        );

        checkoutPage.clickContinue();

        page.waitForURL("**/checkout-step-two.html");

        // Step 7: Verify order summary
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
                paymentPage.getPaymentInformation().isEmpty(),
                "Payment information is missing"
        );

        Assert.assertFalse(
                paymentPage.getShippingInformation().isEmpty(),
                "Shipping information is missing"
        );

        // Step 8: Finish order
        paymentPage.finishOrder();

        page.waitForURL("**/checkout-complete.html");

        Assert.assertEquals(
                page.locator(".complete-header").innerText(),
                "Thank you for your order!",
                "Order confirmation is incorrect"
        );
    }
}