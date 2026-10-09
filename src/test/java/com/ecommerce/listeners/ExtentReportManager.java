package com.ecommerce.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExtentReportManager {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> test =
            new ThreadLocal<>();

    public static synchronized ExtentReports getExtentReports() {

        if (extent == null) {
            try {
                Path reportDirectory =
                        Paths.get("reports").toAbsolutePath();

                Files.createDirectories(reportDirectory);

                String reportPath =
                        reportDirectory.resolve("ExtentReport.html")
                                .toString();

                ExtentSparkReporter sparkReporter =
                        new ExtentSparkReporter(reportPath);

                sparkReporter.config()
                        .setDocumentTitle("Ecommerce Automation Report");

                sparkReporter.config()
                        .setReportName("Playwright Java Test Results");

                extent = new ExtentReports();
                extent.attachReporter(sparkReporter);

                extent.setSystemInfo("Framework", "Playwright Java");
                extent.setSystemInfo("Test Runner", "TestNG");
                extent.setSystemInfo("Language", "Java");
                extent.setSystemInfo("Browser", "Chromium");

            } catch (IOException e) {
                throw new RuntimeException(
                        "Unable to create report directory", e);
            }
        }

        return extent;
    }

    public static void createTest(String testName) {
        test.set(getExtentReports().createTest(testName));
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void removeTest() {
        test.remove();
    }

    public static synchronized void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }
}