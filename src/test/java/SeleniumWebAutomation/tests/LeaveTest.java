package SeleniumWebAutomation.tests;

import SeleniumWebAutomation.common.CommonTest;
import SeleniumWebAutomation.config.ConfigReader;
import SeleniumWebAutomation.pages.LeavePage;
import SeleniumWebAutomation.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

@Feature("Leave Management")
public class LeaveTest extends CommonTest {

    private LeavePage login() {
        return new LoginPage()
                .loginWith(ConfigReader.getUsername(), ConfigReader.getPassword())
                .navigateToLeave();
    }

    @Test(groups = "smoke")
    @Story("Leave module loads")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Leave module is accessible after login and the URL reflects the leave section")
    public void leaveModuleLoads() {
        LeavePage leavePage = login();
        Assert.assertTrue(leavePage.isLoaded(), "Expected leave module URL after navigation");
    }

    @Test(groups = "regression")
    @Story("Leave types list is populated")
    @Severity(SeverityLevel.NORMAL)
    @Description("Leave types configuration page shows at least one leave type entry in the table")
    public void leaveTypesListIsPopulated() {
        LeavePage leavePage = login().navigateToLeaveTypes().waitForTableToLoad();
        int count = leavePage.getLeaveTypeCount();
        Assert.assertTrue(count > 0, "Leave types table should have at least one entry, found: " + count);
    }
}
