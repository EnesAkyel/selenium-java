package SeleniumWebAutomation.pages;

import SeleniumWebAutomation.common.BasePage;
import SeleniumWebAutomation.driver.DriverManager;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LeavePage extends BasePage {
    @FindBy(css = ".oxd-topbar-header-breadcrumb h6")
    private WebElement pageTitle;

    public LeavePage() { super(DriverManager.getDriver()); }

    public boolean isLoaded() {
        waitForVisible(pageTitle);
        return driver.getCurrentUrl().contains("/leave/");
    }
}
