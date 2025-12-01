package Tests.EndToEnd.ProblemUser;

import Tests.Base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class TaxCalculationTest extends TestBase {

    // Login as problem user ==> add product 1 ==> add product 2 ==> check if products appears in cart ==> calculate Tax ==> check if tax calculated correctly.

    String productTitle;
    String productPrice;

    double totalPrice;

    @Test(priority = 1)
    public void loginAsProblemUser() {

        loginUser("problem_user", "secret_sauce");

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory"));
    }

    @Test(dependsOnMethods = {"loginAsProblemUser"})
    public void addProductToCart() {

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory"));

        productTitle = driver.findElement(By.xpath("//*[@id=\"item_4_title_link\"]/div")).getText();
        productPrice = driver.findElement(By.xpath("//*[@id=\"inventory_container\"]/div/div[1]/div[2]/div[2]/div")).getText();

        double price = Double.parseDouble(productPrice.replace("$", ""));

        WebElement addToCartButton = driver.findElement(By.id("add-to-cart-sauce-labs-backpack"));
        addToCartButton.click();

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.id("remove-sauce-labs-backpack"))));
        WebElement removeButton = driver.findElement(By.id("remove-sauce-labs-backpack"));

        Assert.assertTrue(removeButton.isDisplayed());

        waitFor().until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("span.shopping_cart_badge"))));
        WebElement cartBadge = driver.findElement(By.cssSelector("span.shopping_cart_badge"));

        Assert.assertTrue(cartBadge.isDisplayed());
        Assert.assertEquals(cartBadge.getText(), "1");

        totalPrice = price;
    }

    @Test(dependsOnMethods = {"addProductToCart"})
    public void verifyProductsAppearsInCart() {

        WebElement cartPage = driver.findElement(By.cssSelector("a.shopping_cart_link"));
        cartPage.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/cart"));

        String cartProductTitle = driver.findElement(By.id("item_4_title_link")).getText();
        String cartProductPrice = driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[1]/div[3]/div[2]/div[2]/div")).getText();

        Assert.assertEquals(cartProductTitle, productTitle);
        Assert.assertEquals(cartProductPrice, productPrice);
    }


    @Test(dependsOnMethods = {"verifyProductsAppearsInCart"})
    public void checkoutStepOne() {

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
    }

    @Test(dependsOnMethods = {"checkoutStepOne"})
    public void checkTaxCalc() {

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/checkout-step-two"));

        String totalText = driver.findElement(By.cssSelector("div.summary_subtotal_label")).getText();
        double total = Double.parseDouble(totalText.replaceAll("[^0-9.]", ""));

        Assert.assertEquals(totalPrice, total);

        String taxText = driver.findElement(By.cssSelector("div.summary_tax_label")).getText();
        double taxNumber = Double.parseDouble(taxText.replaceAll("[^0-9.]", ""));

        double tax = totalPrice * 0.08;
        double taxRounded = Math.round(tax * 100.0) / 100.0;

        Assert.assertEquals(taxRounded, taxNumber);
    }

    @Test(dependsOnMethods = {"checkTaxCalc"})
    public void checkoutStepTwo() {

        WebElement finishButton = driver.findElement(By.id("finish"));
        finishButton.click();

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/checkout-complete"));

        WebElement successMessage = driver.findElement(By.className("complete-header"));
        Assert.assertEquals(successMessage.getText(), "Thank you for your order!");
    }
}
