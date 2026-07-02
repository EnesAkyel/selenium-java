package SeleniumWebAutomation.tests;

import SeleniumWebAutomation.common.CommonTest;
import SeleniumWebAutomation.config.ConfigReader;
import SeleniumWebAutomation.pages.DashboardPage;
import SeleniumWebAutomation.pages.LoginPage;
import SeleniumWebAutomation.utils.Constants;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

@Feature("Authentication")
public class LoginTest extends CommonTest {

    @Test(groups = "smoke")
    @Story("Valid credentials reach the dashboard")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Admin user with valid credentials is redirected to the dashboard after login")
    public void validCredentialsReachDashboard() {
        DashboardPage dashboard = new LoginPage()
                .loginWith(ConfigReader.getUsername(), ConfigReader.getPassword());

        Assert.assertTrue(dashboard.isLoaded(), "Expected dashboard URL after login");
        Assert.assertEquals(dashboard.getPageTitle(), Constants.DASHBOARD_TITLE);
    }

    @Test(groups = "regression")
    @Story("Invalid password shows alert, no redirect")
    @Severity(SeverityLevel.NORMAL)
    @Description("Wrong password displays an inline error and keeps the user on the login page")
    public void invalidPasswordShowsErrorAlert() {
        String error = new LoginPage()
                .attemptLoginWith(ConfigReader.getUsername(), "wrongPassword!")
                .getErrorText();

        Assert.assertEquals(error, Constants.INVALID_CREDENTIALS_ERROR);
    }

    @Test(groups = "regression")
    @Story("Empty fields trigger required field validation")
    @Severity(SeverityLevel.MINOR)
    @Description("Submitting the login form with both fields empty shows a required error under each field")
    public void emptyFieldsShowRequiredValidation() {
        LoginPage loginPage = new LoginPage().attemptLoginWith("", "");
        List<org.openqa.selenium.WebElement> errors = loginPage.getFieldErrors();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(errors.size() >= 2,
                "Expected at least 2 required field errors, found: " + errors.size());
        errors.forEach(e -> softAssert.assertEquals(e.getText(), Constants.REQUIRED_FIELD_ERROR,
                "Field error text mismatch"));
        softAssert.assertAll();
    }
}
