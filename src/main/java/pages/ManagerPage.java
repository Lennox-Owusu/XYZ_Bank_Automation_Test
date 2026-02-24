package pages;

import models.Customer;
import pagesHelper.BasePage;
import utils.LoggerUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class ManagerPage extends BasePage {

    @FindBy(css = "button[ng-click='addCust()']")
    private WebElement addCustomerButton;

    @FindBy(css = "button[ng-click='openAccount()']")
    private WebElement openAccountButton;

    @FindBy(css = "button[ng-click='showCust()']")
    private WebElement customersButton;

    @FindBy(css = "input[ng-model='fName']")
    private WebElement firstNameInput;

    @FindBy(css = "input[ng-model='lName']")
    private WebElement lastNameInput;

    @FindBy(css = "input[ng-model='postCd']")
    private WebElement postCodeInput;

    @FindBy(css = "button[type='submit']")
    private WebElement addCustomerSubmitButton;

    @FindBy(id = "userSelect")
    private WebElement customerDropdown;

    @FindBy(id = "currency")
    private WebElement currencyDropdown;

    @FindBy(css = "button[type='submit']")
    private WebElement processButton;

    @FindBy(css = "input[ng-model='searchCustomer']")
    private WebElement searchCustomerInput;

    @FindBy(css = "table tbody tr")
    private List<WebElement> customerRows;

    public ManagerPage(WebElement addCustomerButton, WebElement openAccountButton, WebElement searchCustomerInput, List<WebElement> customerRows) {
        super();
        this.addCustomerButton = addCustomerButton;
        this.openAccountButton = openAccountButton;
        this.searchCustomerInput = searchCustomerInput;
        this.customerRows = customerRows;
    }

    public ManagerPage() {

    }

    public void clickAddCustomer() {
        click(addCustomerButton, "Add Customer Tab");
    }

    public void clickOpenAccount() {
        click(openAccountButton, "Open Account Tab");
    }

    public void clickCustomers() {
        click(customersButton, "Customers Tab");
    }

    public void addCustomer(Customer customer) {
        LoggerUtil.logStep(ManagerPage.class, "Adding customer: " + customer.getFullName());

        clickAddCustomer();
        type(firstNameInput, customer.getFirstName(), "First Name");
        type(lastNameInput, customer.getLastName(), "Last Name");
        type(postCodeInput, customer.getPostalCode(), "Post Code");
        click(addCustomerSubmitButton, "Add Customer Submit Button");

        handleAlert();
        LoggerUtil.info(ManagerPage.class, "Customer added successfully: " + customer.getFullName());
    }

    public void addCustomerWithInvalidData(String firstName, String lastName, String postCode) {
        LoggerUtil.logStep(ManagerPage.class, "Attempting to add customer with invalid data");

        clickAddCustomer();

        if (firstName != null) {
            type(firstNameInput, firstName, "First Name");
        }
        if (lastName != null) {
            type(lastNameInput, lastName, "Last Name");
        }
        if (postCode != null) {
            type(postCodeInput, postCode, "Post Code");
        }

        click(addCustomerSubmitButton, "Add Customer Submit Button");

        // Handle alert that appears after submission
        handleAlert();
    }


    public void openAccount(String customerName, String currency) {
        LoggerUtil.logStep(ManagerPage.class, "Opening account for: " + customerName);

        clickOpenAccount();
        selectCustomerFromDropdown(customerName);
        selectCurrencyFromDropdown(currency);
        click(processButton, "Process Button");

        handleAlert();
        LoggerUtil.info(ManagerPage.class, "Account opened successfully for: " + customerName);
    }

    public void selectCustomerFromDropdown(String customerName) {
        click(customerDropdown, "Customer Dropdown");
        WebElement customerOption = driver.findElement(
                By.xpath("//option[contains(text(),'" + customerName + "')]")
        );
        click(customerOption, "Customer: " + customerName);
    }

    public void selectCurrencyFromDropdown(String currency) {
        click(currencyDropdown, "Currency Dropdown");
        WebElement currencyOption = driver.findElement(
                By.xpath("//option[text()='" + currency + "']")
        );
        click(currencyOption, "Currency: " + currency);
    }

    public void searchCustomer(String searchTerm) {
        clickCustomers();
        type(searchCustomerInput, searchTerm, "Search Customer");
        LoggerUtil.info(ManagerPage.class, "Searched for customer: " + searchTerm);
    }

    public boolean isCustomerInList(String customerName) {
        clickCustomers();
        searchCustomer(customerName);

        for (WebElement row : customerRows) {
            if (row.getText().contains(customerName)) {
                LoggerUtil.info(ManagerPage.class, "Customer found in list: " + customerName);
                return true;
            }
        }

        LoggerUtil.info(ManagerPage.class, "Customer not found in list: " + customerName);
        return false;
    }

    public void deleteCustomer(String customerName) {
        LoggerUtil.logStep(ManagerPage.class, "Deleting customer: " + customerName);

        clickCustomers();
        searchCustomer(customerName);

        WebElement deleteButton = driver.findElement(
                By.xpath("//tr[contains(.,'" + customerName + "')]//button[text()='Delete']")
        );
        click(deleteButton, "Delete button for: " + customerName);

        LoggerUtil.info(ManagerPage.class, "Customer deleted: " + customerName);
    }

    public int getCustomerCount() {
        // Handle any pending alerts first
        handleAlert();

        waitHelper.waitForSeconds(1);
        clickCustomers();

        waitHelper.waitForSeconds(1);
        int count = customerRows.size();
        LoggerUtil.debug(ManagerPage.class, "Total customers: " + count);
        return count;
    }


    private void handleAlert() {
        try {
            waitHelper.waitForSeconds(1);
            String alertText = driver.switchTo().alert().getText();
            LoggerUtil.info(ManagerPage.class, "Alert message: " + alertText);
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            LoggerUtil.debug(ManagerPage.class, "No alert present");
        }
    }
}
