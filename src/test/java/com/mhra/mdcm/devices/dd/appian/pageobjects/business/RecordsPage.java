package com.mhra.mdcm.devices.dd.appian.pageobjects.business;

import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import com.mhra.mdcm.devices.dd.appian.pageobjects.business.sections.*;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.PageUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Created by TPD_Auto
 */

public class RecordsPage extends _Page {

    @FindBy(linkText = "Accounts")
    WebElement linkAccounts;
    @FindBy(linkText = "All Devices")
    WebElement linkAllDevices;
    @FindBy(linkText = "All Organisations")
    WebElement linkAllOrganisations;
    @FindBy(linkText = "All Products")
    WebElement linkAllProducts;
    @FindBy(linkText = "Devices")
    WebElement linkDevices;



    public RecordsPage(WebDriver driver) {
        super(driver);
    }

    public Accounts clickOnAccounts() {
        WaitUtils.waitForElementToBePartOfDOM(driver, By.partialLinkText("Accounts"), TIMEOUT_DEFAULT, false);
        WaitUtils.waitForElementToBeClickable(driver, linkAccounts, TIMEOUT_DEFAULT, false);
        //linkAccounts.click();
        PageUtils.singleClick(driver, linkAccounts);
        return new Accounts(driver);
    }

    public AllDevices clickOnAllDevices() {
        WaitUtils.waitForElementToBePartOfDOM(driver, By.partialLinkText("All Devices"), TIMEOUT_DEFAULT, false);
        WaitUtils.waitForElementToBeClickable(driver, linkAllDevices, TIMEOUT_DEFAULT, false);
        linkAllDevices.click();
        return new AllDevices(driver);
    }

    public Devices clickOnDevices() {
        WaitUtils.waitForElementToBePartOfDOM(driver, By.partialLinkText("Devices"), TIMEOUT_DEFAULT, false);
        WaitUtils.waitForElementToBeClickable(driver, linkDevices, TIMEOUT_DEFAULT, false);
        linkDevices.click();
        return new Devices(driver);
    }

    public AllProducts clickOnAllProducts() {
        WaitUtils.waitForElementToBePartOfDOM(driver, By.partialLinkText("Products"), TIMEOUT_DEFAULT, false);
        WaitUtils.waitForElementToBeClickable(driver, linkAllProducts, TIMEOUT_DEFAULT, false);
        linkAllProducts.click();
        return new AllProducts(driver);
    }

    public AllOrganisations clickOnAllOrganisations() {
        WaitUtils.waitForElementToBePartOfDOM(driver, By.partialLinkText("All Organisations"), TIMEOUT_DEFAULT, false);
        WaitUtils.waitForElementToBeClickable(driver, linkAllOrganisations, TIMEOUT_DEFAULT, false);
        linkAllOrganisations.click();
        return new AllOrganisations(driver);
    }

}
