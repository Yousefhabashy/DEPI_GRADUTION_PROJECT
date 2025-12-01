package Tests.Base;

import Utils.ScreenshotUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

public class TestBase {

    public static WebDriver driver;

    public static ChromeOptions chromeOptions() {
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();

        // Disable password manager completely
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);

        // Disable Chrome Safety/Leak detection
        prefs.put("profile.password_manager_leak_detection", false);

        options.setExperimentalOption("prefs", prefs);

        // Disable any password related UI
        options.addArguments("--disable-features=PasswordManagerOnboarding,PasswordLeakDetection,PasswordCheck");
        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--disable-notifications");

        // Avoid automation banner issues
        options.addArguments("--disable-blink-features=AutomationControlled");

        return options;
    }


    @BeforeSuite
    @Parameters({"browser"})
    public void startDriver(@Optional("chrome") String browserName) {
        if (browserName.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver(chromeOptions());
        } else if (browserName.equalsIgnoreCase("chrome-headless")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--window-size=1920,1080");

            driver = new ChromeDriver(options);
        } else if (browserName.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        } else if (browserName.equalsIgnoreCase("edge")) {
            driver = new EdgeDriver();
        } else {
            System.out.println("DRIVER NOT FOUND!!!!");
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(6));
        driver.get("https://www.saucedemo.com");
    }

    public void loginUser(String userName, String password) {

        waitFor().until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));

        WebElement userNameBox = driver.findElement(By.id("user-name"));
        WebElement passwordBox = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));

        userNameBox.sendKeys(userName);
        passwordBox.sendKeys(password);

        loginButton.click();
    }

    @AfterMethod
    public void screenshotOnFailure(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            System.out.println("Failed!");
            System.out.println("Taking Screenshot");
            ScreenshotUtils.takeScreenshot(driver, result.getName());
        }
    }

    @AfterClass
    public void logoutUser() {

        if (!Objects.equals(driver.getCurrentUrl(), "https://www.saucedemo.com/")) {
            WebElement sideBarButton = driver.findElement(By.id("react-burger-menu-btn"));
            sideBarButton.click();

            waitFor().until(ExpectedConditions.visibilityOfElementLocated(By.id("logout_sidebar_link")));

            WebElement logout = driver.findElement(By.id("logout_sidebar_link"));
            WebElement resetAppState = driver.findElement(By.id("reset_sidebar_link"));

            resetAppState.click();
            logout.click();

            Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/");
        }
    }

    public Wait<WebDriver> waitFor() {

        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(2))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class);
    }

    @AfterSuite
    public void stopDriver() {
        driver.quit();
    }
}

