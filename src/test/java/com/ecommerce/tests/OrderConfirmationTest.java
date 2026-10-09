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

        LoginPage loginPage = new LoginPage(page);
        loginPage.login("standard_user", "secret_sauce");

        page.locator(".inventory_item button").first().click();

        page.locator("#shopping_cart_container a").click();

        page.locator("#checkout").click();

        CheckoutPage checkoutPage = new CheckoutPage(page);

        checkoutPage.enterCustomerDetails(
                "Kalpesh",
                "Mali",
                "411001"
        );

        checkoutPage.clickContinue();

        PaymentPage paymentPage = new PaymentPage(page);

        Assert.assertTrue(
                paymentPage.isOrderSummaryDisplayed(),
                "Order summary is missing"
        );

        paymentPage.finishOrder();

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

        confirmationPage.backToProducts();

        Assert.assertTrue(
                page.url().contains("inventory.html"),
                "User did not return to the products page"
        );
    }
}