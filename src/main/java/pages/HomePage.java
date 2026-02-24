package pages;

import config.ConfigReader;
import utils.SeleniumUtils;
import utils.LoggerUtil;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends SeleniumUtils {

    @FindBy(css = "button[ng-click='manager()']")
    private WebElement bankManagerLoginButton;

    @FindBy(css = "button[ng-click='customer()']")
    private WebElement customerLoginButton;

    @FindBy(css = "button[ng-click='home()']")
    private WebElement homeButton;

    public HomePage() {
        super();
    }

    public void navigateToApp() {
        String url = ConfigReader.getAppUrl();
        driver.get(url);
        LoggerUtil.info(HomePage.class, "Navigated to XYZ Bank application: " + url);
    }

    public ManagerPage clickBankManagerLogin() {
        click(bankManagerLoginButton, "Bank Manager Login Button");
        return new ManagerPage();
    }

    public CustomerLoginPage clickCustomerLogin() {
        click(customerLoginButton, "Customer Login Button");
        return new CustomerLoginPage();
    }

    public void clickHome() {
        click(homeButton, "Home Button");
    }

}
