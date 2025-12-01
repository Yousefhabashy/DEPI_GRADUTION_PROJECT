package Tests.Integration;

import Tests.Base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class AddProductFromProductPageTest extends TestBase {

    // Login as standard user ==> add product to cart ==> check product appears in cart.

    String ProductPrice;
    String ProductTitle;

    @Test(priority = 1)
    public void loginAsStandardUser() {

        loginUser("standard_user", "secret_sauce");

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("inventory"));
    }

    @Test(dependsOnMethods = {"loginAsStandardUser"})
    public void addProduct() {

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

    @Test(dependsOnMethods = {"addProduct"})
    public void checkSuccessfulAddToCart() {

        WebElement shoppingCart = driver.findElement(By.cssSelector("a.shopping_cart_link"));
        shoppingCart.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/cart"));

        String addedProductTitle = driver.findElement(By.cssSelector("div.inventory_item_name")).getText();
        String addedProductPrice = driver.findElement(By.cssSelector("div.inventory_item_price")).getText();

        Assert.assertEquals(ProductPrice, addedProductPrice);
        Assert.assertEquals(ProductTitle, addedProductTitle);
    }
}
