package org.framework.webFramework.helpers;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CartHelper {
    public List<String> prices = new ArrayList<>();
    String productListCssSelector = "div[data-name='Active Items']";
    String itemsCountCssSelector = "span[id='sc-subtotal-label-activecart']";
    String totalPriceCssSelector = "span[class='a-size-medium a-color-base sc-price sc-white-space-nowrap']";
    String proceedToBuyButtonCssSelector = "input[value='Proceed to checkout']";
    String cartButtonCssSelector = "span[class='nav-cart-icon nav-sprite']";
    String checkoutCartCssSelector = "span[id='nav-checkout-cart-icon']";
    String deleteItemCssSelector = "span[data-a-selector='decrement-icon']";
    String cartTableCssSelector = "div[data-name='Active Items']";

    public void assertOnsuccessAdditionToCart(WebDriver driver, SoftAssert softAssert) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(productListCssSelector)));
        WebElement itemsCount = driver.findElement(By.cssSelector(itemsCountCssSelector));
        softAssert.assertTrue(itemsCount.getText().trim().contains(String.valueOf(prices.size())),
                "items count should be: " + prices.size());
        WebElement totalPrice = driver.findElement(By.cssSelector(totalPriceCssSelector));
        Float total = calculateTotalPrice(prices);
        String totalPriceString = totalPrice.getText().trim().replace(",", "");
        softAssert.assertTrue(totalPriceString.contains(String.valueOf(total)),
                "items total price should be: " + total);
    }

    public void proceedToBuy(WebDriver driver) {
        new WebDriverWait(driver, Duration.ofSeconds(2));
        WebElement proceedButton = driver.findElement(By.cssSelector(proceedToBuyButtonCssSelector));
        proceedButton.click();
    }

    public Float calculateTotalPrice(List<String> prices) {
        Float total =  0.0F;
        for (String price : prices) {
            total = Float.parseFloat(price) + total;
        }
        return total;
    }

    public void goToCart(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        WebElement cart = driver.findElement(By.cssSelector(cartButtonCssSelector));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", cart);
        wait.until(ExpectedConditions.visibilityOf(cart));
        wait.until(ExpectedConditions.elementToBeClickable(cart));
        js.executeScript("arguments[0].click();", cart);
    }

    public void emptyCart(WebDriver driver) throws InterruptedException {
        WebElement cart = driver.findElement(By.cssSelector(checkoutCartCssSelector));
        cart.click();
        Thread.sleep(500);
        WebElement table = driver.findElement(By.cssSelector(cartTableCssSelector));
        List<WebElement> deleteButtons = table.findElements(By.cssSelector(deleteItemCssSelector));
        while (!deleteButtons.isEmpty())
        {
            try {
                Thread.sleep(500);
                deleteButtons.get(0).click();
                Thread.sleep(500);
                table = driver.findElement(By.cssSelector(cartTableCssSelector));
                Thread.sleep(500);
                deleteButtons = table.findElements(By.cssSelector(deleteItemCssSelector));
            }
            catch (NoSuchElementException e)
            {
                continue;
            }
        }
    }
}
