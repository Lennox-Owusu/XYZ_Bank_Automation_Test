package pages;

import utils.LoggerUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CustomerLoginPage extends BasePage {

    @FindBy(id = "userSelect")
    private WebElement customerDropdown;

    @FindBy(css = "button[type='submit']")
    private WebElement loginButton;

    public CustomerLoginPage() {
        super();
    }

    public AccountPage loginAsCustomer(String customerName) {
        LoggerUtil.logStep(CustomerLoginPage.class, "Logging in as customer: " + customerName);

        click(customerDropdown, "Customer Dropdown");

        WebElement customerOption = driver.findElement(
                By.xpath("//option[contains(text(),'" + customerName + "')]")
        );
        click(customerOption, "Customer: " + customerName);

        click(loginButton, "Login Button");

        LoggerUtil.info(CustomerLoginPage.class, "Successfully logged in as: " + customerName);
        return new AccountPage();
    }

    public boolean isCustomerAvailable(String customerName) {
        click(customerDropdown, "Customer Dropdown");

        try {
            WebElement customerOption = driver.findElement(
                    By.xpath("//option[contains(text(),'" + customerName + "')]")
            );
            return customerOption.isDisplayed();
        } catch (Exception e) {
            LoggerUtil.info(CustomerLoginPage.class, "Customer not found in dropdown: " + customerName);
            return false;
        }
    }
}
