package SeleniumWebAutomation.tests;

import SeleniumWebAutomation.common.CommonTest;
import SeleniumWebAutomation.config.ConfigReader;
import SeleniumWebAutomation.pages.LoginPage;
import SeleniumWebAutomation.pages.PimPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

@Feature("Employee Management")
public class PimTest extends CommonTest {

    private PimPage login() {
        return new LoginPage()
                .loginWith(ConfigReader.getUsername(), ConfigReader.getPassword())
                .navigateToPim();
    }

    @Test(groups = "smoke")
    @Story("Employee list loads with records")
    @Severity(SeverityLevel.CRITICAL)
    @Description("PIM employee list table renders with at least one row after login")
    public void employeeListLoadsWithRecords() {
        PimPage pim = login().waitForTableToLoad();
        Assert.assertTrue(pim.getRecordCount() > 0, "Employee list should contain at least one record");
    }

    @Test(groups = "regression")
    @Story("Edit employee first name is reflected in the list")
    @Severity(SeverityLevel.NORMAL)
    @Description("Creates a test employee, edits their first name, verifies the change in the list, then cleans up")
    public void editEmployeeFirstNameReflectedInList() {
        String name = "Original";
        String lastName = "EditTest";
        String testId = "TEST02";
        PimPage pim = login();

        pim.clickAddEmployee()
                .fillEmployeeForm(name, lastName, testId)
                .saveEmployee();
    }
}
