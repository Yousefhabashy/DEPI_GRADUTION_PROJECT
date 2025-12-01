package Tests.EndToEnd.StandardUser;

import Tests.Base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class CheckoutMultipleProductsTest extends TestBase {

    // Login as standard user ==> add product 1 ==> add product 2 ==> check if products appears in cart ==> complete checkout.

    String openedProduct1Title;
    String openedProduct1Price;

    String openedProduct2Title;
    String openedProduct2Price;

    @Test(priority = 1)
    public void loginAsStandardUser() {

        loginUser("standard_user", "secret_sauce");

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory"));
    }

    @Test(dependsOnMethods = {"loginAsStandardUser"})
    public void addProduct1() {

        String productTitle = driver.findElement(By.xpath("//*[@id=\"item_5_title_link\"]/div")).getText();
        String productPrice = driver.findElement(By.xpath("//*[@id=\"inventory_container\"]/div/div[4]/div[2]/div[2]/div")).getText();

        WebElement productLink = driver.findElement(By.id("item_5_title_link"));
        productLink.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("inventory-item.html?id=5"));

        openedProduct1Title = driver.findElement(By.cssSelector("div.inventory_details_name.large_size")).getText();
        openedProduct1Price = driver.findElement(By.cssSelector("div.inventory_details_price")).getText();

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

    @Test(dependsOnMethods = {"addProduct1"})
    public void addProduct2() {

        WebElement backToProductsButton = driver.findElement(By.id("back-to-products"));
        backToProductsButton.click();

        String productTitle = driver.findElement(By.xpath("//*[@id=\"item_4_title_link\"]/div")).getText();
        String productPrice = driver.findElement(By.xpath("//*[@id=\"inventory_container\"]/div/div[1]/div[2]/div[2]/div")).getText();

        WebElement productLink = driver.findElement(By.id("item_4_title_link"));
        productLink.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("inventory-item.html?id=4"));

        openedProduct2Title = driver.findElement(By.cssSelector("div.inventory_details_name.large_size")).getText();
        openedProduct2Price = driver.findElement(By.cssSelector("div.inventory_details_price")).getText();

        Assert.assertEquals(productTitle, openedProduct2Title);
        Assert.assertEquals(productPrice, openedProduct2Price);

        WebElement addToCartButton = driver.findElement(By.id("add-to-cart"));
        addToCartButton.click();

        WebElement removeButton = driver.findElement(By.id("remove"));
        Assert.assertTrue(removeButton.isDisplayed());

        WebElement cartBadge = driver.findElement(By.cssSelector("span.shopping_cart_badge"));

        Assert.assertTrue(cartBadge.isDisplayed());
        Assert.assertEquals(cartBadge.getText(), "2");
    }

    @Test(dependsOnMethods = {"addProduct2"})
    public void verifyProductsAppearsInCart() {

        WebElement cartPage = driver.findElement(By.cssSelector("a.shopping_cart_link"));
        cartPage.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/cart"));

        String cartProduct1Title = driver.findElement(By.xpath("//*[@id=\"item_5_title_link\"]/div")).getText();
        String cartProduct1Price = driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[1]/div[3]/div[2]/div[2]/div")).getText();

        Assert.assertEquals(cartProduct1Title, openedProduct1Title);
        Assert.assertEquals(cartProduct1Price, openedProduct1Price);

        String cartProduct2Title = driver.findElement(By.xpath("//*[@id=\"item_4_title_link\"]/div")).getText();
        String cartProduct2Price = driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[1]/div[4]/div[2]/div[2]/div")).getText();

        Assert.assertEquals(cartProduct2Title, openedProduct2Title);
        Assert.assertEquals(cartProduct2Price, openedProduct2Price);
    }

    @Test(dependsOnMethods = {"verifyProductsAppearsInCart"})
    public void completeCheckout() {

        WebElement checkoutButton = driver.findElement(By.id("checkout"));
        checkoutButton.click();

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
