package com.mhra.mdcm.devices.dd.appian.pageobjects;


import com.mhra.mdcm.devices.dd.appian.pageobjects.business.*;
import com.mhra.mdcm.devices.dd.appian.pageobjects.external.PortalPage;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.PageUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author TPD_Auto
 */

public class MainNavigationBar extends _Page {

    @FindBy(partialLinkText = "News")
    WebElement news;
    @FindBy(partialLinkText = "Tasks")
    WebElement tasks;
    @FindBy(partialLinkText = "Records")
    WebElement records;
    @FindBy(partialLinkText = "Reports")
    WebElement reports;
    @FindBy(partialLinkText = "Actions")
    WebElement actions;


    @FindBy(xpath = ".//a//following::div[.='PORTAL']")
    WebElement linkPortal;
    @FindBy(xpath = ".//a//following::div[.='MY ACCOUNT']")
    WebElement linkMyAccount;

    @FindBy(css = ".appian-menu-item.appian-menu-item-selected")
    WebElement currentSelection;



    public MainNavigationBar(WebDriver driver) {
        super(driver);
    }

    //==========================================================
    //
    // BUSINESS NAVIGATION BAR
    //
    //==========================================================


    public NewsPage clickNews() {
        WaitUtils.waitForElementToBeClickable(driver, news, TIMEOUT_DEFAULT, false);
        PageUtils.doubleClick(driver, news);
        return new NewsPage(driver);
    }

    public TasksPage clickTasks() {
        WaitUtils.waitForElementToBeClickable(driver, tasks, TIMEOUT_DEFAULT, false);
        tasks.click();
        PageUtils.doubleClick(driver, tasks);
        return new TasksPage(driver);
    }

    public RecordsPage clickRecords() {
        WaitUtils.isPageLoaded(driver, By.partialLinkText("Records"), TIMEOUT_VERY_SMALL, 3);
        WaitUtils.waitForElementToBeClickable(driver, records, TIMEOUT_DEFAULT, false);
        //records.click();
        PageUtils.doubleClick(driver, records);
        return new RecordsPage(driver);
    }

    public ReportsPage clickReports() {
        WaitUtils.waitForElementToBeClickable(driver, reports, TIMEOUT_DEFAULT, false);
        reports.click();
        PageUtils.doubleClick(driver, reports);
        return new ReportsPage(driver);
    }

    public String getCurrentSelectedMenu() {
        WaitUtils.waitForElementToBeClickable(driver, currentSelection, TIMEOUT_DEFAULT, false);
        String selectedMenu = currentSelection.getText();
        return selectedMenu;
    }

    public ActionsPage clickActions() {
        WaitUtils.waitForElementToBeClickable(driver, actions, TIMEOUT_DEFAULT, false);
        actions.click();
        PageUtils.doubleClick(driver, actions);
        return new ActionsPage(driver);
    }

    public boolean isCorrectPage(String expectedHeading) {
        WaitUtils.isPageLoaded(driver, By.partialLinkText(expectedHeading),TIMEOUT_SMALL, 3);
        String title = getTitle();
        boolean isCorrectPage = title.contains(expectedHeading);
        return isCorrectPage;
    }




    //==========================================================
    //
    // MUNUFACTURER AND AUTHORISEDREP NAVIGATION BAR
    //
    //==========================================================


    public PortalPage clickPortals() {
        try {
            WaitUtils.waitForElementToBeClickable(driver, linkPortal, TIMEOUT_DEFAULT, false);
            PageUtils.doubleClick(driver, linkPortal);
        }catch (Exception e){
            By xp = By.xpath(".//a//following::div[.='Portal']");
            WaitUtils.waitForElementToBeClickable(driver, xp, TIMEOUT_DEFAULT, false);
            PageUtils.doubleClick(driver, driver.findElement(xp));
        }
        return new PortalPage(driver);
    }
}
