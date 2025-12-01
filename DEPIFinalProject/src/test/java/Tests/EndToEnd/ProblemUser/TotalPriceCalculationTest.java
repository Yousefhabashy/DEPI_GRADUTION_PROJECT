package Tests.EndToEnd.ProblemUser;

import Tests.Base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class TotalPriceCalculationTest extends TestBase {
    // Login as problem user ==> add multiple products to cart ==> check if product appears in cart ==> calculate total price and tax ==> complete checkout.


    double totalPrice;

    @Test(priority = 1)
    public void loginAsProblemUser() {

        loginUser("problem_user", "secret_sauce");

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("inventory"));
    }

    @Test(dependsOnMethods = {"loginAsProblemUser"})
    public void AddProduct1() {

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory"));

        WebElement addToCartButton = driver.findElement(By.id("add-to-cart-sauce-labs-backpack"));
        addToCartButton.click();

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.id("remove-sauce-labs-backpack"))));
        WebElement removeButton = driver.findElement(By.id("remove-sauce-labs-backpack"));

        Assert.assertTrue(removeButton.isDisplayed());

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("span.shopping_cart_badge"))));
        WebElement cartBadge = driver.findElement(By.cssSelector("span.shopping_cart_badge"));

        Assert.assertTrue(cartBadge.isDisplayed());
        Assert.assertEquals(cartBadge.getText(), "1");
    }

    @Test(dependsOnMethods = {"AddProduct1"})
    public void AddProduct2() {

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory"));

        WebElement addToCartButton = driver.findElement(By.id("add-to-cart-sauce-labs-bike-light"));
        addToCartButton.click();

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.id("remove-sauce-labs-bike-light"))));
        WebElement removeButton = driver.findElement(By.id("remove-sauce-labs-bike-light"));

        Assert.assertTrue(removeButton.isDisplayed());

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("span.shopping_cart_badge"))));
        WebElement cartBadge = driver.findElement(By.cssSelector("span.shopping_cart_badge"));

        Assert.assertTrue(cartBadge.isDisplayed());
        Assert.assertEquals(cartBadge.getText(), "2");
    }

    @Test(dependsOnMethods = {"AddProduct2"})
    public void AddProduct3() {

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory"));

        WebElement addToCartButton = driver.findElement(By.id("add-to-cart-sauce-labs-onesie"));
        addToCartButton.click();

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.id("remove-sauce-labs-onesie"))));
        WebElement removeButton = driver.findElement(By.id("remove-sauce-labs-onesie"));

        Assert.assertTrue(removeButton.isDisplayed());

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("span.shopping_cart_badge"))));
        WebElement cartBadge = driver.findElement(By.cssSelector("span.shopping_cart_badge"));

        Assert.assertTrue(cartBadge.isDisplayed());
        Assert.assertEquals(cartBadge.getText(), "3");
    }

    @Test(dependsOnMethods = {"AddProduct3"})
    public void calculateTotalPrice() {

        WebElement cartPage = driver.findElement(By.cssSelector("a.shopping_cart_link"));
        cartPage.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/cart"));

        String cartProduct1Price = driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[1]/div[3]/div[2]/div[2]/div")).getText();

        String cartProduct2Price = driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[1]/div[4]/div[2]/div[2]/div")).getText();


        String cartProduct3Price = driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[1]/div[5]/div[2]/div[2]/div")).getText();


        double product1 = Double.parseDouble(cartProduct1Price.replace("$", ""));
        double product2 = Double.parseDouble(cartProduct2Price.replace("$", ""));
        double product3 = Double.parseDouble(cartProduct3Price.replace("$", ""));

        totalPrice = product1 + product2 + product3;
    }

    @Test(dependsOnMethods = {"calculateTotalPrice"})
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
