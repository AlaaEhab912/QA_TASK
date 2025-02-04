package org.framework.webFrameworkTests;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.apache.commons.io.FileUtils;
import org.framework.utils.ConfigManager;
import org.framework.webFramework.constants.ConstantData;
import org.framework.webFramework.helpers.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;

public class TestAddToCart
{
    WebDriver driver;
    SoftAssert softAssert;
    LoginPageHelper loginPageHelper;
    HomePageHelper homePageHelper;
    CartHelper cartHelper;
    CategoryPageHelper categoryPageHelper;
    CheckOutPageHelper checkOutPageHelper;
    ProductPageHelper productPageHelper;
    String timeStamp;


    @BeforeSuite
    public void name(ITestContext context) {
        if (context.getCurrentXmlTest().getSuite().getName().equals("Default Suite")) {
            ConfigManager.setRealmId(ConfigManager.getDefaultRealm());
        } else {
            ConfigManager.setRealmId(context.getCurrentXmlTest().getSuite().getName());
        }
    }

    @BeforeClass
    public void beforeClassInitialization() {
        //Create a new folder in each run to save screenshots
        timeStamp = new SimpleDateFormat("(dd-MM-yyyy HH-mm-ss)").format(Calendar.getInstance().getTime());
        new File(".\\Screenshots\\AddToCart\\" + timeStamp).mkdir();
        loginPageHelper = new LoginPageHelper();
        homePageHelper = new HomePageHelper();
        cartHelper = new CartHelper();
        categoryPageHelper = new CategoryPageHelper();
        checkOutPageHelper = new CheckOutPageHelper();
        productPageHelper = new ProductPageHelper();

    }

    @BeforeMethod
    public void beforeMethodInitialization(){
        if (ConstantData.webBrowser.equals("chrome")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else if (ConstantData.webBrowser.equals("firefox")) {
            driver = new FirefoxDriver();
        }
        driver.get(ConstantData.webBaseUrl);
        softAssert = new SoftAssert();
        new WebDriverWait(driver, Duration.ofSeconds(5));
        homePageHelper.goToSignIn(driver);
        loginPageHelper.login(driver);
        homePageHelper.goToAllVideoGames(driver);
        categoryPageHelper.freeShipping(driver);
        categoryPageHelper.newConditionItems(driver);
        categoryPageHelper.sortByPriceHighToLow(driver);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate user can add product to cart and proceed to checkout successfully")
    public void validAddToCartAndProceed() throws InterruptedException {
        cartHelper = categoryPageHelper.addProductsToCart(driver);
        Thread.sleep(2000);
        cartHelper.goToCart(driver);
        Thread.sleep(2000);
        cartHelper.assertOnsuccessAdditionToCart(driver,softAssert);
        Thread.sleep(2000);
        cartHelper.proceedToBuy(driver);
        checkOutPageHelper.chooseAddress(driver);
        Thread.sleep(2000);
        checkOutPageHelper.assertOnTotalWithCOD(driver,softAssert, cartHelper.calculateTotalPrice(cartHelper.prices));
        softAssert.assertAll();
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethodTearDown(ITestResult result) throws InterruptedException {
        if (result.getStatus() == 1) {
            String path = ".\\Screenshots\\AddToCart\\" + timeStamp + "\\Passed\\" + result.getName() + " test screenshot.png";
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
            File destFile = new File(path);
            try {
                FileUtils.copyFile(srcFile, destFile);
                System.out.println("Screenshot saved to: " + destFile.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("Failed to save screenshot: " + e.getMessage());
            }

        } else {
            String path = ".\\Screenshots\\AddToCart\\" + timeStamp + "\\Failed\\" + result.getName() + " test screenshot.png";
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
            File destFile = new File(path);
            try {
                FileUtils.copyFile(srcFile, destFile);
                System.out.println("Screenshot saved to: " + destFile.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("Failed to save screenshot: " + e.getMessage());
            }
        }
        if (driver != null)
        {
            cartHelper.emptyCart(driver);
            driver.quit(); // Use quit() to close all browser windows
        }
    }
}
