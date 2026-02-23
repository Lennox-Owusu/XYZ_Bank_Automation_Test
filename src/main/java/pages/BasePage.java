package pages;

import config.WebDriverFactory;
import utils.LoggerUtil;
import utils.WaitHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePage {

    protected WebDriver driver;
    protected WaitHelper waitHelper;

    public BasePage() {
        this.driver = WebDriverFactory.getDriver();
        this.waitHelper = new WaitHelper(driver);
        PageFactory.initElements(driver, this);
        LoggerUtil.debug(this.getClass(), "Page initialized: " + this.getClass().getSimpleName());
    }


    protected void type(WebElement element, String text, String elementName) {
        waitHelper.waitForElementToBeVisible(element);
        element.clear();
        element.sendKeys(text);
        LoggerUtil.info(this.getClass(), "Typed '" + text + "' into: " + elementName);
    }

    protected String getText(WebElement element, String elementName) {
        waitHelper.waitForElementToBeVisible(element);
        String text = element.getText();
        LoggerUtil.debug(this.getClass(), "Got text '" + text + "' from: " + elementName);
        return text;
    }

    protected boolean isDisplayed(WebElement element, String elementName) {
        try {
            waitHelper.waitForElementToBeVisible(element);
            boolean displayed = element.isDisplayed();
            LoggerUtil.debug(this.getClass(), elementName + " is displayed: " + displayed);
            return displayed;
        } catch (Exception e) {
            // Using error(Class, String, Throwable) - with exception
            LoggerUtil.error(this.getClass(), "Failed to check if element is displayed: " + elementName, e);
            return false;
        }
    }

    protected void click(WebElement element, String elementName) {
        try {
            waitHelper.waitForElementToBeClickable(element);
            element.click();
            LoggerUtil.info(this.getClass(), "Clicked on: " + elementName);
        } catch (Exception e) {
            LoggerUtil.error(this.getClass(), "Failed to click on: " + elementName, e);
            throw e; // Re-throw to fail the test
        }
    }
}
