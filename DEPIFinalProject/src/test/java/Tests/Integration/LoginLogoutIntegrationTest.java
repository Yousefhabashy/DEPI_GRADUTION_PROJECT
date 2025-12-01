package Tests.Integration;

import Tests.Base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class LoginLogoutIntegrationTest extends TestBase {

    // Login as standard user ==> logout user.

    @Test(priority = 1)
    public void loginAsStandardUser() {

        loginUser("standard_user", "secret_sauce");

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("inventory"));
    }

    @Test(dependsOnMethods = {"loginAsStandardUser"})
    public void logoutUser() {

        WebElement sideBarButton = driver.findElement(By.id("react-burger-menu-btn"));
        sideBarButton.click();

        waitFor().until(ExpectedConditions.visibilityOfElementLocated(By.id("logout_sidebar_link")));

        WebElement logout = driver.findElement(By.id("logout_sidebar_link"));
        logout.click();

        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/");
    }
}
