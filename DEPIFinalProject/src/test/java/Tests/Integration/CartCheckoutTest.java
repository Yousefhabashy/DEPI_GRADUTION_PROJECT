package Tests.Integration;

import Tests.Base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class CartCheckoutTest extends TestBase {

    // Login as standard user ==> add product from home page to cart ==> check product appears in cart ==> checkout product.

    String ProductPrice;
    String ProductTitle;

    @Test(priority = 1)
    public void loginAsStandardUser() {

        loginUser("standard_user", "secret_sauce");

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("inventory"));
    }

    @Test(dependsOnMethods = {"loginAsStandardUser"})
    public void addToCartFromProductPage() {

        WebElement productPageLink = driver.findElement(By.id("item_5_title_link"));
        productPageLink.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory-item.html?id=5"));

        ProductPrice = driver.findElement(By.cssSelector("div.inventory_details_price")).getText();
        ProductTitle = driver.findElement(By.cssSelector("div.inventory_details_name.large_size")).getText();

        WebElement addToCartButton = driver.findElement(By.id("add-to-cart"));
        addToCartButton.click();

        waitFor().until(ExpectedConditions.visibilityOfElementLocated(By.id("remove")));
        WebElement removeButton = driver.findElement(By.id("remove"));

        Assert.assertTrue((removeButton.isDisplayed()));

        waitFor().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.shopping_cart_badge")));

        WebElement cartBadge = driver.findElement(By.cssSelector("span.shopping_cart_badge"));
        Assert.assertEquals(cartBadge.getText(), "1");
    }

    @Test(dependsOnMethods = {"addToCartFromProductPage"})
    public void checkSuccessfulAddToCart() {

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

        String checkoutProductPrice = driver.findElement(By.cssSelector("div.inventory_item_price")).getText();
        String checkoutProductTitle = driver.findElement(By.cssSelector("div.inventory_item_name")).getText();

        Assert.assertEquals(ProductPrice, checkoutProductPrice);
        Assert.assertEquals(ProductTitle, checkoutProductTitle);

        WebElement finishButton = driver.findElement(By.id("finish"));
        finishButton.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/checkout-complete"));

        WebElement successMessage = driver.findElement(By.cssSelector("h2.complete-header"));
        Assert.assertEquals(successMessage.getText(), "Thank you for your order!");
    }
}
