package Tests.EndToEnd.StandardUser;

import Tests.Base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class CheckProductDetailsAllOverPagesTest extends TestBase {

    // Login as standard user ==> get product details  ==> check product details at product page  ==> check product details at cart page ==> check product details at checkout page.


    String expectedProductTitle;
    String expectedProductDesc;
    String expectedProductPrice;

    @Test(priority = 1)
    public void loginAsStandardUser() {

        loginUser("standard_user", "secret_sauce");

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory"));
    }

    @Test(dependsOnMethods = {"loginAsStandardUser"})
    public void getExpectedProductDetails() {

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory"));

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.id("item_1_title_link"))));

        expectedProductTitle = driver.findElement(By.id("item_1_title_link")).getText();
        expectedProductDesc = driver.findElement(By.xpath("//*[@id=\"inventory_container\"]/div/div[3]/div[2]/div[1]/div")).getText();
        expectedProductPrice = driver.findElement(By.xpath("//*[@id=\"inventory_container\"]/div/div[3]/div[2]/div[2]/div")).getText();

        WebElement productDetailsLink = driver.findElement(By.id("item_1_title_link"));
        productDetailsLink.click();

        Assert.assertTrue(driver.getCurrentUrl().contains("/inventory-item.html?id=1"));
    }

    @Test(dependsOnMethods = {"getExpectedProductDetails"})
    public void compareToProductDetailsPage() {

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("div.inventory_details_name.large_size"))));

        String productDetailsTitle = driver.findElement(By.cssSelector("div.inventory_details_name.large_size")).getText();
        String productDetailsDesc = driver.findElement(By.cssSelector("div.inventory_details_desc.large_size")).getText();
        String productDetailsPrice = driver.findElement(By.cssSelector("div.inventory_details_price")).getText();

        Assert.assertEquals(productDetailsTitle, expectedProductTitle);
        Assert.assertEquals(productDetailsDesc, expectedProductDesc);
        Assert.assertEquals(productDetailsPrice, expectedProductPrice);
    }

    @Test(dependsOnMethods = {"compareToProductDetailsPage"})
    public void addToCart() {

        WebElement addToCartButton = driver.findElement(By.id("add-to-cart"));
        addToCartButton.click();

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.id("remove"))));
        WebElement removeButton = driver.findElement(By.id("remove"));

        Assert.assertTrue(removeButton.isDisplayed());

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("span.shopping_cart_badge"))));
        WebElement cartBadge = driver.findElement(By.cssSelector("span.shopping_cart_badge"));

        Assert.assertTrue(cartBadge.isDisplayed());
        Assert.assertEquals(cartBadge.getText(), "1");

        WebElement cartPage = driver.findElement(By.cssSelector("a.shopping_cart_link"));
        cartPage.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/cart"));
    }

    @Test(dependsOnMethods = {"addToCart"})
    public void compareToCart() {

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.id("item_1_title_link"))));

        String cartTitle = driver.findElement(By.id("item_1_title_link")).getText();
        String cartDesc = driver.findElement(By.cssSelector("div.inventory_item_desc")).getText();
        String cartPrice = driver.findElement(By.cssSelector("div.inventory_item_price")).getText();

        Assert.assertEquals(cartTitle, expectedProductTitle);
        Assert.assertEquals(cartDesc, expectedProductDesc);
        Assert.assertEquals(cartPrice, expectedProductPrice);
    }

    @Test(dependsOnMethods = {"compareToCart"})
    public void proceedToCheckout() {

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
    }

    @Test(dependsOnMethods = {"proceedToCheckout"})
    public void compareToCheckoutFinish() {

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.id("item_1_title_link"))));

        String checkoutTitle = driver.findElement(By.id("item_1_title_link")).getText();
        String checkoutDesc = driver.findElement(By.cssSelector("div.inventory_item_desc")).getText();
        String checkoutPrice = driver.findElement(By.cssSelector("div.inventory_item_price")).getText();

        Assert.assertEquals(checkoutTitle, expectedProductTitle);
        Assert.assertEquals(checkoutDesc, expectedProductDesc);
        Assert.assertEquals(checkoutPrice, expectedProductPrice);
    }

    @Test(dependsOnMethods = {"compareToCheckoutFinish"})
    public void completeCheckout() {

        WebElement finishButton = driver.findElement(By.id("finish"));
        finishButton.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/checkout-complete"));

        WebElement successMessage = driver.findElement(By.className("complete-header"));
        Assert.assertEquals(successMessage.getText(), "Thank you for your order!");
    }
}
