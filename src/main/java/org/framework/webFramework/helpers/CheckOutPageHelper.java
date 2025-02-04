package org.framework.webFramework.helpers;


import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import java.time.Duration;

public class CheckOutPageHelper
{
    String addressCssSelector = "h2[id='deliver-to-customer-text']";
    String paymentMethodCssSelector = "h2[id='payment-option-text-default'] span[class='break-word']";
    String totalPriceCssSelector = "span[class='a-list-item a-size-medium a-color-base a-text-bold'] div[class='order-summary-line-definition']";
    public void chooseAddress(WebDriver driver) throws InterruptedException {
        try {
            driver.findElement(By.cssSelector(addressCssSelector)).click();
        }
        catch (NoSuchElementException ex)
        {
            driver.findElement(By.cssSelector("input[data-csa-c-slot-id='checkout-secondary-continue-shipaddressselect']")).click();
        }
        Thread.sleep(2000);
    }

    public void assertOnTotalWithCOD(WebDriver driver, SoftAssert softAssert, Float totalPrice)
    {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(totalPriceCssSelector)));
        WebElement total = driver.findElement(By.cssSelector(totalPriceCssSelector));
        String totalString = total.getText().trim().replace(",","");
        totalPrice = totalPrice +12;
        softAssert.assertTrue(totalString.contains(String.valueOf(totalPrice)), "Total price +COD fees should be: "+ String.valueOf(totalPrice));
    }
}
