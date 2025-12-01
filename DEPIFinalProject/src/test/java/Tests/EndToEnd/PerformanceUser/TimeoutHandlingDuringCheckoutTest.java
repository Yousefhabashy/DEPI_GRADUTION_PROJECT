package Tests.EndToEnd.PerformanceUser;

import Tests.Base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class TimeoutHandlingDuringCheckoutTest extends TestBase {

    // With handling wait for every action to make sure elements appears
    // Login as performance glitch user ==> add product 1 ==> add product 2 ==> check if products appears in cart ==> complete checkout.

    String product1Title;
    String product1Price;

    String product2Title;
    String product2Price;

    @Test(priority = 1)
    public void loginAsPerformanceGlitchUser() {

        loginUser("performance_glitch_user", "secret_sauce");

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory"));
    }

    @Test(dependsOnMethods = {"loginAsPerformanceGlitchUser"})
    public void addProduct1() {
        waitFor().until(ExpectedConditions.visibilityOfElementLocated(By.id("item_4_title_link")));

        product1Title = driver.findElement(By.id("item_4_title_link")).getText();
        product1Price = driver.findElement(By.xpath("//*[@id=\"inventory_container\"]/div/div[1]/div[2]/div[2]/div")).getText();

        WebElement addToCartButton = driver.findElement(By.id("add-to-cart-sauce-labs-backpack"));
        addToCartButton.click();

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.id("remove-sauce-labs-backpack"))));
        WebElement removeButton = driver.findElement(By.id("remove-sauce-labs-backpack"));

        Assert.assertTrue(removeButton.isDisplayed());

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("span.shopping_cart_badge"))));
        WebElement cartBadge = driver.findElement(By.cssSelector("span.shopping_cart_badge"));

        Assert.assertTrue(cartBadge.isDisplayed());
        Assert.assertEquals(cartBadge.getText(), "1");
    }

    @Test(dependsOnMethods = {"addProduct1"})
    public void addProduct2() {

        waitFor().until(ExpectedConditions.visibilityOfElementLocated(By.id("item_1_title_link")));

        product2Title = driver.findElement(By.id("item_1_title_link")).getText();
        product2Price = driver.findElement(By.xpath("//*[@id=\"inventory_container\"]/div/div[3]/div[2]/div[2]/div")).getText();

        WebElement addToCartButton = driver.findElement(By.id("add-to-cart-sauce-labs-bolt-t-shirt"));
        addToCartButton.click();

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.id("remove-sauce-labs-bolt-t-shirt"))));
        WebElement removeButton = driver.findElement(By.id("remove-sauce-labs-bolt-t-shirt"));

        Assert.assertTrue(removeButton.isDisplayed());

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("span.shopping_cart_badge"))));
        WebElement cartBadge = driver.findElement(By.cssSelector("span.shopping_cart_badge"));

        Assert.assertTrue(cartBadge.isDisplayed());
        Assert.assertEquals(cartBadge.getText(), "2");
    }

    @Test(dependsOnMethods = {"addProduct2"})
    public void verifyProductsAppearsInCart() {

        WebElement cartPage = driver.findElement(By.cssSelector("a.shopping_cart_link"));
        cartPage.click();

        waitFor().until(ExpectedConditions.urlContains("/cart"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/cart"));

        waitFor().until(ExpectedConditions.visibilityOfElementLocated(By.id("item_4_title_link")));

        String cartProduct1Title = driver.findElement(By.id("item_4_title_link")).getText();
        String cartProduct1Price = driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[1]/div[3]/div[2]/div[2]/div")).getText();

        Assert.assertEquals(cartProduct1Title, product1Title);
        Assert.assertEquals(cartProduct1Price, product1Price);

        String cartProduct2Title = driver.findElement(By.id("item_1_title_link")).getText();
        String cartProduct2Price = driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[1]/div[4]/div[2]/div[2]/div")).getText();

        Assert.assertEquals(cartProduct2Title, product2Title);
        Assert.assertEquals(cartProduct2Price, product2Price);
    }

    @Test(dependsOnMethods = {"verifyProductsAppearsInCart"})
    public void completeCheckout() {

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/cart"));

        WebElement checkoutButton = driver.findElement(By.id("checkout"));
        checkoutButton.click();

        waitFor().until(ExpectedConditions.urlContains("/checkout-step-one"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/checkout-step-one"));

        waitFor().until(ExpectedConditions.visibilityOfElementLocated(By.id("first-name")));

        WebElement firstNameBox = driver.findElement(By.id("first-name"));
        WebElement lastNameBox = driver.findElement(By.id("last-name"));
        WebElement postalCode = driver.findElement(By.id("postal-code"));

        firstNameBox.sendKeys("Yousef");
        lastNameBox.sendKeys("Habashy");
        postalCode.sendKeys("32551");

        WebElement continueButton = driver.findElement(By.id("continue"));
        continueButton.click();

        waitFor().until(ExpectedConditions.urlContains("/checkout-step-two"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/checkout-step-two"));

        WebElement finishButton = driver.findElement(By.id("finish"));
        finishButton.click();

        waitFor().until(ExpectedConditions.urlContains("/checkout-complete"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/checkout-complete"));

        waitFor().until(ExpectedConditions.visibilityOfElementLocated(By.className("complete-header")));
        WebElement successMessage = driver.findElement(By.className("complete-header"));
        Assert.assertEquals(successMessage.getText(), "Thank you for your order!");
    }
}
