package com.ecommerce.base;

import com.microsoft.playwright.*;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseTest {

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeMethod
    public void setUp() {

        playwright = Playwright.create();

        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
        );

        context = browser.newContext();
        page = context.newPage();

        page.navigate("https://www.saucedemo.com/");
    }

    public Page getPage() {
        return page;
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {

        try {
            if (result.getStatus() == ITestResult.FAILURE
                    && page != null
                    && !page.isClosed()) {

                Path screenshotDirectory =
                        Paths.get("screenshots", "failures");

                Files.createDirectories(screenshotDirectory);

                String timestamp = LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
                );

                String testName = result.getMethod().getMethodName()
                        .replaceAll("[^a-zA-Z0-9_-]", "_");

                Path screenshotPath = screenshotDirectory.resolve(
                        testName + "_" + timestamp + ".png"
                );

                page.screenshot(
                        new Page.ScreenshotOptions()
                                .setPath(screenshotPath)
                                .setFullPage(true)
                );

                System.out.println(
                        "Failure screenshot saved: "
                                + screenshotPath.toAbsolutePath()
                );
            }

        } catch (Exception e) {
            System.err.println(
                    "Failed to capture screenshot: " + e.getMessage()
            );

        } finally {
            if (context != null) {
                context.close();
            }

            if (browser != null) {
                browser.close();
            }

            if (playwright != null) {
                playwright.close();
            }
        }
    }
}