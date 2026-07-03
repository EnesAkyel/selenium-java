package SeleniumWebAutomation.pages;

import SeleniumWebAutomation.common.BasePage;
import SeleniumWebAutomation.config.ConfigReader;
import SeleniumWebAutomation.driver.DriverManager;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PimPage extends BasePage {
    @FindBy(css = ".oxd-table-body .oxd-table-row")
    private WebElement firstTableRow;

    @FindBy(xpath = "//span[contains(., 'Record') and contains(., 'Found')]")
    private WebElement recordFound;

    @FindBy(css = "button[type='submit']")
    private WebElement submitButton;

    @FindBy(xpath = "//label[normalize-space()='Employee Id']/ancestor::div[contains(@class,'oxd-input-group')]//input")
    private WebElement employeeIdInput;

    @FindBy(css = "input[placeholder='First Name']")
    private WebElement firstNameInput;

    @FindBy(css = "input[placeholder='Last Name']")
    private WebElement lastNameInput;

    public PimPage() { super(DriverManager.getDriver()); }

    @Step("Wait for employee table to load")
    public PimPage waitForTableToLoad() {
        waitForVisible(firstTableRow);
        return this;
    }

    public int getRecordCount() {
        scrollToElement(recordFound);
        return Integer.parseInt(waitForVisible(recordFound).getText().replaceAll("[^0-9]", ""));
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
        submitButton.click();
    }
}
