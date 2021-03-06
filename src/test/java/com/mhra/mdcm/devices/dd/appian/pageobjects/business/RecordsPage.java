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
    @FindBy(linkText = "Applications")
    WebElement linkApplications;
    @FindBy(linkText = "CFS Services")
    WebElement linkCFSServices;
    @FindBy(linkText = "GMDN Devices")
    WebElement linkGMDNDevices;
    @FindBy(linkText = "Organisations")
    WebElement linkOrganisations;
    @FindBy(linkText = "Registered Products")
    WebElement linkRegisteredProducts;
    @FindBy(linkText = "Registered Devices")
    WebElement linkRegisteredDevices;



    public RecordsPage(WebDriver driver) {
        super(driver);
    }

    public Accounts clickOnAccounts() {
        WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText("Accounts"), TIMEOUT_10_SECOND, false);
        WaitUtils.waitForElementToBeClickable(driver, linkAccounts, TIMEOUT_10_SECOND, false);
        //linkAccounts.click();
        PageUtils.singleClick(driver, linkAccounts);
        return new Accounts(driver);
    }

    public GMDNDevices clickOnGMDNDevices() {
        WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText("GMDN Devices"), TIMEOUT_10_SECOND, false);
        WaitUtils.waitForElementToBeClickable(driver, linkGMDNDevices, TIMEOUT_10_SECOND, false);
        linkGMDNDevices.click();
        return new GMDNDevices(driver);
    }

    public RegisteredDevices clickOnRegisteredDevices() {
        WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText("Registered Devices"), TIMEOUT_10_SECOND, false);
        WaitUtils.waitForElementToBeClickable(driver, linkRegisteredDevices, TIMEOUT_10_SECOND, false);
        linkRegisteredDevices.click();
        return new RegisteredDevices(driver);
    }

    public RegisteredProducts clickOnRegisteredProducts() {
        WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText("Registered Products"), TIMEOUT_10_SECOND, false);
        WaitUtils.waitForElementToBeClickable(driver, linkRegisteredProducts, TIMEOUT_10_SECOND, false);
        linkRegisteredProducts.click();
        return new RegisteredProducts(driver);
    }

    public Organisations clickOnOrganisations() {
        WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText("Organisations"), TIMEOUT_10_SECOND, false);
        WaitUtils.waitForElementToBeClickable(driver, linkOrganisations, TIMEOUT_10_SECOND, false);
        linkOrganisations.click();
        return new Organisations(driver);
    }

    public Applications clickOnApplications() {
        WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText("Applications"), TIMEOUT_10_SECOND, false);
        WaitUtils.waitForElementToBeClickable(driver, linkApplications, TIMEOUT_10_SECOND, false);
        linkApplications.click();
        return new Applications(driver);
    }

    public CFSServices clickOnCFSServices() {
        WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText("CFS Services"), TIMEOUT_10_SECOND, false);
        WaitUtils.waitForElementToBeClickable(driver, linkCFSServices, TIMEOUT_10_SECOND, false);
        linkCFSServices.click();
        return new CFSServices(driver);
    }
}
