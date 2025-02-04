package org.framework.webFramework.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ProductPageHelper
{
    String addToCartCssSelector = "input[id='add-to-cart-button']";
    String declineButtonCssSelector = "input[aria-labelledby='attachSiNoCoverage-announce']";
    public void addToCart(WebDriver driver)
    {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(addToCartCssSelector))));
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.cssSelector(addToCartCssSelector))));
        driver.findElement(By.cssSelector(addToCartCssSelector)).click();
    }

    public void additionalWarrantyDecline(WebDriver driver)
    {
        new WebDriverWait(driver, Duration.ofSeconds(3));
        driver.findElement(By.cssSelector(declineButtonCssSelector)).click();
    }

    public void goBackToCategory(WebDriver driver)
    {
        HomePageHelper homePageHelper = new HomePageHelper();
        homePageHelper.goToAllVideoGames(driver);
        CategoryPageHelper categoryPageHelper = new CategoryPageHelper();
        categoryPageHelper.freeShipping(driver);
        categoryPageHelper.newConditionItems(driver);
        categoryPageHelper.sortByPriceHighToLow(driver);
    }
}
