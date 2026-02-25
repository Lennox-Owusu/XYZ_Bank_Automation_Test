package tests.customer;

import base.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.account.AccountPage;
import pages.customer.CustomerLoginPage;
import pages.manager.ManagerPage;
import utils.LoggerUtil;
import utils.TestDataGenerator;

@Epic("XYZ Bank - Customer Module")
@Feature("Customer Operations")
public class CustomerTests extends BaseTest {

    private String customerFullName;

    @BeforeMethod
    public void createTestCustomer() {
        String[] customerData = TestDataGenerator.generateValidCustomerData();
        String firstName = customerData[0];
        String lastName = customerData[1];
        String postalCode = customerData[2];
        customerFullName = TestDataGenerator.generateFullName(firstName, lastName);

        ManagerPage managerPage = homePage.clickBankManagerLogin();
        managerPage.addCustomer(firstName, lastName, postalCode);
        managerPage.openAccount(customerFullName, "Dollar");
        homePage.clickHome();

        LoggerUtil.info(CustomerTests.class, "Test customer created: " + customerFullName);
    }

    @Test(priority = 1, description = "Verify customer can login successfully")
    @Story("Customer Login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Test to verify that a customer can successfully login to their account")
    public void testCustomerLogin() {
        LoggerUtil.info(CustomerTests.class, "Starting test: Customer Login");

        CustomerLoginPage loginPage = homePage.clickCustomerLogin();

        boolean isCustomerAvailable = loginPage.isCustomerAvailable(customerFullName);
        Assert.assertTrue(isCustomerAvailable,
                "Customer should be available in dropdown: " + customerFullName);

        AccountPage accountPage = loginPage.loginAsCustomer(customerFullName);

        int balance = accountPage.getBalance();
        Assert.assertEquals(balance, 0, "Initial balance should be 0");

        LoggerUtil.info(CustomerTests.class, "Test completed: Customer login successful");
    }

    @Test(priority = 2, description = "Verify customer can deposit funds")
    @Story("Deposit Funds")
    @Severity(SeverityLevel.CRITICAL)
    public void testDepositFunds() {
        LoggerUtil.info(CustomerTests.class, "Starting test: Deposit Funds");

        CustomerLoginPage loginPage = homePage.clickCustomerLogin();
        AccountPage accountPage = loginPage.loginAsCustomer(customerFullName);

        int initialBalance = accountPage.getBalance();
        int depositAmount = TestDataGenerator.generateDepositAmount();

        accountPage.deposit(depositAmount);

        boolean isSuccessful = accountPage.isTransactionSuccessful();
        Assert.assertTrue(isSuccessful, "Deposit transaction should be successful");

        int finalBalance = accountPage.getBalance();
        int expectedBalance = initialBalance + depositAmount;

        Assert.assertEquals(finalBalance, expectedBalance,
                "Balance should increase by deposit amount");

        LoggerUtil.info(CustomerTests.class, "Test completed: Deposit successful");
    }

    @Test(priority = 3, description = "Verify customer can withdraw funds with sufficient balance")
    @Story("Withdraw Funds")
    @Severity(SeverityLevel.CRITICAL)
    public void testWithdrawFundsWithSufficientBalance() {
        LoggerUtil.info(CustomerTests.class, "Starting test: Withdraw with Sufficient Balance");

        CustomerLoginPage loginPage = homePage.clickCustomerLogin();
        AccountPage accountPage = loginPage.loginAsCustomer(customerFullName);

        int depositAmount = 1000;
        accountPage.deposit(depositAmount);

        int balanceAfterDeposit = accountPage.getBalance();
        int withdrawalAmount = TestDataGenerator.generateWithdrawalAmount(depositAmount);

        accountPage.withdraw(withdrawalAmount);

        boolean isSuccessful = accountPage.isTransactionSuccessful();
        Assert.assertTrue(isSuccessful, "Withdrawal transaction should be successful");

        int finalBalance = accountPage.getBalance();
        int expectedBalance = balanceAfterDeposit - withdrawalAmount;

        Assert.assertEquals(finalBalance, expectedBalance,
                "Balance should decrease by withdrawal amount");

        LoggerUtil.info(CustomerTests.class, "Test completed: Withdrawal successful");
    }

    @Test(priority = 4, description = "Verify withdrawal fails with insufficient balance")
    @Story("Withdraw Funds")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to verify that withdrawal fails when balance is insufficient")
    public void testWithdrawFundsWithInsufficientBalance() {
        LoggerUtil.info(CustomerTests.class, "Starting test: Withdraw with Insufficient Balance");

        CustomerLoginPage loginPage = homePage.clickCustomerLogin();
        AccountPage accountPage = loginPage.loginAsCustomer(customerFullName);

        int depositAmount = 100;
        accountPage.deposit(depositAmount);

        int balanceBeforeWithdrawal = accountPage.getBalance();
        int withdrawalAmount = depositAmount + 500;

        accountPage.withdraw(withdrawalAmount);

        boolean isWithdrawalFailed = accountPage.isWithdrawalFailed();
        Assert.assertTrue(isWithdrawalFailed, "Withdrawal should fail with insufficient balance");

        int balanceAfterWithdrawal = accountPage.getBalance();
        Assert.assertEquals(balanceAfterWithdrawal, balanceBeforeWithdrawal,
                "Balance should remain unchanged after failed withdrawal");

        LoggerUtil.info(CustomerTests.class, "Test completed: Withdrawal failed as expected");
    }

    @Test(priority = 5, description = "Verify customer can view transaction history")
    @Story("View Transactions")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test to verify that a customer can view their transaction history")
    public void testViewTransactionHistory() {
        LoggerUtil.info(CustomerTests.class, "Starting test: View Transaction History");

        CustomerLoginPage loginPage = homePage.clickCustomerLogin();
        AccountPage accountPage = loginPage.loginAsCustomer(customerFullName);

        int depositAmount = 500;
        accountPage.deposit(depositAmount);

        int withdrawalAmount = 200;
        accountPage.withdraw(withdrawalAmount);

        int transactionCount = accountPage.getTransactionCount();

        Assert.assertEquals(transactionCount, 2,
                "Transaction history should show 2 transactions (1 deposit + 1 withdrawal)");

        LoggerUtil.info(CustomerTests.class, "Test completed: Transaction history verified");
    }

    @Test(priority = 6, description = "Verify deposit with zero amount is rejected")
    @Story("Deposit Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test to verify that deposit with zero or negative amount is rejected")
    public void testDepositWithInvalidAmount() {
        LoggerUtil.info(CustomerTests.class, "Starting test: Deposit with Invalid Amount");

        CustomerLoginPage loginPage = homePage.clickCustomerLogin();
        AccountPage accountPage = loginPage.loginAsCustomer(customerFullName);

        int initialBalance = accountPage.getBalance();

        accountPage.deposit(0);

        int finalBalance = accountPage.getBalance();

        Assert.assertEquals(finalBalance, initialBalance,
                "Balance should not change when depositing zero amount");

        LoggerUtil.info(CustomerTests.class, "Test completed: Invalid deposit rejected");
    }

    @Test(priority = 7, description = "Verify multiple deposits update balance correctly")
    @Story("Deposit Funds")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test to verify that multiple deposits correctly update the account balance")
    public void testMultipleDeposits() {
        LoggerUtil.info(CustomerTests.class, "Starting test: Multiple Deposits");

        CustomerLoginPage loginPage = homePage.clickCustomerLogin();
        AccountPage accountPage = loginPage.loginAsCustomer(customerFullName);

        int deposit1 = 300;
        int deposit2 = 500;
        int deposit3 = 200;

        accountPage.deposit(deposit1);
        accountPage.deposit(deposit2);
        accountPage.deposit(deposit3);

        int finalBalance = accountPage.getBalance();
        int expectedBalance = deposit1 + deposit2 + deposit3;

        Assert.assertEquals(finalBalance, expectedBalance,
                "Balance should equal sum of all deposits");

        int transactionCount = accountPage.getTransactionCount();
        Assert.assertEquals(transactionCount, 3, "Should have 3 deposit transactions");

        LoggerUtil.info(CustomerTests.class, "Test completed: Multiple deposits successful");
    }

    @Test(priority = 9, enabled = false, description = "SECURITY BUG: Verify customer cannot reset transaction history")
    @Story("Transaction Security")
    @Severity(SeverityLevel.CRITICAL)
    @Description("CRITICAL BUG: Customer can reset transaction history which violates audit trail integrity and financial regulations")
    public void testCustomerCannotResetTransactions() {
        LoggerUtil.info(CustomerTests.class, "Starting test: Customer Cannot Reset Transactions");

        CustomerLoginPage loginPage = homePage.clickCustomerLogin();
        AccountPage accountPage = loginPage.loginAsCustomer(customerFullName);

        accountPage.deposit(500);
        accountPage.withdraw(200);

        int transactionCountBefore = accountPage.getTransactionCount();
        Assert.assertTrue(transactionCountBefore > 0,
                "Should have transactions before attempting reset");

        LoggerUtil.info(CustomerTests.class, "Transaction count before reset: " + transactionCountBefore);

        accountPage.resetTransactions();

        int transactionCountAfter = accountPage.getTransactionCount();
        LoggerUtil.error(CustomerTests.class,
                "CRITICAL SECURITY BUG: Customer was able to reset transactions! " +
                        "Before: " + transactionCountBefore + ", After: " + transactionCountAfter);

        Assert.assertEquals(transactionCountAfter, transactionCountBefore,
                "CRITICAL BUG: Customer should NOT be able to reset transaction history. " +
                        "Transaction count changed from " + transactionCountBefore + " to " + transactionCountAfter);

        LoggerUtil.info(CustomerTests.class, "Test completed: Security bug documented");
    }
}
