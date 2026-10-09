
package com.ecommerce.tests;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.LoginPage;
import com.ecommerce.pages.ProductDetailsPage;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ProductDetailsTest extends BaseTest {

    @Test
    public void verifyProductDetailsAndAddToCart() {

        LoginPage loginPage = new LoginPage(page);

        loginPage.login("standard_user", "secret_sauce");

        ProductDetailsPage productPage =
                new ProductDetailsPage(page);

        productPage.openFirstProduct();

        Assert.assertFalse(
                productPage.getProductName().isEmpty(),
                "Product name is missing"
        );

        Assert.assertFalse(
                productPage.getProductDescription().isEmpty(),
                "Product description is missing"
        );

        Assert.assertTrue(
                productPage.getProductPrice().startsWith("$"),
                "Product price is invalid"
        );

        productPage.addToCart();

        Assert.assertTrue(
                productPage.isRemoveButtonDisplayed(),
                "Product was not added to cart"
        );

        Assert.assertEquals(
                productPage.getCartCount(),
                "1",
                "Cart count is incorrect"
        );
    }
}