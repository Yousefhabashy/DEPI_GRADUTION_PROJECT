package Tests.EndToEnd.LockedOutUser;

import Tests.Base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginAttemptAndErrorVerificationTest extends TestBase {

//    Login as locked out user ==> check errors

    @Test(priority = 1)
    public void loginAsLockedOutUser() {

        loginUser("locked_out_user", "secret_sauce");

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("div.error-message-container.error"))));

        WebElement errorMessage = driver.findElement(By.cssSelector("div.error-message-container.error"));

        Assert.assertTrue(errorMessage.isDisplayed());
        Assert.assertTrue(errorMessage.getText().contains("Sorry, this user has been locked out."));

    }
}
