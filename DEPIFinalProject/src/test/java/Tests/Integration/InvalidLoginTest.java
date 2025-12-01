package Tests.Integration;

import Tests.Base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class InvalidLoginTest extends TestBase {

    // Login with invalid username


    @Test(priority = 1)
    public void invalidUserLogin() {


        loginUser("standarrd_user", "secret_sauce");

        WebElement errorMessage = driver.findElement(By.cssSelector("div.error-message-container.error"));
        Assert.assertTrue(errorMessage.getText().contains("Username and password do not match any user in this service"));

        // to clean the elements for next script
        WebElement userNameBox = driver.findElement(By.id("user-name"));
        WebElement passwordBox = driver.findElement(By.id("password"));
        userNameBox.clear();
        passwordBox.clear();
    }
}
