package SeleniumWebAutomation.components;

import SeleniumWebAutomation.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public class TableComponent {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By rowLocator;

    public TableComponent(WebDriver driver, By rowLocator) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
        this.rowLocator = rowLocator;
    }

    public int getRowCount() {
        return getRows().size();
    }

    public List<WebElement> getRows() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(rowLocator));
        return driver.findElements(rowLocator);
    }

    public Optional<WebElement> findRowContaining(String text) {
        return getRows().stream()
                .filter(row -> row.getText().contains(text))
                .findFirst();
    }

    public boolean containsRow(String text) {
        return findRowContaining(text).isPresent();
    }
}
