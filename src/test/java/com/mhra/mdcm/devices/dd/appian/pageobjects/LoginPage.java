package com.mhra.mdcm.devices.dd.appian.pageobjects;

import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.PageUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author TPD_Auto
 */

public class LoginPage extends _Page {

    @FindBy(id = "un")
    WebElement username;
    @FindBy(id = "pw")
    WebElement password;
    @FindBy(css = "input#remember")
    WebElement remember;
    @FindBy(css = ".gwt-Anchor.pull-down-toggle")
    WebElement settings;
    @FindBy(css = ".settings-pull-down .gwt-Anchor.pull-down-toggle")
    WebElement loggedInUsername;

    @FindBy(xpath = ".//label[@for='remember']//following::input[1]")
    WebElement loginBtn;

    @FindBy(xpath = ".//span[contains(@style, 'personalization')]")
    WebElement photoIcon;
    @FindBy(xpath = "//*[contains(text(),'Sign Out')]")
    WebElement signOutLink;


    //Error message
    @FindBy(xpath = ".//img[@id='logo']//following::div[@class='message']")
    WebElement errorMsg;


    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage loadPage(String url) {
        WaitUtils.nativeWait(2);
        driver.get(url);
        return new LoginPage(driver);
    }

    public MainNavigationBar loginAs(String usernameTxt, String passwordTxt) {
        logoutIfLoggedIn();
        dontRemember();

        //login
        username.sendKeys(usernameTxt);
        password.sendKeys(passwordTxt);
        username.submit();

        return new MainNavigationBar(driver);
    }

    public void dontRemember() {
        WaitUtils.waitForElementToBeClickable(driver, remember, TIMEOUT_DEFAULT, false);
        if (remember.getAttribute("checked") != null) {
            remember.click();
        }
    }

    /**
     * Business site
     *
     * @return
     */
    public LoginPage logoutIfLoggedIn() {
        try {
            WaitUtils.waitForElementToBeClickable(driver, settings, 10, false);
            if (settings.isDisplayed()) {
                //settings.click();
                PageUtils.doubleClick(driver, settings);
                driver.findElement(By.linkText("Sign Out")).click();
                WaitUtils.waitForElementToBeClickable(driver, remember, 10, false);
                WaitUtils.nativeWait(2);
            }
        } catch (Exception e) {
            //Probably not logged in
        }
        return new LoginPage(driver);
    }

    /**
     * logout from Manufacturer and AuthorisedRep site
     *
     * @return
     */
    public LoginPage logoutIfLoggedInOthers() {
        try {
            WaitUtils.waitForElementToBeClickable(driver, photoIcon, 10, false);
            if (photoIcon.isDisplayed()) {
                //settings.click();
                PageUtils.doubleClick(driver, photoIcon);
                signOutLink.click();
                WaitUtils.waitForElementToBeClickable(driver, remember, 10, false);

                //If logout and login is too fast, appian system shows 404 in some instance of automation
                WaitUtils.nativeWait(2);
            }
        } catch (Exception e) {
            //Probably not logged in
        }
        return new LoginPage(driver);
    }

    public boolean isErrorMessageCorrect(String expectedErrorMsg) {
        WaitUtils.waitForElementToBeVisible(driver, errorMsg, 10, false);
        boolean contains = errorMsg.getText().contains(expectedErrorMsg);
        return contains;
    }

    public boolean isInLoginPage() {
        //WaitUtils.waitForElementToBeClickable(driver, loginBtn, 10, false);
        boolean isLoginPage = loginBtn.isDisplayed() && loginBtn.isEnabled();
        return isLoginPage;
    }
}
