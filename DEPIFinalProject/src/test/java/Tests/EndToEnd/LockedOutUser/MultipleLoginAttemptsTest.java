package Tests.EndToEnd.LockedOutUser;

import Tests.Base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MultipleLoginAttemptsTest extends TestBase {

    //    Login as locked out user ==> check error ==> check login with wrong password error

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
    public void loginAsLockedOutUser() {

        loginUser("locked_out_user", "secret_sauce");

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("div.error-message-container.error"))));

        WebElement errorMessage = driver.findElement(By.cssSelector("div.error-message-container.error"));

        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertTrue(errorMessage.getText().contains("Sorry, this user has been locked out."));
    }

    @Test(dependsOnMethods = {"loginAsLockedOutUser"})
    public void loginWithWrongPassword() {

        loginUser("locked_out_user", "public_sauce");

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("div.error-message-container.error"))));

        WebElement errorMessage = driver.findElement(By.cssSelector("div.error-message-container.error"));

        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertTrue(errorMessage.getText().contains("Username and password do not match any user in this service"));
    }

    @Test(dependsOnMethods = {"loginWithWrongPassword"})
    public void loginAgainAsLockedOutUser() {

        loginUser("locked_out_user", "secret_sauce");

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("div.error-message-container.error"))));

        WebElement errorMessage = driver.findElement(By.cssSelector("div.error-message-container.error"));

        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertTrue(errorMessage.getText().contains("Sorry, this user has been locked out."));
    }
}
