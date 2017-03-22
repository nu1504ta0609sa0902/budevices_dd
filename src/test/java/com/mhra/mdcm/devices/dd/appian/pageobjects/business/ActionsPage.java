package com.mhra.mdcm.devices.dd.appian.pageobjects.business;


import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import com.mhra.mdcm.devices.dd.appian.pageobjects.business.sections.CreateTestsData;
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


    public ActionsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isInActionsPage() {
        try {
            WaitUtils.waitForElementToBeClickable(driver, linkCreateTestAccount, TIMEOUT_DEFAULT, false);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public CreateTestsData gotoTestsHarnessPage() {
        WaitUtils.isPageLoadingComplete(driver, 10);
        WaitUtils.nativeWaitInSeconds(2);
        WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText("Create Test Account"), TIMEOUT_MEDIUM, false);
        WaitUtils.waitForElementToBeClickable(driver, linkCreateTestAccount, TIMEOUT_MEDIUM, false);
        //linkCreateTestAccount.click();
        PageUtils.singleClick(driver, linkCreateTestAccount);
        return new CreateTestsData(driver);
    }
}
