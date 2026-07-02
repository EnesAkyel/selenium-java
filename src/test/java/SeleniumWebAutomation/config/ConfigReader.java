package SeleniumWebAutomation.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Properties props = new Properties();

    static {
        try (InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) throw new RuntimeException("config.properties not found in classpath");
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    private ConfigReader() {}

    public static String getBaseUrl() { return props.getProperty("base.url"); }
    public static String getBrowser() { return props.getProperty("browser", "chrome"); }
    public static int getImplicitWait() { return Integer.parseInt(props.getProperty("implicit.wait", "10")); }
    public static int getExplicitWait() { return Integer.parseInt(props.getProperty("explicit.wait", "15")); }
    public static String getUsername() { return props.getProperty("username"); }
    public static String getPassword() { return props.getProperty("password"); }
}
