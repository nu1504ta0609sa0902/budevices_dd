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


    public TasksPage(WebDriver driver) {
        super(driver);
    }

    public TaskSection clickOnTaskNumber(int count) {
        WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText("New Account Request"), TIMEOUT_DEFAULT, false);
        WebElement taskLink = listOfNewAccount.get(count);
        taskLink.click();
        return new TaskSection(driver);
    }

    public TaskSection clickOnTaskNumber(int count, String link) {
        WaitUtils.nativeWaitInSeconds(1);
        WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText("Work In Progress"), TIMEOUT_DEFAULT, false);
        //WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText(link), TIMEOUT_5_SECOND, false);
        try {
            if (link.contains("New Account")) {
                WebElement taskLink = listOfNewAccount.get(count);
                taskLink.click();
            } else if (link.contains("New Manufacturer")) {
                WebElement taskLink = listOfNewManufacturerRequest.get(count);
                taskLink.click();
            } else if (link.contains("New Service Request")) {
                WebElement taskLink = listOfNewServiceRequest.get(count);
                taskLink.click();
            } else if (link.contains("Update Manufacturer Reg")) {
                WebElement taskLink = listOfUpdateManufacturerRegRequest.get(count);
                taskLink.click();
            }
        }catch (Exception e){
            //No items meaning there is no task with specified link
        }
        return new TaskSection(driver);
    }

    public TaskSection clickOnLinkWithText(String orgName) {
        WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText(orgName), TIMEOUT_SMALL, false);
        WebElement taskLink = driver.findElement(By.partialLinkText(orgName));
        taskLink.click();
        log.info("Task found for : " + orgName);
        return new TaskSection(driver);
    }

    public boolean isLinkVisible(String link){
        WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
        boolean isVisible = true;
        try{
            WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText(link), TIMEOUT_SMALL, false);
        }catch (Exception e){
            isVisible = false;
        }
        return isVisible;
    }
}
