package com.mhra.mdcm.devices.dd.appian.utils.selenium.page;

import com.google.common.base.Predicate;
import com.sun.istack.internal.Nullable;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

/**
 * @author TPD_Auto
 */
public class WaitUtils {

    //mhratest is very slow, set this to 0 once slow issue is fixed
    static int timeForTesting = 60;

    public static void waitForElementToBeClickable(WebDriver driver, WebElement element, int maxTimeToWait) {
        maxTimeToWait = resetMaxTime(maxTimeToWait);
        new WebDriverWait(driver, maxTimeToWait).until(ExpectedConditions.elementToBeClickable(element));
    }

    public static void waitForElementToBeClickable(WebDriver driver, By by, int maxTimeToWait) {
        maxTimeToWait = resetMaxTime(maxTimeToWait);
        new WebDriverWait(driver, maxTimeToWait).until(ExpectedConditions.elementToBeClickable(by));
    }

    public static void waitForElementToBeVisible(WebDriver driver, WebElement element, int maxTimeToWait) {
        maxTimeToWait = resetMaxTime(maxTimeToWait);
        new WebDriverWait(driver, maxTimeToWait).until(ExpectedConditions.visibilityOf(element));
    }

    public static void waitForElementToBeVisible(WebDriver driver, By by, int maxTimeToWait) {
        maxTimeToWait = resetMaxTime(maxTimeToWait);
        WebElement element = driver.findElement(by);
        new WebDriverWait(driver, maxTimeToWait).until(ExpectedConditions.visibilityOf(element));
    }

    private static int resetMaxTime(int maxTimeToWait) {
        if (timeForTesting > 0) {
            maxTimeToWait = timeForTesting;
        }
        return maxTimeToWait;
    }

    /**
     * DON'T USE FOR WAITING FOR PAGES, UNLESS ITS TO DO WITH SOME NATIVE COMPONENTS WHICH SELENIUM CAN'T HANDLE
     * <p>
     * Should be used for non selenium related tasks
     * <p>
     * Example when we upload a document
     *
     * @param tis
     */
    public static void nativeWait(int tis) {
        try {
            Thread.sleep(1000 * tis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param driver
     * @param by
     * @param maxTimeToWait
     * @param overrideTimeSpecified
     */
    public static void waitForElementToBeClickable(WebDriver driver, By by, int maxTimeToWait, boolean overrideTimeSpecified) {
        if (overrideTimeSpecified)
            maxTimeToWait = resetMaxTime(maxTimeToWait);
        new WebDriverWait(driver, maxTimeToWait).until(ExpectedConditions.elementToBeClickable(by));
    }


    /**
     * @param driver
     * @param element
     * @param maxTimeToWait
     * @param overrideTimeSpecified
     */
    public static void waitForElementToBeClickable(WebDriver driver, WebElement element, int maxTimeToWait, boolean overrideTimeSpecified) {
        if (overrideTimeSpecified)
            maxTimeToWait = resetMaxTime(maxTimeToWait);
        new WebDriverWait(driver, maxTimeToWait).until(ExpectedConditions.elementToBeClickable(element));
    }


    public static void waitForElementToBeVisible(WebDriver driver, WebElement element, int maxTimeToWait, boolean overrideTimeSpecified) {
        if (overrideTimeSpecified)
            maxTimeToWait = resetMaxTime(maxTimeToWait);
        new WebDriverWait(driver, maxTimeToWait).until(ExpectedConditions.visibilityOf(element));
    }


    public static void waitForElementToBeVisible(WebDriver driver, By by, int maxTimeToWait, boolean overrideTimeSpecified) {
        if (overrideTimeSpecified)
            maxTimeToWait = resetMaxTime(maxTimeToWait);
        WebElement element = driver.findElement(by);
        new WebDriverWait(driver, maxTimeToWait).until(ExpectedConditions.visibilityOf(element));
    }

    public static void waitForAlert(WebDriver driver, int maxTimeToWait, boolean overrideTimeSpecified) {
        if (overrideTimeSpecified)
            maxTimeToWait = resetMaxTime(maxTimeToWait);
        new WebDriverWait(driver, maxTimeToWait).until(ExpectedConditions.alertIsPresent());
    }

    public static void setImplicitWaits(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public static boolean isAlertPresent(WebDriver driver) {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }


    public static void isElementPartOfDom(WebDriver driver, By by, int maxTimeToWait, boolean overrideTimeSpecified) {
        if (overrideTimeSpecified)
            maxTimeToWait = resetMaxTime(maxTimeToWait);
        new WebDriverWait(driver, maxTimeToWait).until(ExpectedConditions.presenceOfElementLocated(by));
    }


    public static void isElementPartOfDomAdvanced(WebDriver driver, final WebElement element, int maxTimeToWait, boolean overrideTimeSpecified) {
        if (overrideTimeSpecified)
            maxTimeToWait = resetMaxTime(maxTimeToWait);

        new WebDriverWait(driver, maxTimeToWait)
                .ignoring(StaleElementReferenceException.class)
                .until(new Predicate<WebDriver>() {
                    @Override
                    public boolean apply(@Nullable WebDriver driver) {
                        boolean displayed = element.isDisplayed();
                        return displayed;
                    }
                });
    }


    /**
     * PREVENT StaleElementReference issue
     *
     * @param driver
     * @param by
     * @param maxTimeToWait
     * @param overrideTimeSpecified
     */
    public static void waitForElementToBePartOfDOM(WebDriver driver, final By by, int maxTimeToWait, boolean overrideTimeSpecified) {
        if (overrideTimeSpecified)
            maxTimeToWait = resetMaxTime(maxTimeToWait);

        new WebDriverWait(driver, maxTimeToWait)
                .ignoring(StaleElementReferenceException.class)
                .until(new Predicate<WebDriver>() {
                    @Override
                    public boolean apply(@Nullable WebDriver driver) {
                        WebElement element = driver.findElement(by);
                        boolean clickAble = element.isDisplayed() && element.isEnabled();
                        return clickAble;
                    }
                });
    }

    public static void waitForPageToLoad(WebDriver driver, By by, int maxTimeToWait, boolean overrideTimeSpecified) {
        try {
            new WebDriverWait(driver, maxTimeToWait).until(ExpectedConditions.presenceOfElementLocated(by));
        }catch (Exception e){
            //Aim is to pause the page for sometimes
        }
    }


    public static boolean isPageLoaded(WebDriver driver, By by, int maxTimeToWait, int numberOfTimes) {
        boolean loadingCompleted = false;
        int attempt = 0;
        do{
            try {
                new WebDriverWait(driver, maxTimeToWait).until(ExpectedConditions.presenceOfElementLocated(by));
                loadingCompleted = true;
                break;
            }catch (Exception e){
                //
            }
            attempt++;
        }while(!loadingCompleted && attempt < numberOfTimes);

        return loadingCompleted;
    }
}
