package SeleniumWebAutomation.pages;

import SeleniumWebAutomation.common.BasePage;
import SeleniumWebAutomation.components.TableComponent;
import SeleniumWebAutomation.config.ConfigReader;
import SeleniumWebAutomation.driver.DriverManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LeavePage extends BasePage {
    private static final By TABLE_ROW = By.cssSelector(".oxd-table-body .oxd-table-row");

    @FindBy(css = ".oxd-topbar-header-breadcrumb h6")
    private WebElement pageTitle;

    @FindBy(css = ".oxd-table-body .oxd-table-row")
    private WebElement firstTableRow;

    public LeavePage() { super(DriverManager.getDriver()); }

    public boolean isLoaded() {
        waitForVisible(pageTitle);
        return driver.getCurrentUrl().contains("/leave/");
    }

    @Step("Navigate to Leave Types")
    public LeavePage navigateToLeaveTypes() {
        driver.get(ConfigReader.getBaseUrl() + "/web/index.php/leave/viewLeaveTypeList");
        return this;
    }

    @Step("Wait for leave table to load")
    public LeavePage waitForTableToLoad() {
        waitForVisible(firstTableRow);
        return this;
    }

    public TableComponent getLeaveTypeTable() {
        return new TableComponent(driver, TABLE_ROW);
    }

    public int getLeaveTypeCount() {
        return getLeaveTypeTable().getRowCount();
    }
}
