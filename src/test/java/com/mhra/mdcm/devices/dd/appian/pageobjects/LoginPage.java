package com.mhra.mdcm.devices.dd.appian.pageobjects;

import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.FileUtils;
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
    @FindBy(css = ".choice_pair>label")
    WebElement rememberLabel;
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
        WaitUtils.nativeWaitInSeconds(2);
        driver.get(url);
        return new LoginPage(driver);
    }

    public MainNavigationBar loginAsManufacturer(String usernameTxt, String passwordTxt) {
        return loginAsManufacturer(usernameTxt, passwordTxt, false);
    }

    /**
     * This should only be used for Manufacturer and AuthorisedRep
     * @param usernameTxt
     * @param passwordTxt
     * @param isBusinesss
     */
    private MainNavigationBar loginAsManufacturer(String usernameTxt, String passwordTxt, boolean isBusinesss) {

        boolean loggedOut = isAlreadyLoggedOut();
        if(loggedOut){
            login(usernameTxt, passwordTxt);
        }else{

            logoutIfLoggedInOthers();
            WaitUtils.nativeWaitInSeconds(1);
            login(usernameTxt, passwordTxt);

            //This should check if we are in login page and we are logged in as a manufacturer and authorisedRep
//            boolean inLoginPage = amIInLoginPageManufactuererOrAuthorisedRep();
//            boolean isAlreadyLoggedInAaUser = isAlreadyLoggedInAsSpecifiedUserInManufactuererOrAuthorisedRep(usernameTxt);
//
//            //Logout if not in login page and is not already logged in as someone else
//            if (!inLoginPage && !isAlreadyLoggedInAaUser)
//                logoutIfLoggedInOthers();
//
//            //I was logged in as someone else now login correctly
//            if (!isAlreadyLoggedInAaUser) {
//                login(usernameTxt, passwordTxt);
//            }
        }

        return new MainNavigationBar(driver);
    }

    public MainNavigationBar loginAs(String usernameTxt, String passwordTxt) {
        boolean loggedOut = isAlreadyLoggedOut();
        if(loggedOut){
            login(usernameTxt, passwordTxt);
        }else {

            logoutIfLoggedIn();
            WaitUtils.nativeWaitInSeconds(1);
            login(usernameTxt, passwordTxt);

//            boolean inLoginPage = amIInLoginPage();
//            boolean isAlreadyLoggedInAaUser = isAlreadyLoggedInAsSpecifiedUser(usernameTxt);
//
//            //Logout if not in login page and is not already logged in as someone else
//            if (!inLoginPage && !isAlreadyLoggedInAaUser)
//                logoutIfLoggedIn();
//
//            //I was logged in as someone else now login correctly
//            if (!isAlreadyLoggedInAaUser) {
//                //logoutIfLoggedIn();
//                login(usernameTxt, passwordTxt);
//            }
        }

        return new MainNavigationBar(driver);
    }

    private void login(String usernameTxt, String passwordTxt) {
        dontRemember();

        //login
        WaitUtils.waitForElementToBeClickable(driver, username, TIMEOUT_SMALL, false);
        username.sendKeys(usernameTxt);
        password.sendKeys(passwordTxt);
        username.submit();
    }

    public void dontRemember() {
        WaitUtils.waitForElementToBeClickable(driver, rememberLabel, TIMEOUT_SMALL, false);
        String checked = remember.getAttribute("checked");
        boolean selected = remember.isSelected();
        if ( checked != null || selected) {
            //remember.click();
            rememberLabel.click();
        }
    }

    /**
     * Business site
     *
     * @return
     */
    public LoginPage logoutIfLoggedIn() {
        boolean loggedOut = isAlreadyLoggedOut();
        if(!loggedOut) {
            try {
                WaitUtils.waitForElementToBeClickable(driver, settings, TIMEOUT_SMALL, false);
                if (settings.isDisplayed()) {
                    //settings.click();
                    PageUtils.doubleClick(driver, settings);
                    driver.findElement(By.linkText("Sign Out")).click();

                    //If logout and login is too fast, appian system shows 404 in some instance of automation
                    WaitUtils.nativeWaitInSeconds(2);

                    String baseUrl = FileUtils.getTestUrl();
                    driver.get(baseUrl);
                    WaitUtils.waitForElementToBeClickable(driver, remember, TIMEOUT_SMALL, false);
                }
            } catch (Exception e) {
                //Probably not logged in
            }
        }
        return new LoginPage(driver);
    }

    /**
     * logout from Manufacturer and AuthorisedRep site
     *
     * @return
     */
    public LoginPage logoutIfLoggedInOthers() {
        boolean loggedOut = isAlreadyLoggedOut();
        if(!loggedOut) {
            try {
                WaitUtils.waitForElementToBeClickable(driver, photoIcon, 10, false);
                if (photoIcon.isDisplayed()) {
                    //settings.click();
                    PageUtils.doubleClick(driver, photoIcon);
                    signOutLink.click();

                    //If logout and login is too fast, appian system shows 404 in some instance of automation
                    WaitUtils.nativeWaitInSeconds(2);

                    String baseUrl = FileUtils.getTestUrl();
                    driver.get(baseUrl);
                    WaitUtils.waitForElementToBeClickable(driver, remember, 10, false);
                }
            } catch (Exception e) {
                //Probably not logged in
            }
        }
        return new LoginPage(driver);
    }

    private boolean isAlreadyLoggedOut() {
        //WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
        boolean loggedOut = true;
        try{
            WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText("Forgot your password"), 5, false);
        }catch (Exception e){
            loggedOut = false;
        }
        return loggedOut;
    }

    public boolean isErrorMessageCorrect(String expectedErrorMsg) {
        WaitUtils.waitForElementToBeVisible(driver, errorMsg, 5, false);
        boolean contains = errorMsg.getText().contains(expectedErrorMsg);
        return contains;
    }

    public boolean isInLoginPage() {
        boolean isLoginPage = isAlreadyLoggedOut();
        //WaitUtils.waitForElementToBeClickable(driver, loginBtn, 10, false);
        //boolean isLoginPage = loginBtn.isDisplayed() && loginBtn.isEnabled();
        return isLoginPage;
    }

    private boolean isAlreadyLoggedInAsSpecifiedUser(String usernameTxt) {
        boolean isAreadyLoggedInAaUser = false;
        try {
            String loggedInAs = loggedInUsername.getText();
            String checkIfThisUserIsAlreadyLoggedIn = usernameTxt.replaceAll("\\.", " ");
            isAreadyLoggedInAaUser = loggedInAs.contains(checkIfThisUserIsAlreadyLoggedIn);
        } catch (Exception e) {
            isAreadyLoggedInAaUser = false;
        }
        return isAreadyLoggedInAaUser;
    }

    private boolean isAlreadyLoggedInAsSpecifiedUserInManufactuererOrAuthorisedRep(String usernameTxt) {

        boolean isAreadyLoggedInAaUser = false;
        try {
            WaitUtils.waitForElementToBeClickable(driver, photoIcon, 2, false);
            photoIcon.click();
            String loggedInAs = driver.findElement(By.cssSelector(".GFWJSJ4DKN strong")).getText();
            String checkIfThisUserIsAlreadyLoggedIn = usernameTxt.replaceAll("\\.", " ");
            isAreadyLoggedInAaUser = loggedInAs.contains(checkIfThisUserIsAlreadyLoggedIn);
        } catch (Exception e) {
            isAreadyLoggedInAaUser = false;
        }
        return isAreadyLoggedInAaUser;
    }


    private boolean amIInLoginPage() {
        boolean isInLoginPage = true;
        try {
            WaitUtils.waitForElementToBeVisible(driver, loggedInUsername, TIMEOUT_SMALL, false);
            isInLoginPage = false;
        } catch (Exception e) {
            isInLoginPage = true;
        }
        return isInLoginPage;
    }

    private boolean amIInLoginPageManufactuererOrAuthorisedRep() {
        boolean isInLoginPage = true;
        try {
            WaitUtils.waitForElementToBeVisible(driver, photoIcon, TIMEOUT_SMALL, false);
            isInLoginPage = false;
        } catch (Exception e) {
            isInLoginPage = true;
        }
        return isInLoginPage;
    }
}
