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
import org.testng.annotations.DataProvider;
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

    // Employee IDs 0001–0003 are present in the standard OrangeHRM demo dataset
    @DataProvider(name = "employeeIds")
    public Object[][] employeeIds() {
        return new Object[][] {
                {"0001"},
                {"0002"},
                {"0003"},
        };
    }

    @Test(groups = "regression", dataProvider = "employeeIds")
    @Story("Search by employee ID returns exactly one result")
    @Severity(SeverityLevel.NORMAL)
    @Description("Searching by a known employee ID narrows the table to a single matching row")
    public void searchByEmployeeIdReturnsOneRecord(String employeeId) {
        PimPage pim = login();
        pim.searchByEmployeeId(employeeId);
        Assert.assertEquals(pim.getRecordCount(), 1,
                "Search by ID " + employeeId + " should return exactly one employee");
    }

    @Test(groups = "regression")
    @Story("Edit employee first name is reflected in the list")
    @Severity(SeverityLevel.NORMAL)
    @Description("Creates a test employee, edits their first name, verifies the change in the list, then cleans up")
    public void editEmployeeFirstNameReflectedInList() {
        String testId = "TEST02";
        String updatedName = "Updated";
        PimPage pim = login();

        pim.clickAddEmployee()
                .fillEmployeeForm("Original", "EditTest", testId)
                .saveEmployee();

        pim.navigateToList()
                .clickEditForEmployee(testId)
                .updateFirstName(updatedName)
                .savePersonalDetails();

        pim.navigateToList().searchByEmployeeId(testId);
        Assert.assertTrue(pim.getEmployeeTable().containsRow(updatedName),
                "Updated first name '" + updatedName + "' should appear in the employee list");

        pim.deleteEmployeeById(testId);
    }

    @Test(groups = "regression")
    @Story("Add new employee then delete")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Creates a test employee, verifies it appears in search, then deletes it — verifying the full CRUD lifecycle")
    public void addAndDeleteEmployee() {
        String testId = "TEST01";
        PimPage pim = login();

        pim.clickAddEmployee()
                .fillEmployeeForm("Automation", "Test", testId)
                .saveEmployee();

        pim.navigateToList()
                .searchByEmployeeId(testId);
        Assert.assertEquals(pim.getRecordCount(), 1,
                "Newly added employee should appear in search results");

        pim.deleteEmployeeById(testId)
                .searchByEmployeeId(testId);
        Assert.assertEquals(pim.getRecordCount(), 0,
                "Deleted employee should no longer appear in search results");
    }
}
