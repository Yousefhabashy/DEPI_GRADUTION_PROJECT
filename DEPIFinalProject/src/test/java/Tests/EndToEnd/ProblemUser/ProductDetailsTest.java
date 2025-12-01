package Tests.EndToEnd.ProblemUser;

import Tests.Base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class ProductDetailsTest extends TestBase {

    // Login as problem user ==> check product home details matches the product page details.


    @Test(priority = 1)
    public void loginAsProblemUser() {

        loginUser("problem_user", "secret_sauce");

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/inventory"));
    }

    @Test(dependsOnMethods = {"loginAsProblemUser"})
    public void checkProductDetails() {

        WebElement productLink = driver.findElement(By.id("item_0_title_link"));

        String homeProductTitle = driver.findElement(By.cssSelector("div.inventory_item_name ")).getText();
        String homeProductPrice = driver.findElement(By.cssSelector("div.inventory_item_price")).getText();
        String homeProductDesc = driver.findElement(By.cssSelector("div.inventory_item_desc")).getText();

        productLink.click();

        String productTitle = driver.findElement(By.cssSelector("div.inventory_details_name.large_size")).getText();
        String productPrice = driver.findElement(By.cssSelector("div.inventory_details_price")).getText();
        String productDesc = driver.findElement(By.cssSelector("div.inventory_details_desc.large_size")).getText();

        Assert.assertEquals(homeProductTitle, productTitle);
        Assert.assertEquals(homeProductPrice, productPrice);
        Assert.assertEquals(homeProductDesc, productDesc);

    }

}
