package Tests.Integration;

import Tests.Base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ProductsFilterTest extends TestBase {

    @Test(priority = 1)
    public void loginAsStandardUser() {

        loginUser("standard_user", "secret_sauce");

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("inventory"));
    }

    @Test(dependsOnMethods = {"loginAsStandardUser"})
    public void filterByNameATOZ() {

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("inventory"));

        WebElement filter = driver.findElement(By.className("product_sort_container"));
        Select select = new Select(filter);
        select.selectByVisibleText("Name (A to Z)");

        List<WebElement> products = driver.findElements(By.cssSelector("div.inventory_item_name "));

        List<String> actualNames = new ArrayList<>();
        for (WebElement product : products) {
            actualNames.add(product.getText());
        }

        List<String> expectedNames = new ArrayList<>(actualNames);
        Collections.sort(expectedNames);

        Assert.assertEquals(actualNames, expectedNames);


    }

    @Test(dependsOnMethods = {"filterByNameATOZ"})
    public void filterByNameZTOA() {

        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("inventory"));

        WebElement filter = driver.findElement(By.className("product_sort_container"));
        Select select = new Select(filter);
        select.selectByVisibleText("Name (Z to A)");

        List<WebElement> productsNames = driver.findElements(By.cssSelector("div.inventory_item_name "));

        List<String> actualNames = new ArrayList<>();
        for (WebElement product : productsNames) {
            actualNames.add(product.getText());
        }

        List<String> expectedNames = new ArrayList<>(actualNames);
        Collections.sort(expectedNames);
        Collections.reverse(expectedNames);

        Assert.assertEquals(actualNames, expectedNames);
    }


    @Test(dependsOnMethods = {"filterByNameZTOA"})
    public void filterByPriceLowToHigh() {
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("inventory"));

        WebElement filter = driver.findElement(By.className("product_sort_container"));
        Select select = new Select(filter);
        select.selectByVisibleText("Price (low to high)");

        List<WebElement> productPrices = driver.findElements(By.cssSelector("div.inventory_item_price"));

        List<Double> actualPrices = new ArrayList<>();
        for (WebElement price : productPrices) {

            String text = price.getText().trim().replace("$", "");
            actualPrices.add(Double.parseDouble(text));
        }

        List<Double> expectedPrices = new ArrayList<>(actualPrices);
        Collections.sort(expectedPrices);

        Assert.assertEquals(actualPrices, expectedPrices);
    }

    @Test(dependsOnMethods = {"filterByPriceLowToHigh"})
    public void filterByPriceHighToLow() {
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("inventory"));

        WebElement filter = driver.findElement(By.className("product_sort_container"));
        Select select = new Select(filter);
        select.selectByVisibleText("Price (high to low)");

        List<WebElement> productPrices = driver.findElements(By.cssSelector("div.inventory_item_price"));

        List<Double> actualPrices = new ArrayList<>();
        for (WebElement price : productPrices) {

            String text = price.getText().trim().replace("$", "");
            actualPrices.add(Double.parseDouble(text));
        }

        List<Double> expectedPrices = new ArrayList<>(actualPrices);
        Collections.sort(expectedPrices);
        Collections.reverse(expectedPrices);

        Assert.assertEquals(actualPrices, expectedPrices);
    }
}
