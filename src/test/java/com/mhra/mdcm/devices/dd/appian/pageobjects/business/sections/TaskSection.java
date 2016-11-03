package com.mhra.mdcm.devices.dd.appian.pageobjects.business.sections;

import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import com.mhra.mdcm.devices.dd.appian.pageobjects.business.TasksPage;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.PageUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by TPD_Auto
 */
@Component
public class TaskSection extends _Page {

    @FindBy(xpath = ".//h4")
    WebElement taskHeading;

    //Accept taskSection
    @FindBy(xpath = ".//button[.='Accept']")
    WebElement accept;
    @FindBy(xpath = ".//button[.='Go Back']")
    WebElement goBack;

    //Approve reject taskSection
    @FindBy(xpath = ".//button[.='Approve']")
    WebElement approve;
    @FindBy(xpath = ".//button[.='Reject']")
    WebElement reject;

    //Rejection reason
    @FindBy(xpath = ".//label[.='Other']")
    WebElement other;
    @FindBy(xpath = ".//textarea[1]")
    WebElement commentArea;
    @FindBy(xpath = ".//button[.='Submit']")
    WebElement submitBtn;


    @Autowired
    public TaskSection(WebDriver driver) {
        super(driver);
    }

    public boolean isCorrectTask(String orgName) {
        WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText("Reassign Task"), TIMEOUT_MEDIUM, false);
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
        WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText("Reassign Task"), 5, false);
        WaitUtils.waitForElementToBeClickable(driver, approve, TIMEOUT_SMALL, false);
        //approve.click();
        PageUtils.doubleClick(driver, approve);
        return new TasksPage(driver);
    }

    /**
     * Rejecting a taskSection requires user to verify a reason
     *
     * So work flow is different
     * @return
     */
    public TaskSection rejectTask() {
        WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText("Reassign Task"), 5, false);
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
