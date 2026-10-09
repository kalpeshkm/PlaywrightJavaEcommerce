package com.ecommerce.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.ecommerce.base.BaseTest;
import com.microsoft.playwright.Page;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.stream.Stream;

public class TestListener implements ITestListener {

    private static final Path SCREENSHOT_DIR =
            Paths.get("screenshots", "failures");

    @Override
    public void onStart(ITestContext context) {
        ExtentReportManager.getExtentReports();
    }

    @Override
    public void onTestStart(ITestResult result) {

        ExtentReportManager.createTest(
                result.getTestClass().getName()
                        + " - "
                        + result.getMethod().getMethodName()
        );

        ExtentReportManager.getTest().log(
                Status.INFO,
                "Test execution started"
        );
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        ExtentTest test = ExtentReportManager.getTest();

        if (test != null) {
            test.log(Status.PASS, "Test passed successfully");
        }

        ExtentReportManager.removeTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {

        ExtentTest test = ExtentReportManager.getTest();

        if (test == null) {
            return;
        }

        test.log(Status.FAIL, "Test failed");

        if (result.getThrowable() != null) {
            test.log(Status.FAIL, result.getThrowable());
        }

        try {
            Path screenshot = captureFailureScreenshot(result);

            if (screenshot != null && Files.exists(screenshot)) {

                String reportImagePath =
                        "../screenshots/failures/"
                                + screenshot.getFileName();

                test.fail(
                        "Failure Screenshot",
                        MediaEntityBuilder
                                .createScreenCaptureFromPath(reportImagePath)
                                .build()
                );

                System.out.println(
                        "Failure screenshot saved: "
                                + screenshot.toAbsolutePath()
                );

            } else {
                test.log(
                        Status.WARNING,
                        "Screenshot unavailable. The browser page may already be closed."
                );
            }

        } catch (Exception e) {
            test.log(
                    Status.WARNING,
                    "Screenshot capture failed: " + e.getMessage()
            );

            System.err.println(
                    "Could not capture screenshot: " + e.getMessage()
            );
        } finally {
            ExtentReportManager.removeTest();
        }
    }

    private Path captureFailureScreenshot(ITestResult result)
            throws Exception {

        Files.createDirectories(SCREENSHOT_DIR);

        Object instance = result.getInstance();

        if (instance instanceof BaseTest) {

            BaseTest baseTest = (BaseTest) instance;
            Page page = baseTest.getPage();

            if (page != null && !page.isClosed()) {

                String timestamp = LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS")
                );

                String methodName = result.getMethod()
                        .getMethodName()
                        .replaceAll("[^a-zA-Z0-9_-]", "_");

                Path screenshot = SCREENSHOT_DIR.resolve(
                        methodName + "_" + timestamp + ".png"
                );

                page.screenshot(
                        new Page.ScreenshotOptions()
                                .setPath(screenshot)
                                .setFullPage(true)
                );

                return screenshot;
            }
        }

        String prefix = result.getMethod()
                .getMethodName()
                .replaceAll("[^a-zA-Z0-9_-]", "_") + "_";

        try (Stream<Path> files = Files.list(SCREENSHOT_DIR)) {

            return files
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName()
                            .toString()
                            .startsWith(prefix))
                    .filter(path -> path.getFileName()
                            .toString()
                            .endsWith(".png"))
                    .max(Comparator.comparingLong(
                            path -> path.toFile().lastModified()
                    ))
                    .orElse(null);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        ExtentTest test = ExtentReportManager.getTest();

        if (test != null) {
            test.log(Status.SKIP, "Test skipped");

            if (result.getThrowable() != null) {
                test.log(Status.SKIP, result.getThrowable());
            }
        }

        ExtentReportManager.removeTest();
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.flushReport();
    }
}