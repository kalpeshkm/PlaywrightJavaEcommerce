package com.ecommerce.tests;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.LoginPage;
import com.ecommerce.pages.InventoryPage;

import com.microsoft.playwright.Page;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartTest extends BaseTest {

    @Test
    public void addProductToCartTest() {

        LoginPage loginPage = new LoginPage(page);

        loginPage.login(
                "standard_user",
                "secret_sauce"
        );

        InventoryPage inventoryPage =
                new InventoryPage(page);

        inventoryPage.addBackpackToCart();

        Assert.assertEquals(
                inventoryPage.getCartCount(),
                "1"
        );

        inventoryPage.openCart();

        Assert.assertTrue(
                page.url().contains("cart.html")
        );

        Assert.assertTrue(
                page.getByText(
                        "Sauce Labs Backpack",
                        new Page.GetByTextOptions().setExact(true)
                ).isVisible()
        );
    }
}