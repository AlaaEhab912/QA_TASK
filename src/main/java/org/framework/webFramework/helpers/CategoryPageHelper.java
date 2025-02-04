package org.framework.webFramework.helpers;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class CategoryPageHelper {
    String searchResultTableXPath = "/html/body/div[1]/div[1]/div[1]/div[1]/div/span[1]";
    String productsListCssSelector = "div[role='listitem']";
    String freeShippingCheckBoxCssSelector = "label[for='apb-browse-refinements-checkbox_0'] i[class='a-icon a-icon-checkbox']";
    String newConditionXPath = "//span[@class='a-size-base a-color-base'][normalize-space()='New']";
    String sortByCssSelector = "#a-autoid-0-announce";
    String highToLowPriceSortCssSelector = "#s-result-sort-select_2";
    String productCssSelector = "div[data-cel-widget^='search_result_']"; //from 1 to size -2
    String productMainPriceCssSelector = "span[class='a-price-whole']"; //to be accessed from product element
    String productNameCssSelector = "a[class='a-link-normal s-line-clamp-2 s-link-style a-text-normal']"; //to be accessed from product element
    String addToCartButtonCssSelector = "button[name='submit.addToCart']"; //to be accessed from product element
    String productAttachedPriceCssSelector = "span[class='a-color-base']"; // if productMainPrice doesn't exist, try to access this
    String extraOffersForProductCssSelector = "a[class='a-link-normal s-link-style s-underline-text s-underline-link-text']"; //if productMainPrice doesn't exist and productAttachedPrice within range then will click on this to add to cart
    String offerProductCssSelector = "div[id='aod-offer']";
    String offerAddToCartButtonCssSelector = "input[name='submit.addToCart']"; //to be accessed from product element

    public void freeShipping(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        WebElement freeShippingCheckBox = driver.findElement(By.cssSelector(freeShippingCheckBoxCssSelector));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", freeShippingCheckBox);
        wait.until(ExpectedConditions.visibilityOf(freeShippingCheckBox));
        wait.until(ExpectedConditions.elementToBeClickable(freeShippingCheckBox));
        if (!freeShippingCheckBox.isSelected()) {
            // If it's not selected, click it to select
            js.executeScript("arguments[0].click();", freeShippingCheckBox);
        }
    }

    public void newConditionItems(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        WebElement newCondition = driver.findElement(By.xpath(newConditionXPath));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", newCondition);
        wait.until(ExpectedConditions.visibilityOf(newCondition));
        wait.until(ExpectedConditions.elementToBeClickable(newCondition));
        js.executeScript("arguments[0].click();", newCondition);
    }

    public void sortByPriceHighToLow(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(sortByCssSelector)));
        driver.findElement(By.cssSelector(sortByCssSelector)).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(highToLowPriceSortCssSelector)));
        driver.findElement(By.cssSelector(highToLowPriceSortCssSelector)).click();
    }

    public CartHelper addProductsToCart(WebDriver driver) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(productCssSelector)));
        // Find all the product items
        WebElement table = driver.findElement(By.xpath(searchResultTableXPath));
        List<WebElement> productsList = table.findElements(By.cssSelector(productsListCssSelector));
        CartHelper cartHelper = new CartHelper();

        for (int i = 0; i < productsList.size() ; i++) {
            try {
                Thread.sleep(500);
                // Extract the product price
                WebElement productPrice = productsList.get(i).findElement(By.cssSelector(productMainPriceCssSelector));
                String productPriceString = productPrice.getText().trim().replace(",","");
                if (Float.parseFloat(productPriceString) < 15000) {
                    try {
                        Thread.sleep(1000);
                        if(productsList.get(i).findElement(By.cssSelector(addToCartButtonCssSelector)).isDisplayed() &&
                                productsList.get(i).findElement(By.cssSelector(addToCartButtonCssSelector)).isEnabled())
                        {
                            Thread.sleep(500);
                            productsList.get(i).findElement(By.cssSelector(addToCartButtonCssSelector)).click();
                            //if yes then add to cart and add to price and products list
                            cartHelper.prices.add(productPriceString);
                            //cartHelper.productsNames.add(productsList.get(i).findElement(By.cssSelector(productNameCssSelector)).getText());
                        }
                    }
                    catch (NoSuchElementException e)
                    {
                        Thread.sleep(1000);
                        productsList.get(i).findElement(By.cssSelector(productNameCssSelector)).click();
                        Thread.sleep(3000);
                        ProductPageHelper productPageHelper = new ProductPageHelper();
                        productPageHelper.addToCart(driver);
                        Thread.sleep(1000);
                        productPageHelper.additionalWarrantyDecline(driver);
                        cartHelper.prices.add(productPriceString);
                        Thread.sleep(500);
                        productPageHelper.goBackToCategory(driver);
                        Thread.sleep(500);
                        table = driver.findElement(By.xpath(searchResultTableXPath));
                        Thread.sleep(500);
                        productsList = table.findElements(By.cssSelector(productsListCssSelector));
                    }
                }

            }
            catch (NoSuchElementException e) {
                try {
                    //check additional price
                    WebElement productAdditionalPrice = productsList.get(i).findElement(By.cssSelector(productAttachedPriceCssSelector));
                    if (productAdditionalPrice != null && !productAdditionalPrice.getText().trim().isEmpty()&& productAdditionalPrice.getText().trim().contains("EGP")) {
                        String productPriceString = productAdditionalPrice.getText().trim().replace(",","");
                        productPriceString = productPriceString.replace(".00","");
                        productPriceString = productPriceString.replace("EGP ","");
                        if (Float.parseFloat(productPriceString) < 15000) {
                            //if yes then click on offers
                            WebElement offers = productsList.get(i).findElement(By.cssSelector(extraOffersForProductCssSelector));
                            offers.click();
                            Thread.sleep(4000);
                            List<WebElement> offersProductsList = driver.findElements(By.cssSelector(offerProductCssSelector));
                            if (offersProductsList.size() == 1) {
                                Thread.sleep(3000);
                                WebElement offerPrice = offersProductsList.get(0).findElement(By.cssSelector(productMainPriceCssSelector));
                                //check if price is less than 15k
                                productPriceString = offerPrice.getText().trim().replace(",","");
                                if (Integer.parseInt(productPriceString) < 15000) {
                                    new WebDriverWait(driver, Duration.ofSeconds(5));
                                    Thread.sleep(1000);
                                    offersProductsList.get(0).findElement(By.cssSelector(offerAddToCartButtonCssSelector)).click();
                                    cartHelper.prices.add(productPriceString);
                                    ProductPageHelper productPageHelper = new ProductPageHelper();
                                    productPageHelper.goBackToCategory(driver);
                                    Thread.sleep(500);
                                    table = driver.findElement(By.xpath(searchResultTableXPath));
                                    Thread.sleep(500);
                                    productsList = table.findElements(By.cssSelector(productsListCssSelector));
                                }
                            }
                            else {
                                for (int x = 1; x < offersProductsList.size(); x++) {
                                    WebElement offerPrice = offersProductsList.get(x).findElement(By.cssSelector(productMainPriceCssSelector));
                                    //check if price is less than 15k
                                    productPriceString = offerPrice.getText().trim().replace(",","");
                                    if (Float.parseFloat(productPriceString) < 15000) {
                                        Thread.sleep(3000);
                                        //if yes then add to cart and add to price and products list and then break
                                        offersProductsList.get(x).findElement(By.cssSelector(offerAddToCartButtonCssSelector)).click();
                                        cartHelper.prices.add(productPriceString);
                                        Thread.sleep(3000);
                                        ProductPageHelper productPageHelper = new ProductPageHelper();
                                        productPageHelper.goBackToCategory(driver);
                                        Thread.sleep(500);
                                        table = driver.findElement(By.xpath(searchResultTableXPath));
                                        Thread.sleep(500);
                                        productsList = table.findElements(By.cssSelector(productsListCssSelector));
                                        break;
                                    }
                                }
                            }
                        }

                    }
                }
                catch (NoSuchElementException ex)
                {
                    continue;
                }
            }
        }
        return cartHelper;
    }
}
