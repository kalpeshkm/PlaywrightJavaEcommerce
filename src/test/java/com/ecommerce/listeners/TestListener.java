
package com.ecommerce.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

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

        if (test != null) {
            test.log(Status.FAIL, "Test failed");

            if (result.getThrowable() != null) {
                test.log(
                        Status.FAIL,
                        result.getThrowable()
                );
            }
        }

        ExtentReportManager.removeTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        ExtentTest test = ExtentReportManager.getTest();

        if (test != null) {
            test.log(Status.SKIP, "Test skipped");

            if (result.getThrowable() != null) {
                test.log(
                        Status.SKIP,
                        result.getThrowable()
                );
            }
        }

        ExtentReportManager.removeTest();
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.flushReport();
    }
}