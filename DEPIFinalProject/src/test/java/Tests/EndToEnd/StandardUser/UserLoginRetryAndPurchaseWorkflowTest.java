package Tests.EndToEnd.StandardUser;

import Tests.Base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class UserLoginRetryAndPurchaseWorkflowTest extends TestBase {

    // Login with invalid user ==> Login as standard user ==> add product to cart from product details page  ==> check if product appears in cart ==> checkout product.


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
    public void loginWithInvalidUser() {

        loginUser("yousef_user", "secret_sauce");

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("div.error-message-container.error"))));
        WebElement errorMessage = driver.findElement(By.cssSelector("div.error-message-container.error"));

        Assert.assertTrue(errorMessage.getText().contains("Username and password do not match any user in this service"));
    }

    @Test(dependsOnMethods = {"loginWithInvalidUser"})
    public void loginWithValidUser() {

        loginUser("standard_user", "secret_sauce");

        waitFor().until(ExpectedConditions.urlContains("/inventory"));

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory"));
    }

    @Test(dependsOnMethods = {"loginWithValidUser"})
    public void addToCartFromProductPage() {

        WebElement productPageLink = driver.findElement(By.id("item_5_title_link"));
        productPageLink.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory-item"));

        String ProductPrice = driver.findElement(By.cssSelector("div.inventory_details_price")).getText();
        String ProductTitle = driver.findElement(By.cssSelector("div.inventory_details_name.large_size")).getText();


        waitFor().until(ExpectedConditions.visibilityOfElementLocated(By.id("add-to-cart")));

        WebElement addToCartButton = driver.findElement(By.id("add-to-cart"));
        addToCartButton.click();

        WebElement cartBadge = driver.findElement(By.cssSelector("span.shopping_cart_badge"));
        Assert.assertEquals(cartBadge.getText(), "1");

        WebElement shoppingCart = driver.findElement(By.cssSelector("a.shopping_cart_link"));
        shoppingCart.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/cart"));

        String addedProductTitle = driver.findElement(By.cssSelector("div.inventory_item_name")).getText();
        String addedProductPrice = driver.findElement(By.cssSelector("div.inventory_item_price")).getText();

        Assert.assertEquals(ProductPrice, addedProductPrice);
        Assert.assertEquals(ProductTitle, addedProductTitle);
    }

    @Test(dependsOnMethods = {"addToCartFromProductPage"})
    public void checkoutProduct() {
        WebElement checkoutButton = driver.findElement(By.id("checkout"));
        checkoutButton.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/checkout-step-one"));

        WebElement firstName = driver.findElement(By.id("first-name"));
        WebElement lastName = driver.findElement(By.id("last-name"));
        WebElement postalCode = driver.findElement(By.id("postal-code"));

        WebElement continueButton = driver.findElement(By.id("continue"));

        firstName.sendKeys("Yousef");
        lastName.sendKeys("Habashy");
        postalCode.sendKeys("4588");

        continueButton.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/checkout-step-two"));

        WebElement finishButton = driver.findElement(By.id("finish"));
        finishButton.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/checkout-complete"));

        WebElement successMessage = driver.findElement(By.cssSelector("h2.complete-header"));
        Assert.assertEquals(successMessage.getText(), "Thank you for your order!");
    }
}
