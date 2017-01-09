package com.mhra.mdcm.devices.dd.appian.pageobjects.business.sections;

import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import com.mhra.mdcm.devices.dd.appian.pageobjects.business.TasksPage;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.PageUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Created by TPD_Auto
 */

public class TaskSection extends _Page {

    @FindBy(xpath = ".//h4")
    WebElement taskHeading;

    //Accept taskSection
    @FindBy(xpath = ".//button[.='Accept']")
    WebElement accept;
    @FindBy(xpath = ".//button[.='Go Back']")
    WebElement goBack;

    //Approve reject taskSection
    @FindBy(xpath = ".//button[.='Accept Registration']")
    WebElement approve;
    @FindBy(xpath = ".//button[.='Reject Registration']")
    WebElement reject;

    //Rejection reason
    @FindBy(xpath = ".//label[.='Other']")
    WebElement other;
    @FindBy(xpath = ".//textarea[1]")
    WebElement commentArea;
    @FindBy(xpath = ".//button[.='Submit']")
    WebElement submitBtn;



    public TaskSection(WebDriver driver) {
        super(driver);
    }

    public boolean isCorrectTask(String orgName) {
        WaitUtils.nativeWaitInSeconds(4);
        WaitUtils.waitForElementToBeVisible(driver, By.xpath(".//h4"), TIMEOUT_MEDIUM, false);
        boolean contains = taskHeading.getText().contains(orgName);
        return contains;
    }

    public TaskSection acceptTask() {
        try {
            WaitUtils.waitForElementToBeVisible(driver, accept, 5, false);
            WaitUtils.waitForElementToBeClickable(driver, accept, 5, false);
            if (accept.isDisplayed()) {
                PageUtils.doubleClick(driver, accept);
            }
        } catch (Exception e) {
            log.info("Task Already Accepted ");
        }
        return new TaskSection(driver);
    }

    public TasksPage approveTask() {
        try {
            WaitUtils.nativeWaitInSeconds(2);
            WaitUtils.waitForElementToBeClickable(driver, approve, TIMEOUT_SMALL, false);
            //approve.click();
            PageUtils.doubleClick(driver, approve);
        }catch (Exception e){
            try {
                WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//button[.='Approve']"), TIMEOUT_SMALL, false);
                PageUtils.doubleClick(driver, driver.findElement(By.xpath(".//button[.='Approve']")));
            }catch(Exception e2){
                WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//*[contains(text(),'Device Selection')]//following::button[.='Approve']"), TIMEOUT_SMALL, false);
                PageUtils.doubleClick(driver, driver.findElement(By.xpath(".//*[contains(text(),'Device Selection')]//following::button[.='Approve']")));
            }
        }
        return new TasksPage(driver);
    }

    /**
     * Rejecting a taskSection requires user to verify a reason
     *
     * So work flow is different
     * @return
     */
    public TaskSection rejectTask() {
        WaitUtils.nativeWaitInSeconds(2);
        WaitUtils.waitForElementToBeClickable(driver, reject, TIMEOUT_SMALL, false);
        //approve.click();
        PageUtils.doubleClick(driver, reject);
        return new TaskSection(driver);
    }

    public TasksPage enterRejectionReason(String reason, String randomTestComment) {
        if(reason.contains("Other")){
            WaitUtils.waitForElementToBeClickable(driver, other, TIMEOUT_MEDIUM, false);
            other.click();
            WaitUtils.waitForElementToBeClickable(driver, commentArea, TIMEOUT_MEDIUM, false);
            commentArea.sendKeys(randomTestComment);
        }

        //Submit rejection
        PageUtils.singleClick(driver, submitBtn);
        return new TasksPage(driver);
    }

}
