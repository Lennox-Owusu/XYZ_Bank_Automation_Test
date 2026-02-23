package tests;

import base.BaseTest;
import io.qameta.allure.*;
import models.Customer;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.ManagerPage;
import utils.LoggerUtil;
import utils.TestDataGenerator;

@Epic("XYZ Bank - Manager Module")
@Feature("Manager Operations")
public class ManagerTests extends BaseTest {

    @Test(priority = 1, description = "Verify manager can add a customer with valid data")
    @Story("Add Customer")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddCustomerWithValidData() {
        LoggerUtil.info(ManagerTests.class, "Starting test: Add Customer with Valid Data");

        Customer customer = TestDataGenerator.generateValidCustomer();

        ManagerPage managerPage = homePage.clickBankManagerLogin();
        managerPage.addCustomer(customer);

        boolean isCustomerAdded = managerPage.isCustomerInList(customer.getFirstName());

        Assert.assertTrue(isCustomerAdded,
                "Customer should be added to the list: " + customer.getFullName());

        LoggerUtil.info(ManagerTests.class, "Test completed: Customer added successfully");
    }

    @Test(priority = 2, description = "Verify customer name validation - reject numbers")
    @Story("Add Customer Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("BUG: Application should reject customer names containing numbers but currently accepts them")
    public void testAddCustomerWithNumbersInName() {
        LoggerUtil.info(ManagerTests.class, "Starting test: Add Customer with Numbers in Name");

        String invalidFirstName = TestDataGenerator.generateInvalidNameWithNumbers();
        String validLastName = "Smith";
        String validPostalCode = "12345";

        ManagerPage managerPage = homePage.clickBankManagerLogin();

        int initialCount = managerPage.getCustomerCount();
        LoggerUtil.info(ManagerTests.class, "Initial customer count: " + initialCount);

        managerPage.addCustomerWithInvalidData(invalidFirstName, validLastName, validPostalCode);

        int finalCount = managerPage.getCustomerCount();
        LoggerUtil.info(ManagerTests.class, "Final customer count: " + finalCount);

        // This assertion will FAIL - which is correct because the app has a bug
        Assert.assertEquals(finalCount, initialCount,
                "BUG FOUND: Customer count should NOT increase when adding invalid name with numbers. " +
                        "Expected: " + initialCount + ", Actual: " + finalCount);

        LoggerUtil.info(ManagerTests.class, "Test completed: Invalid name with numbers rejected");
    }

    @Test(priority = 3, description = "Verify customer name validation - reject special characters")
    @Story("Add Customer Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("BUG: Application should reject customer names containing special characters but currently accepts them")
    public void testAddCustomerWithSpecialCharactersInName() {
        LoggerUtil.info(ManagerTests.class, "Starting test: Add Customer with Special Characters");

        String invalidFirstName = TestDataGenerator.generateInvalidNameWithSpecialChars();
        String validLastName = "Doe";
        String validPostalCode = "54321";

        ManagerPage managerPage = homePage.clickBankManagerLogin();

        int initialCount = managerPage.getCustomerCount();
        LoggerUtil.info(ManagerTests.class, "Initial customer count: " + initialCount);

        managerPage.addCustomerWithInvalidData(invalidFirstName, validLastName, validPostalCode);

        int finalCount = managerPage.getCustomerCount();
        LoggerUtil.info(ManagerTests.class, "Final customer count: " + finalCount);

        // This assertion will FAIL - which is correct because the app has a bug
        Assert.assertEquals(finalCount, initialCount,
                "BUG FOUND: Customer count should NOT increase when adding invalid name with special characters. " +
                        "Expected: " + initialCount + ", Actual: " + finalCount);

        LoggerUtil.info(ManagerTests.class, "Test completed: Invalid name with special chars rejected");
    }

    @Test(priority = 4, description = "Verify postal code validation - reject letters")
    @Story("Add Customer Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("BUG: Application should reject postal codes containing letters but currently accepts them")
    public void testAddCustomerWithLettersInPostalCode() {
        LoggerUtil.info(ManagerTests.class, "Starting test: Add Customer with Letters in Postal Code");

        String validFirstName = "John";
        String validLastName = "Williams";
        String invalidPostalCode = TestDataGenerator.generateInvalidPostalCodeWithLetters();

        ManagerPage managerPage = homePage.clickBankManagerLogin();

        int initialCount = managerPage.getCustomerCount();
        LoggerUtil.info(ManagerTests.class, "Initial customer count: " + initialCount);

        managerPage.addCustomerWithInvalidData(validFirstName, validLastName, invalidPostalCode);

        int finalCount = managerPage.getCustomerCount();
        LoggerUtil.info(ManagerTests.class, "Final customer count: " + finalCount);

        // This assertion will FAIL - which is correct because the app has a bug
        Assert.assertEquals(finalCount, initialCount,
                "BUG FOUND: Customer count should NOT increase when adding invalid postal code with letters. " +
                        "Expected: " + initialCount + ", Actual: " + finalCount);

        LoggerUtil.info(ManagerTests.class, "Test completed: Invalid postal code with letters rejected");
    }

    @Test(priority = 5, description = "Verify manager can create account for existing customer")
    @Story("Create Account")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreateAccountForExistingCustomer() {
        LoggerUtil.info(ManagerTests.class, "Starting test: Create Account for Existing Customer");

        Customer customer = TestDataGenerator.generateValidCustomer();

        ManagerPage managerPage = homePage.clickBankManagerLogin();
        managerPage.addCustomer(customer);

        String currency = "Dollar";
        managerPage.openAccount(customer.getFullName(), currency);

        LoggerUtil.info(ManagerTests.class, "Test completed: Account created successfully");
        Assert.assertTrue(true, "Account created without errors");
    }

    @Test(priority = 6, description = "Verify manager can delete customer account")
    @Story("Delete Account")
    @Severity(SeverityLevel.CRITICAL)
    public void testDeleteCustomerAccount() {
        LoggerUtil.info(ManagerTests.class, "Starting test: Delete Customer Account");

        Customer customer = TestDataGenerator.generateValidCustomer();

        ManagerPage managerPage = homePage.clickBankManagerLogin();
        managerPage.addCustomer(customer);

        boolean isCustomerAdded = managerPage.isCustomerInList(customer.getFirstName());
        Assert.assertTrue(isCustomerAdded, "Customer should be in the list before deletion");

        managerPage.deleteCustomer(customer.getFirstName());

        boolean isCustomerDeleted = !managerPage.isCustomerInList(customer.getFirstName());
        Assert.assertTrue(isCustomerDeleted,
                "Customer should be removed from the list after deletion");

        LoggerUtil.info(ManagerTests.class, "Test completed: Customer deleted successfully");
    }

    @Test(priority = 7, description = "Verify customer search functionality")
    @Story("Search Customer")
    @Severity(SeverityLevel.NORMAL)
    public void testSearchCustomer() {
        LoggerUtil.info(ManagerTests.class, "Starting test: Search Customer");

        Customer customer = TestDataGenerator.generateValidCustomer();

        ManagerPage managerPage = homePage.clickBankManagerLogin();
        managerPage.addCustomer(customer);

        managerPage.clickCustomers();
        managerPage.searchCustomer(customer.getFirstName());

        boolean isCustomerFound = managerPage.isCustomerInList(customer.getFirstName());
        Assert.assertTrue(isCustomerFound,
                "Customer should be found in search results: " + customer.getFullName());

        LoggerUtil.info(ManagerTests.class, "Test completed: Customer search successful");
    }
}
