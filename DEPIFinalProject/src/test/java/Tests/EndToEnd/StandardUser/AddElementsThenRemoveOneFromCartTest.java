package Tests.EndToEnd.StandardUser;

import Tests.Base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class AddElementsThenRemoveOneFromCartTest extends TestBase {


    // Login as standard user ==> add product 1 ==> add product 2 ==> check if products appears in cart ==> remove product ==> check is removed.

    String ProductTitle;
    String ProductPrice;
    String ProductTitle2;
    String ProductPrice2;


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
    public void addToCartFromHomePage2() {

        ProductTitle2 = driver.findElement(By.xpath("//*[@id=\"item_0_title_link\"]/div")).getText();
        ProductPrice2 = driver.findElement(By.xpath("//*[@id=\"inventory_container\"]/div/div[2]/div[2]/div[2]/div")).getText();

        WebElement addToCartButton = driver.findElement(By.id("add-to-cart-sauce-labs-bike-light"));
        addToCartButton.click();

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.id("remove-sauce-labs-bike-light"))));
        WebElement removeButton = driver.findElement(By.id("remove-sauce-labs-bike-light"));

        Assert.assertTrue(removeButton.isDisplayed());

        WebElement cartBadge = driver.findElement(By.cssSelector("span.shopping_cart_badge"));
        Assert.assertEquals(cartBadge.getText(), "2");
    }

    @Test(dependsOnMethods = {"addToCartFromHomePage2"})
    public void EnterCart() {
        WebElement cartBtn = driver.findElement(By.className("shopping_cart_link"));
        cartBtn.click();
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("cart.html"));
    }

    @Test(dependsOnMethods = {"EnterCart"})
    public void Check() {
        String cartProduct1Title = driver.findElement(By.id("item_4_title_link")).getText();
        String cartProduct1Price = driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[1]/div[3]/div[2]/div[2]/div")).getText();
        String cartProduct2Title = driver.findElement(By.id("item_0_title_link")).getText();
        String cartProduct2Price = driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[1]/div[4]/div[2]/div[2]/div")).getText();
        Assert.assertEquals(cartProduct1Title,ProductTitle);
        Assert.assertEquals(cartProduct2Title,ProductTitle2);
        Assert.assertEquals(cartProduct1Price,ProductPrice);
        Assert.assertEquals(cartProduct2Price,ProductPrice2);
    }
    @Test(dependsOnMethods = {"Check"})
    public void Remove1() {
        WebElement removeBtn = driver.findElement(By.id("remove-sauce-labs-bike-light"));
        removeBtn.click();
        WebElement cartBadge = driver.findElement(By.cssSelector("span.shopping_cart_badge"));
        Assert.assertEquals(cartBadge.getText(), "1");
    }
}
