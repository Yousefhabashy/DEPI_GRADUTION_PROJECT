package Tests.EndToEnd.StandardUser;

import Tests.Base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class SuccessfulPurchaseFlowTest extends TestBase {

    // Login as standard user ==> add product to cart from product details page  ==> check if product appears in cart ==> checkout product ==> logout user.



    String openedProductTitle;
    String openedProductPrice;

    @Test(priority = 1)
    public void loginAsStandardUser() {

        loginUser("standard_user", "secret_sauce");

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory"));
    }

    @Test(dependsOnMethods = {"loginAsStandardUser"})
    public void addProductToCart() {

        String productTitle = driver.findElement(By.xpath("//*[@id=\"item_5_title_link\"]/div")).getText();
        String productPrice = driver.findElement(By.xpath("//*[@id=\"inventory_container\"]/div/div[4]/div[2]/div[2]/div")).getText();

        WebElement productLink = driver.findElement(By.id("item_5_title_link"));
        productLink.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("inventory-item.html?id=5"));

        openedProductTitle = driver.findElement(By.cssSelector("div.inventory_details_name.large_size")).getText();
        openedProductPrice = driver.findElement(By.cssSelector("div.inventory_details_price")).getText();

        Assert.assertEquals(productTitle, openedProductTitle);
        Assert.assertEquals(productPrice, openedProductPrice);

        WebElement addToCartButton = driver.findElement(By.id("add-to-cart"));
        addToCartButton.click();

        WebElement removeButton = driver.findElement(By.id("remove"));
        Assert.assertTrue(removeButton.isDisplayed());

        WebElement cartBadge = driver.findElement(By.cssSelector("span.shopping_cart_badge"));

        Assert.assertTrue(cartBadge.isDisplayed());
        Assert.assertEquals(cartBadge.getText(), "1");
    }

    @Test(dependsOnMethods = {"addProductToCart"})
    public void verifyProductAppearsInCart() {

        WebElement cartPage = driver.findElement(By.cssSelector("a.shopping_cart_link"));
        cartPage.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/cart"));

        String cartProduct1Title = driver.findElement(By.xpath("//*[@id=\"item_5_title_link\"]/div")).getText();
        String cartProduct1Price = driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[1]/div[3]/div[2]/div[2]/div")).getText();

        Assert.assertEquals(cartProduct1Title, openedProductTitle);
        Assert.assertEquals(cartProduct1Price, openedProductPrice);
    }

    @Test(dependsOnMethods = {"verifyProductAppearsInCart"})
    public void checkoutProduct() {
        String productTitle = driver.findElement(By.cssSelector("div.inventory_item_name")).getText();
        String productPrice = driver.findElement(By.cssSelector("div.inventory_item_price")).getText();

        Assert.assertEquals(productTitle, openedProductTitle);
        Assert.assertEquals(productPrice, openedProductPrice);

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

    @Test(dependsOnMethods = {"checkoutProduct"})
    public void logoutUser() {

        WebElement sideBarButton = driver.findElement(By.id("react-burger-menu-btn"));
        sideBarButton.click();

        waitFor().until(ExpectedConditions.visibilityOfElementLocated(By.id("logout_sidebar_link")));

        WebElement logout = driver.findElement(By.id("logout_sidebar_link"));
        WebElement resetAppState = driver.findElement(By.id("reset_sidebar_link"));

        resetAppState.click();
        logout.click();

        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/");

    }
}

