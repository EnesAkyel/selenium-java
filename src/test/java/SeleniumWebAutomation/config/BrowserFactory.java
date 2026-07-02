package SeleniumWebAutomation.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class BrowserFactory {
    private BrowserFactory() {}

    public static WebDriver createDriver() {
        boolean headless = isHeadless();
        String browser = System.getProperty("browser", ConfigReader.getBrowser()).toLowerCase();

        return switch (browser) {
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions options = new FirefoxOptions();
                if (headless) options.addArguments("-headless");
                yield new FirefoxDriver(options);
            }
            default -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                if (headless) options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
                yield new ChromeDriver(options);
            }
        };
    }

    // CI environment variable is set automatically in GitHub Actions
    private static boolean isHeadless() {
        return "true".equalsIgnoreCase(System.getenv("CI"))
                || "true".equalsIgnoreCase(System.getProperty("headless", "false"));
    }
}
