package SeleniumWebAutomation.common;

import SeleniumWebAutomation.config.BrowserFactory;
import SeleniumWebAutomation.config.ConfigReader;
import SeleniumWebAutomation.driver.DriverManager;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;

public abstract class CommonTest {

    @BeforeSuite(alwaysRun = true)
    public void writeEnvironmentProperties() throws IOException {
        Properties env = new Properties();
        env.setProperty("Target URL", ConfigReader.getBaseUrl());
        env.setProperty("Browser", ConfigReader.getBrowser());
        env.setProperty("Java Version", System.getProperty("java.version"));
        env.setProperty("Selenium Version", "4.45.0");

        Path resultsDir = Paths.get("target/allure-results");
        Files.createDirectories(resultsDir);
        try (FileOutputStream out = new FileOutputStream(resultsDir.resolve("environment.properties").toFile())) {
            env.store(out, null);
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverManager.setDriver(BrowserFactory.createDriver());
        DriverManager.getDriver().manage().window().maximize();
        DriverManager.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));
        DriverManager.getDriver().get(ConfigReader.getBaseUrl() + "/web/index.php/auth/login");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            attachScreenshot();
        }
        if (DriverManager.getDriver() != null) {
            DriverManager.getDriver().quit();
        }
        DriverManager.removeDriver();
    }

    @Attachment(value = "Failure Screenshot", type = "image/png")
    private byte[] attachScreenshot() {
        return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
    }
}
