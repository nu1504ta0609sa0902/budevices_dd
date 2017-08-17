package com.mhra.mdcm.devices.dd.appian.pageobjects.business;

import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import com.mhra.mdcm.devices.dd.appian.pageobjects.business.sections.TaskSection;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Created by TPD_Auto
 */

public class TasksPage extends _Page {

    @FindBy(partialLinkText = "New Service Request")
    List<WebElement> listOfNewServiceRequest;

    @FindBy(partialLinkText = "New Manufacturer Registration Request")
    List<WebElement> listOfNewManufacturerRequest;

    @FindBy(partialLinkText = "Update Manufacturer Registration Request")
    List<WebElement> listOfUpdateManufacturerRegRequest;

    @FindBy(partialLinkText = "New Account Request")
    List<WebElement> listOfNewAccount;

    @FindBy(xpath = ".//span[contains(text(),'Work in progress')]")
    WebElement applicationWorkInProgress;

    @FindBy(css = "div > table > tbody > tr")
    List<WebElement> listOfWIPTableRows;


    public TasksPage(WebDriver driver) {
        super(driver);
    }

    public TaskSection clickOnLinkWithText(String orgName) {
        WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText(orgName), TIMEOUT_5_SECOND, false);
        WebElement taskLink = driver.findElement(By.partialLinkText(orgName));
        taskLink.click();

        return new TaskSection(driver);
    }

    public boolean isApplicationWIPTableDisplayingData() {
        //WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
        WaitUtils.waitForElementToBeClickable(driver, By.cssSelector("div > table > tbody > tr"), TIMEOUT_15_SECOND, false);

        //Verify its not the No items available message
        if(listOfWIPTableRows.size() == 1){
            boolean noItems = listOfWIPTableRows.get(0).getText().contains("No items");
            return !noItems;
        }else {
            return listOfWIPTableRows.size() > 0;
        }
    }

    public TaskSection gotoApplicationWIPPage() {
        WaitUtils.waitForElementToBeVisible(driver, applicationWorkInProgress, TIMEOUT_30_SECOND, false);
        applicationWorkInProgress.click();
        return new TaskSection(driver);
    }
}
