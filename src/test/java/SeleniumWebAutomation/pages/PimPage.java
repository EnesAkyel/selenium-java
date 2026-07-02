package SeleniumWebAutomation.pages;

import SeleniumWebAutomation.common.BasePage;
import SeleniumWebAutomation.components.TableComponent;
import SeleniumWebAutomation.config.ConfigReader;
import SeleniumWebAutomation.driver.DriverManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PimPage extends BasePage {
    private static final By TABLE_ROW = By.cssSelector(".oxd-table-body .oxd-table-row");

    @FindBy(css = ".oxd-table-body .oxd-table-row")
    private WebElement firstTableRow;

    @FindBy(xpath = "//*[contains(text(),'Records Found')]")
    private WebElement recordsFound;

    @FindBy(css = "button[type='submit']")
    private WebElement submitButton;

    @FindBy(xpath = "//label[normalize-space()='Employee Id']/ancestor::div[contains(@class,'oxd-input-group')]//input")
    private WebElement employeeIdInput;

    @FindBy(css = "input[placeholder='First Name']")
    private WebElement firstNameInput;

    @FindBy(css = "input[placeholder='Last Name']")
    private WebElement lastNameInput;

    @FindBy(css = "button:has(i.bi-pencil-fill)")
    private WebElement editButton;

    @FindBy(css = "button:has(i.bi-trash)")
    private WebElement deleteButton;

    @FindBy(xpath = "//button[normalize-space()='Yes, Delete']")
    private WebElement deleteConfirmButton;

    @FindBy(css = ".oxd-toast--success")
    private WebElement successToast;

    public PimPage() { super(DriverManager.getDriver()); }

    public TableComponent getEmployeeTable() {
        return new TableComponent(driver, TABLE_ROW);
    }

    @Step("Wait for employee table to load")
    public PimPage waitForTableToLoad() {
        waitForVisible(firstTableRow);
        return this;
    }

    public int getRecordCount() {
        return Integer.parseInt(waitForVisible(recordsFound).getText().replaceAll("[^0-9]", ""));
    }

    @Step("Search by employee ID: {employeeId}")
    public void searchByEmployeeId(String employeeId) {
        waitForVisible(employeeIdInput).clear();
        employeeIdInput.sendKeys(employeeId);
        waitForClickable(submitButton).click();
        waitForVisible(recordsFound);
    }

    @Step("Navigate to employee list")
    public PimPage navigateToList() {
        driver.get(ConfigReader.getBaseUrl() + "/web/index.php/pim/viewEmployeeList");
        return this;
    }

    @Step("Open Add Employee form")
    public PimPage clickAddEmployee() {
        driver.get(ConfigReader.getBaseUrl() + "/web/index.php/pim/addEmployee");
        return this;
    }

    @Step("Fill employee form: {firstName} {lastName}, ID: {employeeId}")
    public PimPage fillEmployeeForm(String firstName, String lastName, String employeeId) {
        waitForVisible(firstNameInput).clear();
        firstNameInput.sendKeys(firstName);
        lastNameInput.clear();
        lastNameInput.sendKeys(lastName);
        waitForVisible(employeeIdInput).clear();
        employeeIdInput.sendKeys(employeeId);
        return this;
    }

    @Step("Save employee")
    public void saveEmployee() {
        waitForClickable(submitButton).click();
    }

    @Step("Click edit for employee with ID: {employeeId}")
    public PimPage clickEditForEmployee(String employeeId) {
        searchByEmployeeId(employeeId);
        waitForClickable(editButton).click();
        return this;
    }

    @Step("Update first name to: {firstName}")
    public PimPage updateFirstName(String firstName) {
        waitForVisible(firstNameInput).clear();
        firstNameInput.sendKeys(firstName);
        return this;
    }

    @Step("Save personal details")
    public void savePersonalDetails() {
        waitForClickable(submitButton).click();
        waitForVisible(successToast);
    }

    @Step("Delete employee with ID: {employeeId}")
    public PimPage deleteEmployeeById(String employeeId) {
        searchByEmployeeId(employeeId);
        waitForClickable(deleteButton).click();
        waitForClickable(deleteConfirmButton).click();
        return this;
    }
}
