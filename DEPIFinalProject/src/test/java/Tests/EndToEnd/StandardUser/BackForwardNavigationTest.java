package Tests.EndToEnd.StandardUser;

import Tests.Base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class BackForwardNavigationTest extends TestBase {


    // 1- Login as standard user ==> 2- open product 1 ==> 3- open product 2 ==> 4- open product 3 ==> 5- add product 3 to cart ==> 6- navigate back to home page ==>
    // 7- navigate back again to product 2 ==> 8- add product 2 to cart ==> 9- check if products appears in cart ==> 10- complete checkout.

    String product2Title;
    String product2Price;

    String product3Title;
    String product3Price;

    @Test(priority = 1)
    public void loginAsStandardUser() {

        loginUser("standard_user", "secret_sauce");

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("inventory"));
    }

    @Test(dependsOnMethods = {"loginAsStandardUser"})
    public void navigate3Products() {

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory"));

        WebElement product1Link = driver.findElement(By.id("item_4_title_link"));
        product1Link.click();
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory-item.html?id=4"));

        driver.navigate().back();
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory"));

        WebElement product2Link = driver.findElement(By.id("item_0_title_link"));
        product2Link.click();
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory-item.html?id=0"));

        driver.navigate().back();
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory"));

        WebElement product3Link = driver.findElement(By.id("item_1_title_link"));
        product3Link.click();
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory-item.html?id=1"));
    }

    @Test(dependsOnMethods = {"navigate3Products"})
    public void addLastProductToCart() {

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory-item.html?id=1"));

        product3Title = driver.findElement(By.cssSelector("div.inventory_details_name.large_size")).getText();
        product3Price = driver.findElement(By.cssSelector("div.inventory_details_price")).getText();

        WebElement addToCartButton = driver.findElement(By.id("add-to-cart"));
        addToCartButton.click();

        WebElement removeButton = driver.findElement(By.id("remove"));
        Assert.assertTrue(removeButton.isDisplayed());

        WebElement cartBadge = driver.findElement(By.cssSelector("span.shopping_cart_badge"));

        Assert.assertTrue(cartBadge.isDisplayed());
        Assert.assertEquals(cartBadge.getText(), "1");
    }

    @Test(dependsOnMethods = {"addLastProductToCart"})
    public void addPreviousProduct() {

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory-item.html?id=1"));

        driver.navigate().back();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory"));

        driver.navigate().back();
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory-item.html?id=0"));

        product2Title = driver.findElement(By.cssSelector("div.inventory_details_name.large_size")).getText();
        product2Price = driver.findElement(By.cssSelector("div.inventory_details_price")).getText();

        WebElement addToCartButton = driver.findElement(By.id("add-to-cart"));
        addToCartButton.click();

        WebElement removeButton = driver.findElement(By.id("remove"));
        Assert.assertTrue(removeButton.isDisplayed());

        WebElement cartBadge = driver.findElement(By.cssSelector("span.shopping_cart_badge"));

        Assert.assertTrue(cartBadge.isDisplayed());
        Assert.assertEquals(cartBadge.getText(), "2");
    }

    @Test(dependsOnMethods = {"addPreviousProduct"})
    public void checkAddedProducts() {

        WebElement cartPage = driver.findElement(By.cssSelector("a.shopping_cart_link"));
        cartPage.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/cart"));

        driver.navigate().back();
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory-item.html?id=0"));

        driver.navigate().forward();
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/cart"));

        String product3CartTitle = driver.findElement(By.xpath("//*[@id=\"item_1_title_link\"]/div")).getText();
        String product3CartPrice = driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[1]/div[3]/div[2]/div[2]/div")).getText();

        Assert.assertEquals(product3CartTitle, product3Title);
        Assert.assertEquals(product3CartPrice, product3Price);

        String product2CartTitle = driver.findElement(By.xpath("//*[@id=\"item_0_title_link\"]/div")).getText();
        String product2CartPrice = driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[1]/div[4]/div[2]/div[2]/div")).getText();

        Assert.assertEquals(product2CartTitle, product2Title);
        Assert.assertEquals(product2CartPrice, product2Price);
    }

    @Test(dependsOnMethods = {"checkAddedProducts"})
    public void completeCheckout() {
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/cart"));

        WebElement checkoutButton = driver.findElement(By.id("checkout"));
        checkoutButton.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/checkout-step-one"));

        WebElement firstNameBox = driver.findElement(By.id("first-name"));
        firstNameBox.sendKeys("Yousef");

        driver.navigate().back();
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/cart"));

        driver.navigate().forward();
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/checkout-step-one"));

        WebElement lastNameBox = driver.findElement(By.id("last-name"));
        WebElement postalCode = driver.findElement(By.id("postal-code"));

        firstNameBox.sendKeys("Yousef");
        lastNameBox.sendKeys("Habashy");
        postalCode.sendKeys("32551");

        WebElement continueButton = driver.findElement(By.id("continue"));
        continueButton.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/checkout-step-two"));

        driver.navigate().back();
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/checkout-step-one"));

        driver.navigate().forward();
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/checkout-step-two"));

        WebElement finishButton = driver.findElement(By.id("finish"));
        finishButton.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/checkout-complete"));

        WebElement successMessage = driver.findElement(By.className("complete-header"));
        Assert.assertEquals(successMessage.getText(), "Thank you for your order!");
    }

}
