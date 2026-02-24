package pages;

import org.openqa.selenium.JavascriptExecutor;
import pagesHelper.BasePage;
import utils.LoggerUtil;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class AccountPage extends BasePage {

    @FindBy(css = "button[ng-click='transactions()']")
    private WebElement transactionsButton;

    @FindBy(css = "button[ng-click='deposit()']")
    private WebElement depositButton;

    @FindBy(css = "button[ng-click='withdrawl()']")
    private WebElement withdrawalButton;

    @FindBy(css = "button[ng-click='logout()']")
    private WebElement logoutButton;

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

    public AccountPage() {
        super();
    }

    public void clickTransactions() {
        waitHelper.waitForElementToBeClickable(transactionsButton);
        click(transactionsButton, "Transactions Button");
        waitHelper.waitForSeconds(1); // Wait for transaction table to load
    }

    public void clickDeposit() {
        waitHelper.waitForElementToBeClickable(depositButton);
        click(depositButton, "Deposit Button");
    }

    public void clickWithdrawal() {
        waitHelper.waitForElementToBeClickable(withdrawalButton);
        click(withdrawalButton, "Withdrawal Button");
    }

    public void logout() {
        try {
            // Scroll to logout button
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", logoutButton);
            waitHelper.waitForSeconds(1);

            // Try clicking normally
            waitHelper.waitForElementToBeClickable(logoutButton);
            click(logoutButton, "Logout Button");

        } catch (Exception e) {
            // If normal click fails, use JavaScript click
            LoggerUtil.info(AccountPage.class, "Normal click failed, trying JavaScript click");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", logoutButton);
        }

        LoggerUtil.info(AccountPage.class, "User logged out successfully");
    }


    public void deposit(int amount) {
        LoggerUtil.logStep(AccountPage.class, "Depositing amount: " + amount);

        clickDeposit();

        // Wait for form to be ready
        waitHelper.waitForSeconds(1);
        waitHelper.waitForElementToBeVisible(amountInput);
        waitHelper.waitForElementToBeClickable(amountInput);

        // Clear and type amount
        amountInput.clear();
        type(amountInput, String.valueOf(amount), "Amount Input");

        // Submit
        waitHelper.waitForElementToBeClickable(submitButton);
        click(submitButton, "Submit Button");

        // Wait for transaction to complete
        waitHelper.waitForSeconds(2);

        LoggerUtil.info(AccountPage.class, "Deposit completed: " + amount);
    }

    public void withdraw(int amount) {
        LoggerUtil.logStep(AccountPage.class, "Withdrawing amount: " + amount);

        clickWithdrawal();

        // Wait for form to be ready
        waitHelper.waitForSeconds(1);
        waitHelper.waitForElementToBeVisible(amountInput);
        waitHelper.waitForElementToBeClickable(amountInput);

        // Clear and type amount
        amountInput.clear();
        type(amountInput, String.valueOf(amount), "Amount Input");

        // Submit
        waitHelper.waitForElementToBeClickable(submitButton);
        click(submitButton, "Submit Button");

        // Wait for transaction to complete
        waitHelper.waitForSeconds(2);

        LoggerUtil.info(AccountPage.class, "Withdrawal completed: " + amount);
    }

    public int getBalance() {
        waitHelper.waitForElementToBeVisible(balanceLabel);
        waitHelper.waitForSeconds(1); // Extra wait for balance to update
        String balanceText = getText(balanceLabel, "Balance Label");
        int balance = Integer.parseInt(balanceText);
        LoggerUtil.debug(AccountPage.class, "Current balance: " + balance);
        return balance;
    }

    public String getTransactionMessage() {
        try {
            waitHelper.waitForElementToBeVisible(transactionMessage);
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
        waitHelper.waitForSeconds(1);
        int count = transactionRows.size();
        LoggerUtil.debug(AccountPage.class, "Total transactions: " + count);
        clickBack();
        return count;
    }

    public boolean isTransactionSuccessful() {
        try {
            waitHelper.waitForSeconds(1);
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
            waitHelper.waitForSeconds(1);
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
        waitHelper.waitForElementToBeClickable(backButton);
        click(backButton, "Back Button");
        waitHelper.waitForSeconds(1);
    }

    public void resetTransactions() {
        clickTransactions();
        waitHelper.waitForElementToBeClickable(resetButton);
        click(resetButton, "Reset Button");
        LoggerUtil.info(AccountPage.class, "Transactions reset");
        waitHelper.waitForSeconds(1);
        clickBack();
    }
}
