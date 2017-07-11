package com.mhra.mdcm.devices.dd.appian.pageobjects.business;


import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.PageUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Created by TPD_Auto
 */

public class ActionsPage extends _Page {

    @FindBy(partialLinkText = "Create Test Account")
    WebElement linkCreateTestAccount;

    @FindBy(xpath = ".//h3[contains(text(), 'Application complete')]")
    WebElement txtApplicationComplete;
    @FindBy(xpath = ".//h3[contains(text(), 'Application complete')]/following::h4[1]")
    WebElement txtApplicationReference;


    public ActionsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isInActionsPage() {
        try {
            WaitUtils.waitForElementToBeClickable(driver, linkCreateTestAccount, TIMEOUT_10_SECOND, false);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isApplicationSubmittedSuccessfully() {
        //WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
        try {
            WaitUtils.waitForElementToBeClickable(driver, txtApplicationComplete, TIMEOUT_15_SECOND);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public _CreateTestsData gotoTestsHarnessPage() {
        //WaitUtils.isPageLoadingComplete(driver, 10);
        WaitUtils.nativeWaitInSeconds(2);
        WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText("Create Test Account"), TIMEOUT_15_SECOND, false);
        WaitUtils.waitForElementToBeClickable(driver, linkCreateTestAccount, TIMEOUT_15_SECOND, false);
        //linkCreateTestAccount.click();
        PageUtils.singleClick(driver, linkCreateTestAccount);
        return new _CreateTestsData(driver);
    }

    public String getApplicationReferenceNumber() {
        return txtApplicationReference.getText();
    }
}
