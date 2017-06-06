package com.mhra.mdcm.devices.dd.appian.pageobjects.business.sections;

import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import com.mhra.mdcm.devices.dd.appian.pageobjects.business.TasksPage;
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

public class TaskSection extends _Page {

    @FindBy(xpath = ".//h3")
    WebElement taskHeading;
    @FindBy(xpath = ".//a[contains(text(),'Organisation Details')]//following::p[1]")
    WebElement taskHeading2;

    @FindBy(xpath = ".//div[contains(text(),'Submitted')]")
    WebElement thSubmitted;

    //Accept taskSection
    @FindBy(xpath = ".//button[contains(text(), 'Accept')]")
    WebElement accept;
    @FindBy(xpath = ".//button[.='Go Back']")
    WebElement goBack;

    //Approve reject taskSection
    @FindBy(xpath = ".//button[contains(text(), 'Approve')]")
    WebElement approveNewAccount;
    @FindBy(xpath = ".//button[.='Accept Registration']")
    WebElement approve;
    @FindBy(xpath = ".//button[.='Reject Registration']")
    WebElement reject;

    //Application WIP page
    @FindBy(xpath = ".//*[text()='Urgency']/following::tr/td[1]")
    List<WebElement> listOfApplicationReferences;
    @FindBy(xpath = ".//*[contains(text(), 'Search by manufacturer')]/following::input[1]")
    WebElement tbxSearchByManufacturer;
    @FindBy(xpath = ".//button[text()='Search']")
    WebElement btnSearchForManufacuturer;
    @FindBy(xpath = ".//button[text()='Assign to myself']")
    WebElement btnAssignToMe;
    @FindBy(xpath = ".//button[text()='Yes']")
    WebElement btnConfirmYesAssignToMe;
    @FindBy(xpath = ".//button[text()='No']")
    WebElement btnConfirmNoAssignToMe;

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
        WaitUtils.waitForElementToBeVisible(driver, accept, TIMEOUT_15_SECOND, false);
        boolean contains = taskHeading.getText().contains(orgName);
        return contains;
    }
    public boolean isCorrectTask(String orgName, String taskType) {

        WebElement header = taskHeading2;
        if(taskType!=null && taskType.contains("New Account Request")){
            header = taskHeading;
        }

        try {
            //For new account
            WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
            WaitUtils.waitForElementToBeVisible(driver, header, TIMEOUT_5_SECOND, false);
            boolean contains = header.getText().contains(orgName);
            return contains;
        } catch (Exception e) {
            return false;
        }
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
        WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
        //WaitUtils.nativeWaitInSeconds(2);
        try {
            WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//*[contains(text(),'Device Selection')]//following::button[.='Approve']"), TIMEOUT_5_SECOND, false);
            PageUtils.doubleClick(driver, driver.findElement(By.xpath(".//*[contains(text(),'Device Selection')]//following::button[.='Approve']")));
        }catch (Exception e){
            try {
                WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//button[.='Approve']"), TIMEOUT_5_SECOND, false);
                PageUtils.doubleClick(driver, driver.findElement(By.xpath(".//button[.='Approve']")));
            }catch(Exception e2){
                WaitUtils.waitForElementToBeClickable(driver, approve, TIMEOUT_5_SECOND, false);
                PageUtils.doubleClick(driver, approve);
            }
        }
        //log.info("Task should be approved now");
        return new TasksPage(driver);
    }

    public TasksPage approveTaskNewAccount() {
        WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
        WaitUtils.waitForElementToBeClickable(driver, approveNewAccount, TIMEOUT_5_SECOND, false);
        PageUtils.doubleClick(driver, approveNewAccount);
        //log.info("Task should be approved now");
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
        WaitUtils.waitForElementToBeClickable(driver, reject, TIMEOUT_5_SECOND, false);
        //approve.click();
        PageUtils.doubleClick(driver, reject);
        return new TaskSection(driver);
    }

    public TasksPage enterRejectionReason(String reason, String randomTestComment) {
        if(reason.contains("Other")){
            WaitUtils.waitForElementToBeClickable(driver, other, TIMEOUT_15_SECOND, false);
            other.click();
            WaitUtils.waitForElementToBeClickable(driver, commentArea, TIMEOUT_15_SECOND, false);
            commentArea.sendKeys(randomTestComment);
        }

        //Submit rejection
        PageUtils.singleClick(driver, submitBtn);
        return new TasksPage(driver);
    }

    public TaskSection sortBy(String sortBy, int numberOfTimesToClick) {
        WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
        WaitUtils.waitForElementToBeClickable(driver, thSubmitted, TIMEOUT_5_SECOND, false);
        if (sortBy.equals("Submitted")) {
            for (int c = 0; c < numberOfTimesToClick; c++) {
                thSubmitted.click();
                WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
                WaitUtils.nativeWaitInSeconds(2);
            }
        }

        return new TaskSection(driver);
    }

    public TaskSection searchAWIPPageForAccount(String accountNameOrReference) {
        WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
        WaitUtils.waitForElementToBeClickable(driver, tbxSearchByManufacturer, TIMEOUT_10_SECOND);
        tbxSearchByManufacturer.sendKeys(accountNameOrReference);
        btnSearchForManufacuturer.click();
        listOfApplicationReferences.size();
        return new TaskSection(driver);
    }

    public TaskSection clickOnApplicationReferenceLink(String accountNameOrReference) {
        WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText(accountNameOrReference), TIMEOUT_5_SECOND, false);
        WebElement taskLink = driver.findElement(By.partialLinkText(accountNameOrReference));
        taskLink.click();
        log.info("Reference found for : " + accountNameOrReference);
        return new TaskSection(driver);
    }


    public TaskSection assignTaskToMe() {
        WaitUtils.waitForElementToBeClickable(driver, btnAssignToMe, TIMEOUT_10_SECOND);
        btnAssignToMe.click();
        return new TaskSection(driver);
    }

    public TaskSection confirmAssignment(boolean clickYes) {
        WaitUtils.waitForElementToBeClickable(driver, btnConfirmYesAssignToMe, TIMEOUT_10_SECOND);
        if(clickYes){
            btnConfirmYesAssignToMe.click();
        }else{
            btnConfirmNoAssignToMe.click();
        }
        return new TaskSection(driver);
    }
}
