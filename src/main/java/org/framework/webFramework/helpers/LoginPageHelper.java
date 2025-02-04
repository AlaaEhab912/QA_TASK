package org.framework.webFramework.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.framework.webFramework.constants.ConstantData.*;
public class LoginPageHelper
{
    String emailInputFieldCssSelector = "input[class='a-input-text a-span12 auth-autofocus auth-required-field auth-require-claim-validation']";
    String continueButtonLocatorXPath = "//input[@id='continue']";
    String passwordInputFieldCssSelector = "input[class='a-input-text a-span12 auth-autofocus auth-required-field']";
    String signInButtonCssSelector = "#signInSubmit";

    public void login(WebDriver driver){
        new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.findElement(By.cssSelector(emailInputFieldCssSelector)).sendKeys(email);
        driver.findElement(By.xpath(continueButtonLocatorXPath)).click();
        new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.findElement(By.cssSelector(passwordInputFieldCssSelector)).sendKeys(password);
        driver.findElement(By.cssSelector(signInButtonCssSelector)).click();
    }
}
