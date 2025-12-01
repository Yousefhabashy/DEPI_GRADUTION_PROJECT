package Tests.Integration;

import Tests.Base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class SidebarNavigationIntegrationTest extends TestBase {

    @BeforeMethod
    public void setup() {
        ChromeOptions options = new ChromeOptions();

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("autofill.profile_enabled", false);
        prefs.put("autofill.credit_card_enabled", false);
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        options.addArguments("--incognito");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-save-password-bubble");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");

        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
    }
    @AfterMethod
    public void teardown() {

        driver.quit();
    }

    //------------------------------------------------------------------//

    @Test
    public void navigateToItemDetailsTest() {

        driver.findElement(By.id("item_4_img_link")).click();

        Assert.assertTrue(driver.getCurrentUrl().contains("inventory-item.html?id=4"));
    }
    @Test
    public void navigateBackToInventoryTest() {
        driver.findElement(By.id("item_4_title_link")).click();
        driver.findElement(By.id("back-to-products")).click();

        Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"));
    }

    @Test
    public void openSidebarTest() {
        driver.findElement(By.id("react-burger-menu-btn")).click();

        Assert.assertTrue(driver.findElement(By.className("bm-menu")).isDisplayed());
    }

    @Test
    public void closeSidebarTest() throws InterruptedException {
        driver.findElement(By.id("react-burger-menu-btn")).click();
        Thread.sleep(500);
        driver.findElement(By.id("react-burger-cross-btn")).click();
        Thread.sleep(500);
        WebElement hid = driver.findElement(By.className("bm-menu-wrap"));
        String exacthid = hid.getAttribute("aria-hidden");
        Assert.assertEquals(exacthid, "true");
    }

    @Test
    public void navigateToAboutTest() throws InterruptedException {
        driver.findElement(By.id("react-burger-menu-btn")).click();
        Thread.sleep(500);
        driver.findElement(By.id("about_sidebar_link")).click();

        Assert.assertTrue(driver.getCurrentUrl().contains("https://saucelabs.com/"));
    }

    @Test
    public void LogoutTest() throws InterruptedException {
        driver.findElement(By.id("react-burger-menu-btn")).click();
        Thread.sleep(500);
        driver.findElement(By.id("logout_sidebar_link")).click();

        Assert.assertTrue(driver.getCurrentUrl().contains("https://www.saucedemo.com/"));
    }

    @Test
    public void ResetTest() throws InterruptedException {
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.id("react-burger-menu-btn")).click();
        Thread.sleep(500);
        driver.findElement(By.id("reset_sidebar_link")).click();
        driver.findElement(By.className("shopping_cart_link")).click();

        Assert.assertTrue(driver.findElements(By.className("shopping_cart_badge")).isEmpty());
    }

    @Test
    public void CartNavigationTest() {
        driver.findElement(By.className("shopping_cart_link")).click();

        Assert.assertTrue(driver.getCurrentUrl().contains("cart.html"));
    }

    @Test
    public void FromCartToItemsTest() {
        driver.findElement(By.className("shopping_cart_link")).click();
        driver.findElement(By.id("continue-shopping")).click();

        Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"));
    }
}
