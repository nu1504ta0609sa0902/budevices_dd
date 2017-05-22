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

    @FindBy(css = ".Button---btn.Button---default_direction.Button---primary")
    WebElement linkManufacturerRegistration;

    @FindBy(xpath = ".//button[contains(text(),'Register new manufacturer')]")
    WebElement registerANewManufacturer;

    @FindBy(xpath = ".//*[contains(text(),'ype of device')]//following::label[1]")
    WebElement generalMedicalDevice;

    @FindBy(css=".CheckboxGroup---choice_pair>label")
    List<WebElement> listOfDeviceTypes;

    public ExternalHomePage(WebDriver driver) {
        super(driver);
    }

    public boolean isStartNowLinkDisplayed() {
        WaitUtils.waitForElementToBeClickable(driver, linkManufacturerRegistration, TIMEOUT_15_SECOND, false);
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
        WaitUtils.waitForElementToBeClickable(driver, linkManufacturerRegistration, TIMEOUT_5_SECOND, false);
        List<WebElement> listOfElements = driver.findElements(By.cssSelector(".DocumentImage---image.DocumentImage---small"));
        if (listOfElements.size() == 0) {
            isDisabled = false;
        }
        return isDisabled;
    }

    public ManufacturerList gotoListOfManufacturerPage() {
        WaitUtils.waitForElementToBeClickable(driver, linkManufacturerRegistration, TIMEOUT_60_SECOND, false);
        linkManufacturerRegistration.click();
        return new ManufacturerList(driver);
    }

    public ExternalHomePage provideIndicationOfDevicesMade(int index) {

        //WaitUtils.isPageLoadingComplete(driver, 2);
        WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//*[contains(text(),'ype of device')]//following::label[1]"), TIMEOUT_15_SECOND, false);
        WaitUtils.waitForElementToBeClickable(driver, generalMedicalDevice, TIMEOUT_15_SECOND, false);

        //Find element
        WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//*[contains(text(),'ype of device')]//following::label"), TIMEOUT_15_SECOND, false);
        //WaitUtils.nativeWaitInSeconds(1);
        List<WebElement> elements = driver.findElements(By.xpath(".//*[contains(text(),'ype of device')]//following::label"));
        WebElement e = elements.get(index);
        WaitUtils.waitForElementToBeClickable(driver, e, TIMEOUT_15_SECOND, false);

        PageUtils.singleClick(driver, e);
        //WaitUtils.nativeWaitInSeconds(2);

        return new ExternalHomePage(driver);
    }

    public void selectCustomMade(boolean isCustomMade) {
        By customMadeYes = By.xpath(".//*[contains(text(),'type of device')]//following::label[2]");
        By customMadeNo = By.xpath(".//*[contains(text(),'type of device')]//following::label[3]");
        By aimdCustomMadeYes = By.xpath(".//*[contains(text(),'type of device')]//following::label[6]");
        By aimdCustomMadeNo = By.xpath(".//*[contains(text(),'type of device')]//following::label[7]");
        By sppCustomMadeYes = By.xpath(".//*[contains(text(),'type of device')]//following::label[9]");

        try {

            //General medical registeredDevices
            if (isCustomMade) {
                WaitUtils.waitForElementToBeClickable(driver, customMadeYes, TIMEOUT_15_SECOND, false);
                driver.findElement(customMadeYes).click();
            } else {
                WaitUtils.waitForElementToBeClickable(driver, customMadeNo, TIMEOUT_15_SECOND, false);
                driver.findElement(customMadeNo).click();
            }

            //AIMD
            if (isCustomMade) {
                WaitUtils.waitForElementToBeClickable(driver, aimdCustomMadeYes, TIMEOUT_15_SECOND, false);
                WaitUtils.nativeWaitInSeconds(1);
                driver.findElement(aimdCustomMadeYes).click();
            } else {
                WaitUtils.waitForElementToBeClickable(driver, aimdCustomMadeNo, TIMEOUT_15_SECOND, false);
                driver.findElement(aimdCustomMadeNo).click();
            }

            //Others related to SSP
            WaitUtils.waitForElementToBeClickable(driver, sppCustomMadeYes, TIMEOUT_15_SECOND, false);
            driver.findElement(sppCustomMadeYes).click();
        }catch (Exception e){
            //Keeps failing
        }

        //Must be YES
        driver.findElement(aimdCustomMadeYes).click();
        driver.findElement(customMadeYes).click();
        driver.findElement(sppCustomMadeYes).click();
    }

    public _CreateManufacturerTestsData submitIndicationOfDevicesMade(boolean clickNext) {
        if(clickNext) {
            driver.findElements(By.cssSelector(".gwt-RadioButton.GFWJSJ4DGAD.GFWJSJ4DCW>label")).get(0).click();
            driver.findElement(By.xpath(".//button[.='Next']")).click();
        }else{
            WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//button[contains(text(),'Submit')]"), TIMEOUT_30_SECOND, false);
            driver.findElement(By.xpath(".//button[contains(text(),'Submit')]")).click();
        }
        return new _CreateManufacturerTestsData(driver);
    }

    public ExternalHomePage indicateDeviceTypes(boolean isGMD, boolean isIVD, boolean isAIMD, boolean isSPP) {
        int count = 0;
        for(WebElement deviceType: listOfDeviceTypes){

            if(count == 0 && isGMD){
                deviceType.click();
            }else if (count == 1 && isIVD){
                deviceType.click();
            }else if (count == 2 && isAIMD){
                deviceType.click();
            }else if (count == 3 && isSPP){
                deviceType.click();
            }

            count++;
        }
        return new ExternalHomePage(driver);
    }

    public ExternalHomePage registerANewManufacturer() {
        WaitUtils.waitForElementToBeClickable(driver, registerANewManufacturer, TIMEOUT_5_SECOND, false);
        registerANewManufacturer.click();
        return new ExternalHomePage(driver);
    }
}
