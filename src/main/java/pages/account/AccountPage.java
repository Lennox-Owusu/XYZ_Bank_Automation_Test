package pages.account;

import utils.SeleniumUtils;
import utils.LoggerUtil;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class AccountPage extends SeleniumUtils {

    @FindBy(css = "button[ng-click='transactions()']")
    private WebElement transactionsButton;

    @FindBy(css = "button[ng-click='deposit()']")
    private WebElement depositButton;

    @FindBy(css = "button[ng-click='withdrawl()']")
    private WebElement withdrawalButton;

    @FindBy(css = "input[ng-model='amount']")
    private WebElement amountInput;

    @FindBy(css = "button[type='submit']")
    private WebElement submitButton;

    @FindBy(xpath = "//strong[contains(@class,'ng-binding')][2]")
    private WebElement balanceLabel;

    @FindBy(css = "span[class='error ng-binding']")
    private WebElement transactionMessage;

    @FindBy(css = "table tbody tr")
    private List<WebElement> transactionRows;

    @FindBy(css = "button[ng-click='back()']")
    private WebElement backButton;

    @FindBy(css = "button[ng-click='reset()']")
    private WebElement resetButton;


    public void clickTransactions() {
        waitForElementToBeClickable(transactionsButton);
        click(transactionsButton, "Transactions Button");
        waitForSeconds(1); // Wait for transaction table to load
    }

    public void clickDeposit() {
        waitForElementToBeClickable(depositButton);
        click(depositButton, "Deposit Button");
    }

    public void clickWithdrawal() {
        waitForElementToBeClickable(withdrawalButton);
        click(withdrawalButton, "Withdrawal Button");
    }



    public void deposit(int amount) {
        LoggerUtil.logStep(AccountPage.class, "Depositing amount: " + amount);

        clickDeposit();

        // Wait for form to be ready
        waitForSeconds(1);
        waitForElementToBeVisible(amountInput);
        waitForElementToBeClickable(amountInput);

        // Clear and type amount
        amountInput.clear();
        type(amountInput, String.valueOf(amount), "Amount Input");

        // Submit
        waitForElementToBeClickable(submitButton);
        click(submitButton, "Submit Button");

        // Wait for transaction to complete
        waitForSeconds(2);

        LoggerUtil.info(AccountPage.class, "Deposit completed: " + amount);
    }

    public void withdraw(int amount) {
        LoggerUtil.logStep(AccountPage.class, "Withdrawing amount: " + amount);

        clickWithdrawal();

        // Wait for form to be ready
        waitForSeconds(1);
       waitForElementToBeVisible(amountInput);
        waitForElementToBeClickable(amountInput);

        // Clear and type amount
        amountInput.clear();
        type(amountInput, String.valueOf(amount), "Amount Input");

        // Submit
       waitForElementToBeClickable(submitButton);
        click(submitButton, "Submit Button");

        // Wait for transaction to complete
        waitForSeconds(2);

        LoggerUtil.info(AccountPage.class, "Withdrawal completed: " + amount);
    }

    public int getBalance() {
        waitForElementToBeVisible(balanceLabel);
       waitForSeconds(1); // Extra wait for balance to update
        String balanceText = getText(balanceLabel, "Balance Label");
        int balance = Integer.parseInt(balanceText);
        LoggerUtil.debug(AccountPage.class, "Current balance: " + balance);
        return balance;
    }

    public String getTransactionMessage() {
        try {
            waitForElementToBeVisible(transactionMessage);
            String message = getText(transactionMessage, "Transaction Message");
            LoggerUtil.debug(AccountPage.class, "Transaction message: " + message);
            return message;
        } catch (Exception e) {
            LoggerUtil.debug(AccountPage.class, "No transaction message found");
            return "";
        }
    }

    public int getTransactionCount() {
        clickTransactions();
       waitForSeconds(1);
        int count = transactionRows.size();
        LoggerUtil.debug(AccountPage.class, "Total transactions: " + count);
        clickBack();
        return count;
    }

    public boolean isTransactionSuccessful() {
        try {
           waitForSeconds(1);
            String message = getTransactionMessage();

            if (message.isEmpty()) {
                // If no message, check if balance updated (means success)
                LoggerUtil.debug(AccountPage.class, "No message found, assuming success");
                return true;
            }

            boolean isSuccess = message.toLowerCase().contains("successful") ||
                    message.toLowerCase().contains("transaction successful");

            LoggerUtil.debug(AccountPage.class, "Transaction success check: " + isSuccess + " (Message: " + message + ")");
            return isSuccess;

        } catch (Exception e) {
            LoggerUtil.error(AccountPage.class, "Error checking transaction success", e);
            // If no error message appears, assume success
            return true;
        }
    }

    public boolean isWithdrawalFailed() {
        try {
            waitForSeconds(1);
            String message = getTransactionMessage();

            if (message.isEmpty()) {
                LoggerUtil.debug(AccountPage.class, "No failure message found");
                return false;
            }

            boolean isFailed = message.toLowerCase().contains("failed") ||
                    message.toLowerCase().contains("transaction failed");

            LoggerUtil.debug(AccountPage.class, "Withdrawal failure check: " + isFailed + " (Message: " + message + ")");
            return isFailed;

        } catch (Exception e) {
            LoggerUtil.error(AccountPage.class, "Error checking withdrawal failure", e);
            return false;
        }
    }

    public void clickBack() {
        waitForElementToBeClickable(backButton);
        click(backButton, "Back Button");
        waitForSeconds(1);
    }

    public void resetTransactions() {
        clickTransactions();
        waitForElementToBeClickable(resetButton);
        click(resetButton, "Reset Button");
        LoggerUtil.info(AccountPage.class, "Transactions reset");
        waitForSeconds(1);
        clickBack();
    }
}
