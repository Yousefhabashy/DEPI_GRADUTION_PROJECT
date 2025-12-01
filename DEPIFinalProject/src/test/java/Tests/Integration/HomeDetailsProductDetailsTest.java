package Tests.Integration;

import Tests.Base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class HomeDetailsProductDetailsTest extends TestBase {

    // Login as standard user ==> check if product details at home page matches the product page details.


    @Test(priority = 1)
    public void loginAsStandardUser() {

        loginUser("standard_user", "secret_sauce");

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("inventory"));
    }

    @Test(dependsOnMethods = {"loginAsStandardUser"})
    public void checkDetails() {

        WebElement productLink = driver.findElement(By.id("item_2_title_link"));

        String productTitle = driver.findElement(By.xpath("//*[@id=\"item_2_title_link\"]/div")).getText();
        String productPrice = driver.findElement(By.xpath("//*[@id=\"inventory_container\"]/div/div[5]/div[2]/div[1]/div")).getText();
        String productDesc = driver.findElement(By.xpath("//*[@id=\"inventory_container\"]/div/div[5]/div[2]/div[2]/div")).getText();

        productLink.click();

        String title = driver.findElement(By.cssSelector("div.inventory_details_name.large_size")).getText();
        String price = driver.findElement(By.cssSelector("div.inventory_details_desc.large_size")).getText();
        String desc = driver.findElement(By.cssSelector("div.inventory_details_price")).getText();

        Assert.assertEquals(title, productTitle);
        Assert.assertEquals(price, productPrice);
        Assert.assertEquals(desc, productDesc);
    }
}
