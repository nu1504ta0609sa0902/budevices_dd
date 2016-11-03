package com.mhra.mdcm.devices.dd.appian.pageobjects;

import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.FileUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.PageUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Properties;

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

    @Autowired
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage loadPage(String url) {
        WaitUtils.nativeWait(2);
        driver.get(url);
        return new LoginPage(driver);
    }

//    public MainNavigationBar login(String unameKeyValue) {
//        dontRemember();
//
//        //get login details
//        String selectedProfile = System.getProperty("spring.profiles.active");
//        String overrideWithUsername = System.getProperty("test.as.user");
//        Properties props = FileUtils.loadPropertiesFile("data" + File.separator + "users.properties");
//
//        //check if local properties supplies a
//        if(overrideWithUsername==null || overrideWithUsername.equals("")) {
//            overrideWithUsername = props.getProperty("mhratest.user.default.override.username");
//        }
//
//        //Override local uname with overriden values
//        unameKeyValue = FileUtils.getOverriddenUsername(overrideWithUsername, unameKeyValue);
//
//        //Now load the correct username password
//        String un = props.getProperty(selectedProfile + ".username." + unameKeyValue);
//        String pword = props.getProperty(selectedProfile + ".password." + unameKeyValue);
//
//        if(un == null || pword == null){
//            props = FileUtils.loadPropertiesFile("data" + File.separator + "other.configs.properties");
//            un = props.getProperty(selectedProfile + ".username." + unameKeyValue);
//            pword = props.getProperty(selectedProfile + ".password." + unameKeyValue);
//        }
//
//        //login
//        username.sendKeys(un);
//        password.sendKeys(pword);
//        username.submit();
//
//        return new MainNavigationBar(driver);
//    }

//    public MainNavigationBar loginWithSpecificUsernamePassword(String usernameTxt, String passwordTxt) {
//        dontRemember();
//
//        //login
//        username.sendKeys(usernameTxt);
//        password.sendKeys(passwordTxt);
//        username.submit();
//
//        return new MainNavigationBar(driver);
//    }

    public MainNavigationBar loginDataDriver(String usernameTxt, String passwordTxt) {
        logoutIfLoggedIn();
        dontRemember();

        //login
        username.sendKeys(usernameTxt);
        password.sendKeys(passwordTxt);
        username.submit();

        return new MainNavigationBar(driver);
    }

//    public MainNavigationBar reloginUsing(String uname) {
//        logoutIfLoggedIn();
//        MainNavigationBar login = login(uname);
//        return login;
//    }

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

//    public String getLoggedInUserName() {
//        WaitUtils.waitForElementToBeClickable(driver, loggedInUsername, 10, false);
//        return loggedInUsername.getText();
//    }

    public boolean isErrorMessageCorrect(String expectedErrorMsg) {
        WaitUtils.waitForElementToBeVisible(driver, errorMsg, 10, false);
        boolean contains = errorMsg.getText().contains(expectedErrorMsg);
        return contains;
    }

//    public boolean isAlreadyLoggedInAsUser(String username) {
//        try {
//            //Login button should not be visible if logged in
//            WaitUtils.waitForElementToBeClickable(driver, loginBtn, 10, false);
//            return loginBtn.isDisplayed() && loginBtn.isEnabled();
//        } catch (Exception e) {
//            //Not logged in
//            return false;
//        }
//    }

    public boolean isInLoginPage() {
        //WaitUtils.waitForElementToBeClickable(driver, loginBtn, 10, false);
        boolean isLoginPage = loginBtn.isDisplayed() && loginBtn.isEnabled();
        return isLoginPage;
    }
}
