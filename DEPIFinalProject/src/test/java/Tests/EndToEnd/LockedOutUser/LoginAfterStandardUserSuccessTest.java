package Tests.EndToEnd.LockedOutUser;

import Tests.Base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class LoginAfterStandardUserSuccessTest extends TestBase {

    // Login as standard user ==> logout standard user ==> login as locked out user.

    public void loginUser(String userName, String password) {

        waitFor().until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));

        WebElement userNameBox = driver.findElement(By.id("user-name"));
        WebElement passwordBox = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));

        userNameBox.sendKeys(Keys.CONTROL + "a");
        userNameBox.sendKeys(Keys.DELETE);

        passwordBox.sendKeys(Keys.CONTROL + "a");
        passwordBox.sendKeys(Keys.DELETE);

        userNameBox.sendKeys(userName);
        passwordBox.sendKeys(password);

        loginButton.click();
    }

    @Test(priority = 1)
    public void loginAsStandardUser() {

        loginUser("standard_user", "secret_sauce");

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory"));
    }

    @Test(dependsOnMethods = {"loginAsStandardUser"})
    public void logoutStandardUser() {
        WebElement sideBarButton = driver.findElement(By.id("react-burger-menu-btn"));
        sideBarButton.click();

        waitFor().until(ExpectedConditions.visibilityOfElementLocated(By.id("logout_sidebar_link")));

        WebElement logout = driver.findElement(By.id("logout_sidebar_link"));

        logout.click();

        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/");
    }

    @Test(dependsOnMethods = {"logoutStandardUser"})
    public void loginAsLockedOutUser() {

        loginUser("locked_out_user", "secret_sauce");

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("div.error-message-container.error"))));

        WebElement errorMessage = driver.findElement(By.cssSelector("div.error-message-container.error"));

        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertTrue(errorMessage.getText().contains("Sorry, this user has been locked out."));
    }
}
