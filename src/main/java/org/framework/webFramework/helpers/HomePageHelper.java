package org.framework.webFramework.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePageHelper
{
    String accountOptionsCssSelector = "#nav-link-accountList-nav-line-1";
    String signInButtonCssSelector = ".nav-action-inner";
    String allMenuCssSelector = ".hm-icon-label";
    String seeAllCategoriesCssSelector = "a[aria-label='See All Categories']";
    String videoGameCategoryCssSelector = ".hmenu-item[data-menu-id='16']";
    String allVideoGamesXPath = "//a[normalize-space()='All Video Games']";

    public void goToAllVideoGames(WebDriver driver)
    {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(allMenuCssSelector)));
        driver.findElement(By.cssSelector(allMenuCssSelector)).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(seeAllCategoriesCssSelector)));
        driver.findElement(By.cssSelector(seeAllCategoriesCssSelector)).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(videoGameCategoryCssSelector)));
        driver.findElement(By.cssSelector(videoGameCategoryCssSelector)).click();
        new WebDriverWait(driver, Duration.ofSeconds(3));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement e = driver.findElement(By.xpath(allVideoGamesXPath));
        js.executeScript("arguments[0].click();", e);
    }

    public void goToSignIn(WebDriver driver)
    {
        WebElement elementToHover = driver.findElement(By.cssSelector(accountOptionsCssSelector));
        Actions actions = new Actions(driver);
        // Hover over the element
        actions.moveToElement(elementToHover).perform();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(signInButtonCssSelector)));
        driver.findElement(By.cssSelector(signInButtonCssSelector)).click();
    }

}
