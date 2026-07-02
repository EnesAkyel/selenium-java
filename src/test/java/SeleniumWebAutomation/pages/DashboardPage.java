package SeleniumWebAutomation.pages;

import SeleniumWebAutomation.common.BasePage;
import SeleniumWebAutomation.config.ConfigReader;
import SeleniumWebAutomation.driver.DriverManager;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DashboardPage extends BasePage {

    @FindBy(css = ".oxd-topbar-header-breadcrumb h6")
    private WebElement pageTitle;

    public DashboardPage() { super(DriverManager.getDriver()); }

    public boolean isLoaded() {
        waitForVisible(pageTitle);
        return driver.getCurrentUrl().contains("/dashboard/index");
    }

    public String getPageTitle() {
        return waitForVisible(pageTitle).getText();
    }

    @Step("Navigate to PIM")
    public PimPage navigateToPim() {
        driver.get(ConfigReader.getBaseUrl() + "/web/index.php/pim/viewEmployeeList");
        return new PimPage();
    }

    @Step("Navigate to Leave")
    public LeavePage navigateToLeave() {
        driver.get(ConfigReader.getBaseUrl() + "/web/index.php/leave/viewLeaveList");
        return new LeavePage();
    }
}
