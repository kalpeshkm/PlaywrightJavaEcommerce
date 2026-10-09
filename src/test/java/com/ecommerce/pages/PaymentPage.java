
package com.ecommerce.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;

public class PaymentPage {

    private final Page page;

    private final String paymentInformation =
            ".summary_value_label";

    private final String subtotal = ".summary_subtotal_label";
    private final String tax = ".summary_tax_label";
    private final String total = ".summary_total_label";
    private final String finishButton = "#finish";

    public PaymentPage(Page page) {
        this.page = page;
    }

    // Payment information: SauceCard #31337
    public String getPaymentInformation() {
        return page.locator(paymentInformation)
                .nth(0)
                .innerText();
    }

    // Shipping information: Free Pony Express Delivery!
    public String getShippingInformation() {
        return page.locator(paymentInformation)
                .nth(1)
                .innerText();
    }

    public String getSubtotal() {
        return page.locator(subtotal).innerText();
    }

    public String getTax() {
        return page.locator(tax).innerText();
    }

    public String getTotal() {
        return page.locator(total).innerText();
    }

    public boolean isOrderSummaryDisplayed() {
        return page.locator(subtotal).isVisible()
                && page.locator(tax).isVisible()
                && page.locator(total).isVisible();
    }

    public void finishOrder() {
        page.locator(finishButton).click();
    }
}