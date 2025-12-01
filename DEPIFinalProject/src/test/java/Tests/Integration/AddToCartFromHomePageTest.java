package Tests.Integration;

import Tests.Base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class AddToCartFromHomePageTest extends TestBase {

    // Login as standard user ==> add product from home page to cart ==> check product appears in cart.


    String ProductTitle;
    String ProductPrice;

    @Test(priority = 1)
    public void loginAsStandardUser() {

        loginUser("standard_user", "secret_sauce");

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("inventory"));
    }

    @Test(dependsOnMethods = {"loginAsStandardUser"})
    public void addToCartFromHomePage() {

        ProductTitle = driver.findElement(By.xpath("//*[@id=\"item_4_title_link\"]/div")).getText();
        ProductPrice = driver.findElement(By.xpath("//*[@id=\"inventory_container\"]/div/div[1]/div[2]/div[2]/div")).getText();

        WebElement addToCartButton = driver.findElement(By.id("add-to-cart-sauce-labs-backpack"));
        addToCartButton.click();

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.id("remove-sauce-labs-backpack"))));
        WebElement removeButton = driver.findElement(By.id("remove-sauce-labs-backpack"));

        Assert.assertTrue(removeButton.isDisplayed());

        WebElement cartBadge = driver.findElement(By.cssSelector("span.shopping_cart_badge"));
        Assert.assertEquals(cartBadge.getText(), "1");
    }

    @Test(dependsOnMethods = {"addToCartFromHomePage"})
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
