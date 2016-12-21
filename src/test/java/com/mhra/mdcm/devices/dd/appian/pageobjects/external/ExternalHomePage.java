package com.mhra.mdcm.devices.dd.appian.pageobjects.external;

import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import com.mhra.mdcm.devices.dd.appian.pageobjects.external.sections.ManufacturerList;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.CommonUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Created by TPD_Auto
 */

public class ExternalHomePage extends _Page {

    @FindBy(css = ".SafeImage.GFWJSJ4DOFB")
    WebElement linkManufacturerRegistration;

    public ExternalHomePage(WebDriver driver) {
        super(driver);
    }

    public boolean isStartNowLinkDisplayed() {
        WaitUtils.waitForElementToBeClickable(driver, linkManufacturerRegistration, TIMEOUT_MEDIUM, false);
        boolean visible = linkManufacturerRegistration.isDisplayed() && linkManufacturerRegistration.isEnabled();
        return visible;
    }

    public boolean areLinksVisible(String delimitedLinks) {
        boolean visible = CommonUtils.areLinksVisible(driver, delimitedLinks);
        return visible;
    }

    public boolean areLinksClickable(String delimitedLinks) {
        boolean clickable = CommonUtils.areLinksClickable(driver, delimitedLinks);
        return clickable;
    }

    public ManufacturerList gotoListOfManufacturerPage() {
        WaitUtils.waitForElementToBeClickable(driver, linkManufacturerRegistration, TIMEOUT_DEFAULT, false);
        linkManufacturerRegistration.click();
        return new ManufacturerList(driver);
    }
}
