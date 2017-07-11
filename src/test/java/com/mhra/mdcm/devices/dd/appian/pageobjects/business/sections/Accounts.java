package com.mhra.mdcm.devices.dd.appian.pageobjects.business.sections;

import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.RandomDataUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.AssertUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.PageUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TPD_Auto
 */

public class Accounts extends _Page {

    @FindBy(xpath = ".//div[.='Status']//following::a")
    List<WebElement> listOfAccounts;
    @FindBy(xpath = ".//table//th")
    List<WebElement> listOfTableColumns;

    //Edit information related to an account
    @FindBy(xpath = ".//button[contains(text(),'Edit Account')]")
    WebElement editAccountInfoLink;
    @FindBy(xpath = ".//span[.='Address type']//following::input[1]")
    WebElement addressType;
    @FindBy(xpath = ".//label[.='Job Title']//following::input[1]")
    WebElement jobTitle;
    @FindBy(xpath = ".//button[.='Submit']")
    WebElement submitBtn;

    //Updated information related to an account
    @FindBy(xpath = ".//span[.='Job title']//following::p[1]")
    WebElement jobTitleTxt;


    //Search box
    @FindBy(xpath = ".//*[contains(@class, 'filter')]//following::input[1]")
    WebElement searchBox;


    public Accounts(WebDriver driver) {
        super(driver);
    }


    public boolean isHeadingCorrect(String expectedHeadings) {
        WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
        WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//h1[.='" + expectedHeadings + "']") , TIMEOUT_10_SECOND, false);
        WebElement heading = driver.findElement(By.xpath(".//h1[.='" + expectedHeadings + "']"));
        boolean contains = heading.getText().contains(expectedHeadings);
        return contains;
    }


    public boolean isItemsDisplayed(String expectedHeadings) {
        WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
        WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//div[.='Status']//following::a") , TIMEOUT_10_SECOND, false);
        boolean itemsDisplayed = listOfAccounts.size() > 0;
        return itemsDisplayed;
    }

    public Accounts searchForAccount(String orgName) {
        WaitUtils.waitForElementToBeClickable(driver, searchBox, TIMEOUT_30_SECOND, false);
        searchBox.clear();
        searchBox.sendKeys(orgName);
        searchBox.sendKeys(Keys.ENTER);
        return new Accounts(driver);
    }

    /**
     * NOTE THERE MAY BE MORE THAN 1 LINK PER ROW
     * @return
     */
    public String getARandomAccount() {
        //WaitUtils.isPageLoadingComplete(driver, 1);
        WaitUtils.waitForElementToBeClickable(driver, By.linkText("Clear Filters"), TIMEOUT_30_SECOND, false);
        int position = RandomDataUtils.getSimpleRandomNumberBetween(1, listOfAccounts.size() - 1, false);
        WebElement accountLinks = listOfAccounts.get(position);
        String accountName = accountLinks.getText();
        log.info("Account name : " + accountName);
        return accountName;
    }

    public Accounts viewSpecifiedAccount(String randomAccountName) {
        WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText(randomAccountName), TIMEOUT_10_SECOND, false);
        WebElement accountLinks = driver.findElement(By.partialLinkText(randomAccountName));
        //accountLinks.click();
        PageUtils.doubleClick(driver, accountLinks);
        return new Accounts(driver);
    }

    public Accounts gotoEditAccountInformation() {
        WaitUtils.waitForElementToBeClickable(driver, editAccountInfoLink, TIMEOUT_10_SECOND, false);
        editAccountInfoLink.click();
        return new Accounts(driver);
    }

    public boolean isInEditMode() {
        boolean isInEditPage = true;
        try {
            WaitUtils.waitForElementToBeVisible(driver, submitBtn, TIMEOUT_15_SECOND, false);
        }catch (Exception e){
            isInEditPage = false;
        }
        return isInEditPage;
    }
}
