package utils;

import config.ConfigReader;
import config.WebDriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class SeleniumUtils {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public SeleniumUtils() {
        this.driver = WebDriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
        PageFactory.initElements(driver, this);
        LoggerUtil.debug(this.getClass(), "Page initialized: " + this.getClass().getSimpleName());
    }

    //WAIT METHODS

    protected WebElement waitForElementToBeClickable(WebElement element) {
        LoggerUtil.debug(this.getClass(), "Waiting for element to be clickable");
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected void waitForElementToBeVisible(WebElement element) {
        LoggerUtil.debug(this.getClass(), "Waiting for element to be visible");
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            LoggerUtil.error(this.getClass(), "Wait interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    // ========== INTERACTION METHODS ==========

    protected void click(WebElement element, String elementName) {
        try {
            waitForElementToBeClickable(element);
            element.click();
            LoggerUtil.info(this.getClass(), "Clicked on: " + elementName);
        } catch (Exception e) {
            LoggerUtil.error(this.getClass(), "Failed to click on: " + elementName, e);
            throw e;
        }
    }

    protected void type(WebElement element, String text, String elementName) {
        waitForElementToBeVisible(element);
        element.clear();
        element.sendKeys(text);
        LoggerUtil.info(this.getClass(), "Typed '" + text + "' into: " + elementName);
    }

    protected String getText(WebElement element, String elementName) {
        waitForElementToBeVisible(element);
        String text = element.getText();
        LoggerUtil.debug(this.getClass(), "Got text '" + text + "' from: " + elementName);
        return text;
    }

    protected boolean isDisplayed(WebElement element, String elementName) {
        try {
            waitForElementToBeVisible(element);
            boolean displayed = element.isDisplayed();
            LoggerUtil.debug(this.getClass(), elementName + " is displayed: " + displayed);
            return displayed;
        } catch (Exception e) {
            LoggerUtil.error(this.getClass(), "Failed to check if element is displayed: " + elementName, e);
            return false;
        }
    }
}
