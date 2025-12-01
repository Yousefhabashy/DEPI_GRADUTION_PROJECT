package Tests.EndToEnd.StandardUser;

import Tests.Base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class RefreshPageWhilePurchasingTest extends TestBase {

    // Login as standard user ==> add product to cart ==> checkout product.
    // After any step refresh page


    String openedProductTitle;
    String openedProductPrice;

    public void fillInformation() {
        WebElement firstNameBox = driver.findElement(By.id("first-name"));
        WebElement lastNameBox = driver.findElement(By.id("last-name"));
        WebElement postalCode = driver.findElement(By.id("postal-code"));

        firstNameBox.sendKeys("Yousef");
        lastNameBox.sendKeys("Habashy");
        postalCode.sendKeys("32551");
    }

    public void driverRefresh(String urlContent) {
        driver.navigate().refresh();
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains(urlContent));
    }

    @Test(priority = 1)
    public void loginAsStandardUser() {

        loginUser("standard_user", "secret_sauce");

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("inventory"));

        driverRefresh("inventory");
    }

    @Test(dependsOnMethods = {"loginAsStandardUser"})
    public void openProductPage() {

        String productTitle = driver.findElement(By.xpath("//*[@id=\"item_5_title_link\"]/div")).getText();
        String productPrice = driver.findElement(By.xpath("//*[@id=\"inventory_container\"]/div/div[4]/div[2]/div[2]/div")).getText();

        WebElement productLink = driver.findElement(By.id("item_5_title_link"));
        productLink.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("inventory-item.html?id=5"));

        driverRefresh("inventory-item.html?id=5");

        openedProductTitle = driver.findElement(By.cssSelector("div.inventory_details_name.large_size")).getText();
        openedProductPrice = driver.findElement(By.cssSelector("div.inventory_details_price")).getText();

        Assert.assertEquals(productTitle, openedProductTitle);
        Assert.assertEquals(productPrice, openedProductPrice);
    }

    @Test(dependsOnMethods = {"openProductPage"})
    public void addProductToCart() {

        WebElement addToCartButton = driver.findElement(By.id("add-to-cart"));
        addToCartButton.click();

        WebElement removeButton = driver.findElement(By.id("remove"));
        Assert.assertTrue(removeButton.isDisplayed());

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("span.shopping_cart_badge"))));
        WebElement cartBadge = driver.findElement(By.cssSelector("span.shopping_cart_badge"));

        Assert.assertTrue(cartBadge.isDisplayed());
        Assert.assertEquals(cartBadge.getText(), "1");

        WebElement cartPage = driver.findElement(By.cssSelector("a.shopping_cart_link"));
        cartPage.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/cart"));

        driverRefresh("/cart");
    }

    @Test(dependsOnMethods = {"addProductToCart"})
    public void checkoutProduct() {
        String productTitle = driver.findElement(By.cssSelector("div.inventory_item_name")).getText();
        String productPrice = driver.findElement(By.cssSelector("div.inventory_item_price")).getText();

        Assert.assertEquals(productTitle, openedProductTitle);
        Assert.assertEquals(productPrice, openedProductPrice);

        WebElement checkoutButton = driver.findElement(By.id("checkout"));
        checkoutButton.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/checkout-step-one"));

        fillInformation();

        driverRefresh("/checkout-step-one");

        fillInformation();

        WebElement continueButton = driver.findElement(By.id("continue"));
        continueButton.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/checkout-step-two"));

        driverRefresh("/checkout-step-two");

        WebElement finishButton = driver.findElement(By.id("finish"));
        finishButton.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/checkout-complete"));

        driverRefresh("/checkout-complete");

        WebElement successMessage = driver.findElement(By.className("complete-header"));
        Assert.assertEquals(successMessage.getText(), "Thank you for your order!");
    }
}
