package com.mhra.mdcm.devices.dd.appian.pageobjects.business;

import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import com.mhra.mdcm.devices.dd.appian.pageobjects.business.sections.Accounts;
import com.mhra.mdcm.devices.dd.appian.pageobjects.business.sections.AllOrganisations;
import com.mhra.mdcm.devices.dd.appian.pageobjects.business.sections.Devices;
import com.mhra.mdcm.devices.dd.appian.pageobjects.business.sections.Products;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.PageUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;

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


    @Autowired
    public RecordsPage(WebDriver driver) {
        super(driver);
    }

    public Accounts clickOnAccounts() {
        WaitUtils.waitForElementToBePartOfDOM(driver, By.partialLinkText("Accounts"), 10, false);
        WaitUtils.waitForElementToBeClickable(driver, linkAccounts, 10, false);
        //linkAccounts.click();
        PageUtils.singleClick(driver, linkAccounts);
        return new Accounts(driver);
    }

    public Devices clickOnAllDevices() {
        WaitUtils.waitForElementToBePartOfDOM(driver, By.partialLinkText("All Devices"), 10, false);
        WaitUtils.waitForElementToBeClickable(driver, linkAllDevices, 10, false);
        linkAllDevices.click();
        return new Devices(driver);
    }

    public Devices clickOnDevices() {
        WaitUtils.waitForElementToBePartOfDOM(driver, By.partialLinkText("Devices"), 10, false);
        WaitUtils.waitForElementToBeClickable(driver, linkDevices, 10, false);
        linkDevices.click();
        return new Devices(driver);
    }

    public Products clickOnAllProducts() {
        WaitUtils.waitForElementToBePartOfDOM(driver, By.partialLinkText("Products"), 20, false);
        WaitUtils.waitForElementToBeClickable(driver, linkAllProducts, 10, false);
        linkAllProducts.click();
        return new Products(driver);
    }

    public AllOrganisations clickOnAllOrganisations() {
        WaitUtils.waitForElementToBePartOfDOM(driver, By.partialLinkText("All Organisations"), 10, false);
        WaitUtils.waitForElementToBeClickable(driver, linkAllOrganisations, 10, false);
        linkAllOrganisations.click();
        return new AllOrganisations(driver);
    }

}
