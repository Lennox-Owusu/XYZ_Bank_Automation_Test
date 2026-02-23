package pages;

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

    // FIXED: Remove the parameter from constructor
    public AccountPage() {
        super();
    }

    public void clickTransactions() {
        click(transactionsButton, "Transactions Button");
    }

    public void clickDeposit() {
        click(depositButton, "Deposit Button");
    }

    public void clickWithdrawal() {
        click(withdrawalButton, "Withdrawal Button");
    }

    public void logout() {
        click(logoutButton, "Logout Button");
        LoggerUtil.info(AccountPage.class, "User logged out successfully");
    }

    public void deposit(int amount) {
        LoggerUtil.logStep(AccountPage.class, "Depositing amount: " + amount);

        clickDeposit();
        type(amountInput, String.valueOf(amount), "Amount Input");
        click(submitButton, "Submit Button");

        waitHelper.waitForSeconds(1);
        LoggerUtil.info(AccountPage.class, "Deposit completed: " + amount);
    }

    public void withdraw(int amount) {
        LoggerUtil.logStep(AccountPage.class, "Withdrawing amount: " + amount);

        clickWithdrawal();
        type(amountInput, String.valueOf(amount), "Amount Input");
        click(submitButton, "Submit Button");

        waitHelper.waitForSeconds(1);
        LoggerUtil.info(AccountPage.class, "Withdrawal completed: " + amount);
    }

    public int getBalance() {
        String balanceText = getText(balanceLabel, "Balance Label");
        int balance = Integer.parseInt(balanceText);
        LoggerUtil.debug(AccountPage.class, "Current balance: " + balance);
        return balance;
    }

    public String getTransactionMessage() {
        String message = getText(transactionMessage, "Transaction Message");
        LoggerUtil.debug(AccountPage.class, "Transaction message: " + message);
        return message;
    }

    public int getTransactionCount() {
        clickTransactions();
        int count = transactionRows.size();
        LoggerUtil.debug(AccountPage.class, "Total transactions: " + count);
        clickBack();
        return count;
    }

    public boolean isTransactionSuccessful() {
        try {
            String message = getTransactionMessage();
            return message.contains("successful") || message.contains("Transaction successful");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isWithdrawalFailed() {
        try {
            String message = getTransactionMessage();
            return message.contains("failed") || message.contains("Transaction Failed");
        } catch (Exception e) {
            return false;
        }
    }

    public void clickBack() {
        click(backButton, "Back Button");
    }

    public void resetTransactions() {
        clickTransactions();
        click(resetButton, "Reset Button");
        LoggerUtil.info(AccountPage.class, "Transactions reset");
        clickBack();
    }
}
