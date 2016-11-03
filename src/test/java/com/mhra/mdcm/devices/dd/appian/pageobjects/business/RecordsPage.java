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

    @FindBy(partialLinkText = "Accounts")
    WebElement linkAccounts;
    @FindBy(partialLinkText = "Devices")
    WebElement linkDevices;
    @FindBy(partialLinkText = "Products")
    WebElement linkProducts;
    @FindBy(partialLinkText = "All Organisations")
    WebElement linkAllOrganisations;


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

    public Devices clickOnDevices() {
        WaitUtils.waitForElementToBePartOfDOM(driver, By.partialLinkText("Devices"), 10, false);
        WaitUtils.waitForElementToBeClickable(driver, linkDevices, 10, false);
        linkDevices.click();
        return new Devices(driver);
    }

    public Products clickOnProducts() {
        WaitUtils.waitForElementToBePartOfDOM(driver, By.partialLinkText("Products"), 20, false);
        WaitUtils.waitForElementToBeClickable(driver, linkProducts, 10, false);
        linkProducts.click();
        return new Products(driver);
    }

    public AllOrganisations clickOnAllOrganisations() {
        WaitUtils.waitForElementToBePartOfDOM(driver, By.partialLinkText("All Organisations"), 10, false);
        WaitUtils.waitForElementToBeClickable(driver, linkAllOrganisations, 10, false);
        linkAllOrganisations.click();
        return new AllOrganisations(driver);
    }

}
