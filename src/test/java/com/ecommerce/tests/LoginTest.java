
package com.ecommerce.tests;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.LoginPage;
import com.ecommerce.pages.InventoryPage;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test
    public void validLoginTest() {

        LoginPage loginPage = new LoginPage(page);

        loginPage.login(
                "standard_user",
                "secret_sauce"
        );

        InventoryPage inventoryPage =
                new InventoryPage(page);

        Assert.assertEquals(
                inventoryPage.getPageTitle(),
                "Products"
        );

        Assert.assertTrue(
                page.url().contains("inventory.html")
        );
    }

    @Test
    public void invalidLoginTest() {

        LoginPage loginPage = new LoginPage(page);

        loginPage.login(
                "wrong_user",
                "wrong_password"
        );

        Assert.assertTrue(
                loginPage.getErrorMessage()
                        .contains("Username and password do not match")
        );
    }
}