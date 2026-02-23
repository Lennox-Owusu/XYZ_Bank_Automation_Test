package listeners;

import config.WebDriverFactory;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.LoggerUtil;

import java.io.ByteArrayInputStream;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        LoggerUtil.logTestStart(result.getTestClass().getRealClass(), testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        LoggerUtil.logTestEnd(result.getTestClass().getRealClass(), testName, "PASSED");
        captureScreenshot(result, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        LoggerUtil.logTestEnd(result.getTestClass().getRealClass(), testName, "FAILED");
        LoggerUtil.error(result.getTestClass().getRealClass(),
                "Test failed: " + testName, result.getThrowable());
        captureScreenshot(result, "Test Failed");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        LoggerUtil.logTestEnd(result.getTestClass().getRealClass(), testName, "SKIPPED");
    }

    @Override
    public void onStart(ITestContext context) {
        LoggerUtil.info(TestListener.class, "========== TEST SUITE STARTED: " +
                context.getName() + " ==========");
    }

    @Override
    public void onFinish(ITestContext context) {
        LoggerUtil.info(TestListener.class, "========== TEST SUITE FINISHED: " +
                context.getName() + " ==========");
        LoggerUtil.info(TestListener.class, "Total Tests: " + context.getAllTestMethods().length);
        LoggerUtil.info(TestListener.class, "Passed: " + context.getPassedTests().size());
        LoggerUtil.info(TestListener.class, "Failed: " + context.getFailedTests().size());
        LoggerUtil.info(TestListener.class, "Skipped: " + context.getSkippedTests().size());
    }

    private void captureScreenshot(ITestResult result, String screenshotName) {
        try {
            byte[] screenshot = ((TakesScreenshot) WebDriverFactory.getDriver())
                    .getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(screenshotName + " - " + result.getMethod().getMethodName(),
                    new ByteArrayInputStream(screenshot));
            LoggerUtil.info(result.getTestClass().getRealClass(),
                    "Screenshot captured: " + screenshotName);
        } catch (Exception e) {
            LoggerUtil.error(result.getTestClass().getRealClass(),
                    "Failed to capture screenshot", e);
        }
    }
}
