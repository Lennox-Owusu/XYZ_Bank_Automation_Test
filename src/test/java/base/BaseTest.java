package base;

import config.ConfigReader;
import config.WebDriverFactory;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.HomePage;
import utils.LoggerUtil;

import java.io.ByteArrayInputStream;

public class BaseTest {

    protected HomePage homePage;

    @BeforeMethod
    public void setUp() {
        LoggerUtil.info(BaseTest.class, "========== SETUP STARTED ==========");
        LoggerUtil.info(BaseTest.class, "Browser: " + ConfigReader.getBrowser());
        LoggerUtil.info(BaseTest.class, "Application URL: " + ConfigReader.getAppUrl());

        homePage = new HomePage();
        homePage.navigateToApp();

        LoggerUtil.info(BaseTest.class, "========== SETUP COMPLETED ==========");
    }

    @AfterMethod
    public void tearDown() {
        LoggerUtil.info(BaseTest.class, "========== TEARDOWN STARTED ==========");

        takeScreenshot();

        WebDriverFactory.quitDriver();

        LoggerUtil.info(BaseTest.class, "========== TEARDOWN COMPLETED ==========");
    }

    protected void takeScreenshot() {
        try {
            byte[] screenshot = ((TakesScreenshot) WebDriverFactory.getDriver())
                    .getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Final State", new ByteArrayInputStream(screenshot));
            LoggerUtil.info(BaseTest.class, "Screenshot captured: " + "Final State");
        } catch (Exception e) {
            LoggerUtil.error(BaseTest.class, "Failed to capture screenshot", e);
        }
    }
}
