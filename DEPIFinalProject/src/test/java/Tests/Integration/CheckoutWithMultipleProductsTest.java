package Tests.Integration;

import Tests.Base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class CheckoutWithMultipleProductsTest extends TestBase {

    // Login as standard user ==> add products to cart ==> checkout products.

    double totalPrice;
    double bikeLightPrice;

    @Test(priority = 1)
    public void loginAsStandardUser() {

        loginUser("standard_user", "secret_sauce");

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("inventory"));
    }

    @Test(dependsOnMethods = {"loginAsStandardUser"})
    public void addMultipleProductsToCart() {

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory"));

        String backPackPriceText = driver.findElement(By.xpath("//*[@id=\"inventory_container\"]/div/div[1]/div[2]/div[2]/div")).getText();
        double backPackPrice = Double.parseDouble(backPackPriceText.replace("$", ""));

        WebElement backBackAddToCartBtn = driver.findElement(By.id("add-to-cart-sauce-labs-backpack"));
        backBackAddToCartBtn.click();

        String bikeLightPriceText = driver.findElement(By.xpath("//*[@id=\"inventory_container\"]/div/div[2]/div[2]/div[2]/div")).getText();
        bikeLightPrice = Double.parseDouble(bikeLightPriceText.replace("$", ""));

        WebElement bikeLightAddToCartBtn = driver.findElement(By.id("add-to-cart-sauce-labs-bike-light"));
        bikeLightAddToCartBtn.click();

        String fleeceJacketPriceText = driver.findElement(By.xpath("//*[@id=\"inventory_container\"]/div/div[4]/div[2]/div[2]/div")).getText();
        double fleeceJacketPrice = Double.parseDouble(fleeceJacketPriceText.replace("$", ""));

        WebElement fleeceJacketAddToCartBtn = driver.findElement(By.id("add-to-cart-sauce-labs-fleece-jacket"));
        fleeceJacketAddToCartBtn.click();

        totalPrice = backPackPrice + bikeLightPrice + fleeceJacketPrice;

        WebElement cartBadge = driver.findElement(By.cssSelector("span.shopping_cart_badge"));
        Assert.assertEquals(cartBadge.getText(), "3");

        WebElement shoppingCart = driver.findElement(By.id("shopping_cart_container"));
        shoppingCart.click();
    }

    @Test(dependsOnMethods = {"addMultipleProductsToCart"})
    public void checkoutProducts() {

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/cart"));

        WebElement checkoutPageButton = driver.findElement(By.id("checkout"));
        checkoutPageButton.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/checkout-step-one"));

        WebElement firstNameBox = driver.findElement(By.id("first-name"));
        WebElement lastNameBox = driver.findElement(By.id("last-name"));
        WebElement postalCode = driver.findElement(By.id("postal-code"));

        firstNameBox.sendKeys("Yousef");
        lastNameBox.sendKeys("Habashy");
        postalCode.sendKeys("32551");

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
