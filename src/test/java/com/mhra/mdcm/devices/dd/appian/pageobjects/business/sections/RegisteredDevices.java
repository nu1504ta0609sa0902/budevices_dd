package com.mhra.mdcm.devices.dd.appian.pageobjects.business.sections;

import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Created by TPD_Auto 
 */

public class RegisteredDevices extends _Page {

    @FindBy(xpath = ".//div[contains(text(),'Term')]//following::a")
    List<WebElement> listOfDevices;

    @FindBy(xpath = ".//div[contains(text(),'Term')]//following::a")
    WebElement aDevices;


    public RegisteredDevices(WebDriver driver) {
        super(driver);
    }


    public boolean isHeadingCorrect(String expectedHeadings) {
        WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//h1[.='" + expectedHeadings + "']") , TIMEOUT_15_SECOND, false);
        WebElement heading = driver.findElement(By.xpath(".//h1[.='" + expectedHeadings + "']"));
        boolean contains = heading.getText().contains(expectedHeadings);
        return contains;
    }


    public boolean isItemsDisplayed(String expectedHeadings) {
        WaitUtils.waitForElementToBeClickable(driver, aDevices , TIMEOUT_15_SECOND, false);
        boolean itemsDisplayed = listOfDevices.size() > 0;
        return itemsDisplayed;
    }
}
