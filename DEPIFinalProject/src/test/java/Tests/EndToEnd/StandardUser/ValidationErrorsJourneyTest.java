package Tests.EndToEnd.StandardUser;

import Tests.Base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class ValidationErrorsJourneyTest extends TestBase {

    // check for required fields errors ==> login with invalid user ==> login as standard user ==> add product to cart ==> check checkout information fields errors ==> complete checkout.

    private static final Logger log = LoggerFactory.getLogger(ValidationErrorsJourneyTest.class);

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

    public void fillCheckoutInformation(String firstName, String lastName, String postalCode) {

        waitFor().until(ExpectedConditions.visibilityOfElementLocated(By.id("first-name")));

        WebElement firstNameBox = driver.findElement(By.id("first-name"));
        WebElement lastNameBox = driver.findElement(By.id("last-name"));
        WebElement postalCodeBox = driver.findElement(By.id("postal-code"));

        firstNameBox.sendKeys(Keys.CONTROL + "a");
        firstNameBox.sendKeys(Keys.DELETE);

        lastNameBox.sendKeys(Keys.CONTROL + "a");
        lastNameBox.sendKeys(Keys.DELETE);

        postalCodeBox.sendKeys(Keys.CONTROL + "a");
        postalCodeBox.sendKeys(Keys.DELETE);

        firstNameBox.sendKeys(firstName);
        lastNameBox.sendKeys(lastName);
        postalCodeBox.sendKeys(postalCode);
    }


    @Test(priority = 1)
    public void checkLoginRequiredFieldError() {

        waitFor().until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));

        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("div.error-message-container.error"))));
        WebElement errorMessage = driver.findElement(By.cssSelector("div.error-message-container.error"));

        Assert.assertTrue(errorMessage.getText().contains("Username is required"));

        WebElement userNameBox = driver.findElement(By.id("user-name"));

        userNameBox.sendKeys("standard_user");
        loginButton.click();

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("div.error-message-container.error"))));

        Assert.assertTrue(errorMessage.getText().contains("Password is required"));
    }

    @Test(dependsOnMethods = {"checkLoginRequiredFieldError"})
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
    public void addProductToCart() {

        String productTitle = driver.findElement(By.xpath("//*[@id=\"item_5_title_link\"]/div")).getText();
        String productPrice = driver.findElement(By.xpath("//*[@id=\"inventory_container\"]/div/div[4]/div[2]/div[2]/div")).getText();

        WebElement productLink = driver.findElement(By.id("item_5_title_link"));
        productLink.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("inventory-item.html?id=5"));

        String openedProduct1Title = driver.findElement(By.cssSelector("div.inventory_details_name.large_size")).getText();
        String openedProduct1Price = driver.findElement(By.cssSelector("div.inventory_details_price")).getText();

        Assert.assertEquals(productTitle, openedProduct1Title);
        Assert.assertEquals(productPrice, openedProduct1Price);

        WebElement addToCartButton = driver.findElement(By.id("add-to-cart"));
        addToCartButton.click();

        WebElement removeButton = driver.findElement(By.id("remove"));
        Assert.assertTrue(removeButton.isDisplayed());

        WebElement cartBadge = driver.findElement(By.cssSelector("span.shopping_cart_badge"));

        Assert.assertTrue(cartBadge.isDisplayed());
        Assert.assertEquals(cartBadge.getText(), "1");
    }

    @Test(dependsOnMethods = {"addProductToCart"})
    public void openCheckoutPage() {

        WebElement cartPage = driver.findElement(By.cssSelector("a.shopping_cart_link"));
        cartPage.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/cart"));

        WebElement checkoutButton = driver.findElement(By.id("checkout"));
        checkoutButton.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/checkout-step-one"));
    }

    @Test(dependsOnMethods = {"openCheckoutPage"})
    public void checkFirstName() {

        fillCheckoutInformation("", "Habashy", "33352");

        WebElement continueButton = driver.findElement(By.id("continue"));
        continueButton.click();

        waitFor().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.error-message-container.error")));

        WebElement errorMessage = driver.findElement(By.cssSelector("div.error-message-container.error"));

        Assert.assertTrue(errorMessage.getText().contains("First Name is required"));
    }

    @Test(dependsOnMethods = {"checkFirstName"})
    public void checkLastName() {

        fillCheckoutInformation("Yousef", "", "");

        WebElement continueButton = driver.findElement(By.id("continue"));
        continueButton.click();

        waitFor().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.error-message-container.error")));

        WebElement errorMessage = driver.findElement(By.cssSelector("div.error-message-container.error"));

        Assert.assertTrue(errorMessage.getText().contains("Last Name is required"));
    }


    @Test(dependsOnMethods = {"checkLastName"})
    public void checkPostalCode() {

        fillCheckoutInformation("Yousef", "Habashy", "");

        WebElement continueButton = driver.findElement(By.id("continue"));
        continueButton.click();

        waitFor().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.error-message-container.error")));

        WebElement errorMessage = driver.findElement(By.cssSelector("div.error-message-container.error"));

        Assert.assertTrue(errorMessage.getText().contains("Postal Code is required"));
    }

    @Test(dependsOnMethods = {"checkPostalCode"})
    public void completeCheckout() {

        fillCheckoutInformation("Yousef", "Habashy", "33352");

        WebElement continueButton = driver.findElement(By.id("continue"));
        continueButton.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/checkout-step-two"));

        WebElement finishButton = driver.findElement(By.id("finish"));
        finishButton.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/checkout-complete"));

        WebElement successMessage = driver.findElement(By.className("complete-header"));
        Assert.assertEquals(successMessage.getText(), "Thank you for your order!");
    }
}
