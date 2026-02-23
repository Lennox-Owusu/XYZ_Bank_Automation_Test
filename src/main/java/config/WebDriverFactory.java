package config;

import utils.LoggerUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.time.Duration;

public class WebDriverFactory {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private WebDriverFactory() {
    }

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            driver.set(createDriver());
        }
        return driver.get();
    }

    private static WebDriver createDriver() {
        String browser = ConfigReader.getBrowser().toLowerCase();
        boolean headless = ConfigReader.isHeadless();

        LoggerUtil.info(WebDriverFactory.class, "Initializing " + browser + " driver (Headless: " + headless + ")");

        WebDriver webDriver;

        switch (browser) {
            case "chrome":
                webDriver = createChromeDriver(headless);
                break;
            case "firefox":
                webDriver = createFirefoxDriver(headless);
                break;
            case "edge":
                webDriver = createEdgeDriver(headless);
                break;
            default:
                LoggerUtil.warn(WebDriverFactory.class, "Unknown browser: " + browser + ". Defaulting to Chrome");
                webDriver = createChromeDriver(headless);
        }

        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));

        LoggerUtil.info(WebDriverFactory.class, "WebDriver initialized successfully");
        return webDriver;
    }

    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        // Check if running in CI environment
        String ciEnv = System.getenv("CI");
        boolean isCI = "true".equals(ciEnv);

        if (isCI || headless) {
            options.addArguments("--headless=new");
            LoggerUtil.info(WebDriverFactory.class, "Running Chrome in HEADLESS mode (CI: " + isCI + ")");
        }

        // Essential arguments for CI/Linux environments
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-software-rasterizer");
        options.addArguments("--disable-setuid-sandbox");

        if (isCI) {
            // Additional CI-specific options
            options.addArguments("--disable-background-networking");
            options.addArguments("--disable-default-apps");
            options.addArguments("--disable-sync");
            options.addArguments("--metrics-recording-only");
            options.addArguments("--mute-audio");
        }

        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("--headless");
        }
        return new FirefoxDriver(options);
    }

    private static WebDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        if (headless) {
            options.addArguments("--headless");
        }
        return new EdgeDriver(options);
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            LoggerUtil.info(WebDriverFactory.class, "Closing WebDriver");
            driver.get().quit();
            driver.remove();
        }
    }
}
