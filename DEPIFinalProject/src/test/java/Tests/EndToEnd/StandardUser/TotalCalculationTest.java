package Tests.EndToEnd.StandardUser;

import Tests.Base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class TotalCalculationTest extends TestBase {

    // Login as standard user ==> add multiple products to cart ==> check if product appears in cart ==> calculate total price and tax ==> complete checkout.


    String product1Title;
    String product1Price;

    String product2Title;
    String product2Price;

    String product3Title;
    String product3Price;

    double totalPrice;

    @Test(priority = 1)
    public void loginAsStandardUser() {

        loginUser("standard_user", "secret_sauce");

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("inventory"));
    }

    @Test(dependsOnMethods = {"loginAsStandardUser"})
    public void AddProduct1() {

        String productTitle = driver.findElement(By.xpath("//*[@id=\"item_5_title_link\"]/div")).getText();
        String productPrice = driver.findElement(By.xpath("//*[@id=\"inventory_container\"]/div/div[4]/div[2]/div[2]/div")).getText();

        WebElement productLink = driver.findElement(By.id("item_5_title_link"));
        productLink.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("inventory-item.html?id=5"));

        product1Title = driver.findElement(By.cssSelector("div.inventory_details_name.large_size")).getText();
        product1Price = driver.findElement(By.cssSelector("div.inventory_details_price")).getText();

        Assert.assertEquals(productTitle, product1Title);
        Assert.assertEquals(productPrice, product1Price);

        WebElement addToCartButton = driver.findElement(By.id("add-to-cart"));
        addToCartButton.click();

        WebElement removeButton = driver.findElement(By.id("remove"));
        Assert.assertTrue(removeButton.isDisplayed());

        WebElement cartBadge = driver.findElement(By.cssSelector("span.shopping_cart_badge"));

        Assert.assertTrue(cartBadge.isDisplayed());
        Assert.assertEquals(cartBadge.getText(), "1");
    }

    @Test(dependsOnMethods = {"AddProduct1"})
    public void AddProduct2() {

        WebElement backToProductsButton = driver.findElement(By.id("back-to-products"));
        backToProductsButton.click();

        String productTitle = driver.findElement(By.xpath("//*[@id=\"item_4_title_link\"]/div")).getText();
        String productPrice = driver.findElement(By.xpath("//*[@id=\"inventory_container\"]/div/div[1]/div[2]/div[2]/div")).getText();

        WebElement productLink = driver.findElement(By.id("item_4_title_link"));
        productLink.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("inventory-item.html?id=4"));

        product2Title = driver.findElement(By.cssSelector("div.inventory_details_name.large_size")).getText();
        product2Price = driver.findElement(By.cssSelector("div.inventory_details_price")).getText();

        Assert.assertEquals(productTitle, product2Title);
        Assert.assertEquals(productPrice, product2Price);

        WebElement addToCartButton = driver.findElement(By.id("add-to-cart"));
        addToCartButton.click();

        WebElement removeButton = driver.findElement(By.id("remove"));
        Assert.assertTrue(removeButton.isDisplayed());

        WebElement cartBadge = driver.findElement(By.cssSelector("span.shopping_cart_badge"));

        Assert.assertTrue(cartBadge.isDisplayed());
        Assert.assertEquals(cartBadge.getText(), "2");
    }

    @Test(dependsOnMethods = {"AddProduct2"})
    public void AddProduct3() {

        WebElement backToProductsButton = driver.findElement(By.id("back-to-products"));
        backToProductsButton.click();

        String productTitle = driver.findElement(By.xpath("//*[@id=\"item_1_title_link\"]/div")).getText();
        String productPrice = driver.findElement(By.xpath("//*[@id=\"inventory_container\"]/div/div[3]/div[2]/div[2]/div")).getText();

        WebElement productLink = driver.findElement(By.id("item_1_title_link"));
        productLink.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("inventory-item.html?id=1"));

        product3Title = driver.findElement(By.cssSelector("div.inventory_details_name.large_size")).getText();
        product3Price = driver.findElement(By.cssSelector("div.inventory_details_price")).getText();

        Assert.assertEquals(productTitle, product3Title);
        Assert.assertEquals(productPrice, product3Price);

        WebElement addToCartButton = driver.findElement(By.id("add-to-cart"));
        addToCartButton.click();

        WebElement removeButton = driver.findElement(By.id("remove"));
        Assert.assertTrue(removeButton.isDisplayed());

        WebElement cartBadge = driver.findElement(By.cssSelector("span.shopping_cart_badge"));

        Assert.assertTrue(cartBadge.isDisplayed());
        Assert.assertEquals(cartBadge.getText(), "3");
    }

    @Test(dependsOnMethods = {"AddProduct3"})
    public void verifyProductsAppearsInCart() {

        WebElement cartPage = driver.findElement(By.cssSelector("a.shopping_cart_link"));
        cartPage.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/cart"));

        String cartProduct1Title = driver.findElement(By.xpath("//*[@id=\"item_5_title_link\"]/div")).getText();
        String cartProduct1Price = driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[1]/div[3]/div[2]/div[2]/div")).getText();

        Assert.assertEquals(cartProduct1Title, product1Title);
        Assert.assertEquals(cartProduct1Price, product1Price);

        String cartProduct2Title = driver.findElement(By.xpath("//*[@id=\"item_4_title_link\"]/div")).getText();
        String cartProduct2Price = driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[1]/div[4]/div[2]/div[2]/div")).getText();

        Assert.assertEquals(cartProduct2Title, product2Title);
        Assert.assertEquals(cartProduct2Price, product2Price);

        String cartProduct3Title = driver.findElement(By.xpath("//*[@id=\"item_1_title_link\"]/div")).getText();
        String cartProduct3Price = driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[1]/div[5]/div[2]/div[2]/div")).getText();

        Assert.assertEquals(cartProduct3Title, product3Title);
        Assert.assertEquals(cartProduct3Price, product3Price);

        double product1 = Double.parseDouble(cartProduct1Price.replace("$", ""));
        double product2 = Double.parseDouble(cartProduct2Price.replace("$", ""));
        double product3 = Double.parseDouble(cartProduct3Price.replace("$", ""));

        totalPrice = product1 + product2 + product3;
    }

    @Test(dependsOnMethods = {"verifyProductsAppearsInCart"})
    public void checkTaxAndTotalPrice() {

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

        String totalText = driver.findElement(By.cssSelector("div.summary_subtotal_label")).getText();
        double total = Double.parseDouble(totalText.replaceAll("[^0-9.]", ""));

        Assert.assertEquals(totalPrice, total);

        String taxText = driver.findElement(By.cssSelector("div.summary_tax_label")).getText();
        double taxNumber = Double.parseDouble(taxText.replaceAll("[^0-9.]", ""));

        double tax = totalPrice * 0.08;
        double taxRounded = Math.round(tax * 100.0) / 100.0;

        Assert.assertEquals(totalPrice, total);
        Assert.assertEquals(taxRounded, taxNumber);
    }

    @Test(dependsOnMethods = {"checkTaxAndTotalPrice"})
    public void completeCheckout() {

        WebElement finishButton = driver.findElement(By.id("finish"));
        finishButton.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/checkout-complete"));

        WebElement successMessage = driver.findElement(By.className("complete-header"));
        Assert.assertEquals(successMessage.getText(), "Thank you for your order!");
    }
}
