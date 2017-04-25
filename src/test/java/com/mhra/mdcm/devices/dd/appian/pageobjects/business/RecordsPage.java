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
        WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText("Accounts"), TIMEOUT_DEFAULT, false);
        WaitUtils.waitForElementToBeClickable(driver, linkAccounts, TIMEOUT_DEFAULT, false);
        //linkAccounts.click();
        PageUtils.singleClick(driver, linkAccounts);
        return new Accounts(driver);
    }

    public GMDNDevices clickOnGMDNDevices() {
        WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText("GMDN Devices"), TIMEOUT_DEFAULT, false);
        WaitUtils.waitForElementToBeClickable(driver, linkGMDNDevices, TIMEOUT_DEFAULT, false);
        linkGMDNDevices.click();
        return new GMDNDevices(driver);
    }

    public RegisteredDevices clickOnRegisteredDevices() {
        WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText("Registered Devices"), TIMEOUT_DEFAULT, false);
        WaitUtils.waitForElementToBeClickable(driver, linkRegisteredDevices, TIMEOUT_DEFAULT, false);
        linkRegisteredDevices.click();
        return new RegisteredDevices(driver);
    }

    public RegisteredProducts clickOnRegisteredProducts() {
        WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText("Registered Products"), TIMEOUT_DEFAULT, false);
        WaitUtils.waitForElementToBeClickable(driver, linkRegisteredProducts, TIMEOUT_DEFAULT, false);
        linkRegisteredProducts.click();
        return new RegisteredProducts(driver);
    }

    public Organisations clickOnOrganisations() {
        WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText("Organisations"), TIMEOUT_DEFAULT, false);
        WaitUtils.waitForElementToBeClickable(driver, linkOrganisations, TIMEOUT_DEFAULT, false);
        linkOrganisations.click();
        return new Organisations(driver);
    }

}
