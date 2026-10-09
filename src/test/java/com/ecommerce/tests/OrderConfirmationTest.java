
package com.ecommerce.tests;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.LoginPage;
import com.ecommerce.pages.CheckoutPage;
import com.ecommerce.pages.PaymentPage;
import com.ecommerce.pages.OrderConfirmationPage;

import org.testng.Assert;
import org.testng.annotations.Test;

public class OrderConfirmationTest extends BaseTest {

    @Test
    public void verifyOrderConfirmation() {

        // Step 1: Login
        LoginPage loginPage = new LoginPage(page);
        loginPage.login("standard_user", "secret_sauce");

        // Step 2: Add product to cart
        page.locator(".inventory_item button").first().click();

        // Step 3: Open cart
        page.locator("#shopping_cart_container a").click();

        // Step 4: Start checkout
        page.locator("#checkout").click();

        // Step 5: Enter customer details
        CheckoutPage checkoutPage = new CheckoutPage(page);

        checkoutPage.enterCustomerDetails(
                "Kalpesh",
                "Mali",
                "411001"
        );

        checkoutPage.clickContinue();

        // Step 6: Validate and finish order
        PaymentPage paymentPage = new PaymentPage(page);

        Assert.assertTrue(
                paymentPage.isOrderSummaryDisplayed(),
                "Order summary is missing"
        );

        paymentPage.finishOrder();

        // Step 7: Verify order confirmation
        OrderConfirmationPage confirmationPage =
                new OrderConfirmationPage(page);

        Assert.assertTrue(
                confirmationPage.isConfirmationDisplayed(),
                "Order confirmation header is not displayed"
        );

        Assert.assertEquals(
                confirmationPage.getConfirmationHeader(),
                "Thank you for your order!",
                "Confirmation header is incorrect"
        );

        Assert.assertTrue(
                confirmationPage.isConfirmationMessageDisplayed(),
                "Confirmation message is missing"
        );

        // Step 8: Return to products
        confirmationPage.backToProducts();

        Assert.assertTrue(
                page.url().contains("inventory.html"),
                "User did not return to the products page"
        );
    }
}