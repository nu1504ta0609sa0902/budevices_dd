package com.mhra.mdcm.devices.dd.appian.pageobjects.external;

import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import com.mhra.mdcm.devices.dd.appian.pageobjects.external.sections.ManufacturerList;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.CommonUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.PageUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Created by TPD_Auto
 */

public class ExternalHomePage extends _Page {

    @FindBy(css = ".SafeImage.GFWJSJ4DOFB")
    WebElement linkManufacturerRegistration;

    @FindBy(xpath = ".//*[contains(text(),'ype of device')]//following::input[1]")
    WebElement generalMedicalDevice;

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

    public boolean isGotoListOfManufacturerPageLinkDisabled() {
        boolean isDisabled = true;
        WaitUtils.isPageLoadingComplete(driver, 30);
        List<WebElement> listOfElements = driver.findElements(By.cssSelector(".SafeImage.GFWJSJ4DHFB.GFWJSJ4DOFB"));
        if (listOfElements.size() == 0) {
            isDisabled = false;
        }
        return isDisabled;
    }

    public ManufacturerList gotoListOfManufacturerPage() {
        WaitUtils.isPageLoadingComplete(driver, 10);
        WaitUtils.waitForElementToBeClickable(driver, linkManufacturerRegistration, TIMEOUT_VERY_HIGH, false);
        linkManufacturerRegistration.click();
        return new ManufacturerList(driver);
    }

    public ExternalHomePage provideIndicationOfDevicesMade(int index) {

        WaitUtils.isPageLoadingComplete(driver, 5);

        WaitUtils.waitForElementToBePartOfDOM(driver, By.xpath(".//*[contains(text(),'ype of device')]//following::input[1]"), TIMEOUT_MEDIUM, false);
        WaitUtils.waitForElementToBeClickable(driver, generalMedicalDevice, TIMEOUT_MEDIUM, false);

        //Find element
        WaitUtils.waitForElementToBePartOfDOM(driver, By.cssSelector(".GFWJSJ4DPV.GFWJSJ4DCAD input"), TIMEOUT_MEDIUM, false);
        WaitUtils.nativeWaitInSeconds(1);
        List<WebElement> elements = driver.findElements(By.cssSelector(".GFWJSJ4DPV.GFWJSJ4DCAD input"));
        WebElement e = elements.get(index);
        WaitUtils.waitForElementToBeClickable(driver, e, TIMEOUT_MEDIUM, false);

        PageUtils.doubleClick(driver, e);
        WaitUtils.nativeWaitInSeconds(2);

        return new ExternalHomePage(driver);
    }

    public void selectCustomMade(boolean isCustomMade) {
        By customMadeYes = By.xpath(".//*[contains(text(),'custom made')]//following::input[1]");
        By customMadeNo = By.xpath(".//*[contains(text(),'custom made)]//following::input[2]");
        if(isCustomMade) {
            WaitUtils.waitForElementToBeClickable(driver,customMadeYes , TIMEOUT_MEDIUM, false);
            driver.findElement(customMadeYes).click();
        }else{
            WaitUtils.waitForElementToBeClickable(driver, customMadeNo, TIMEOUT_MEDIUM, false);
            driver.findElement(customMadeNo).click();
        }
    }

    public ExternalHomePage submitIndicationOfDevicesMade(boolean clickNext) {
        if(clickNext) {
            driver.findElements(By.cssSelector(".gwt-RadioButton.GFWJSJ4DGAD.GFWJSJ4DCW>label")).get(0).click();
            driver.findElement(By.xpath(".//button[.='Next']")).click();
        }else{
            WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//button[.='Submit']"), TIMEOUT_HIGH, false);
            driver.findElement(By.xpath(".//button[.='Submit']")).click();
        }
        return new ExternalHomePage(driver);
    }
}
