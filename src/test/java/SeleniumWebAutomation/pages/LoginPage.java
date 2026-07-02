package SeleniumWebAutomation.pages;

import SeleniumWebAutomation.common.BasePage;
import SeleniumWebAutomation.driver.DriverManager;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class LoginPage extends BasePage {

    @FindBy(name = "username")
    private WebElement usernameInput;

    @FindBy(name = "password")
    private WebElement passwordInput;

    @FindBy(css = "button[type='submit']")
    private WebElement loginButton;

    @FindBy(css = ".oxd-alert-content-text")
    private WebElement errorAlert;

    @FindBy(css = ".oxd-input-field-error-message")
    private List<WebElement> fieldErrors;

    public LoginPage() { super(DriverManager.getDriver()); }

    @Step("Enter username: {username}")
    public LoginPage enterUsername(String username) {
        waitForVisible(usernameInput);
        usernameInput.clear();
        usernameInput.sendKeys(username);
        return this;
    }

    @Step("Enter password")
    public void enterPassword(String password) {
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    @Step("Login with credentials")
    public DashboardPage loginWith(String username, String password) {
        enterUsername(username).enterPassword(password);
        loginButton.click();
        return new DashboardPage();
    }

    @Step("Attempt login (expected to fail)")
    public LoginPage attemptLoginWith(String username, String password) {
        enterUsername(username).enterPassword(password);
        loginButton.click();
        return this;
    }

    public String getErrorText() {
        return waitForVisible(errorAlert).getText();
    }

    public List<WebElement> getFieldErrors() {
        waitForVisible(fieldErrors.get(0));
        return fieldErrors;
    }
}
