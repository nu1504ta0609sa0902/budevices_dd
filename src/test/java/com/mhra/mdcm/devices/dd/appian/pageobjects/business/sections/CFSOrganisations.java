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

public class CFSOrganisations extends _Page {

    @FindBy(xpath = ".//div[contains(text(),'CFS Organisations ')]//following::a")
    List<WebElement> listOfCFSOrganisations;
    @FindBy(xpath = ".//table//th")
    List<WebElement> listOfTableColumns;

    //Search box
    @FindBy(xpath = ".//*[contains(@class, 'filter')]//following::input[1]")
    WebElement searchBox;


    public CFSOrganisations(WebDriver driver) {
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
        WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//div[contains(text(),'CFS Organisations ')]//following::a") , TIMEOUT_5_SECOND, false);
        boolean itemsDisplayed = listOfCFSOrganisations.size() > 0;
        return itemsDisplayed;
    }

}
