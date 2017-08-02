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

    //Login page
    @FindBy(id = "un")
    WebElement username;
    @FindBy(id = "pw")
    WebElement password;
    @FindBy(css = "input#remember")
    WebElement remember;
    @FindBy(css = ".choice_pair>label")
    WebElement rememberLabel;
    @FindBy(id = "forgotPasswordLink")
    WebElement forgotYourPasswordLink;

    //Settings icons and options
    @FindBy(css = ".gwt-Anchor.pull-down-toggle")
    WebElement settings;
    @FindBy(css = ".settings-pull-down .gwt-Anchor.pull-down-toggle")
    WebElement loggedInUsername;
    @FindBy(xpath = ".//span[contains(@style, 'personalization')]")
    WebElement photoIcon;
    @FindBy(xpath = "//*[contains(text(),'Sign Out')]")
    WebElement signOutLink;

    //Change password form
    @FindBy(name = "oldPw")
    WebElement passwordTemporary;
    @FindBy(name = "newPw")
    WebElement passwordNew;
    @FindBy(name = "confirmNewPw")
    WebElement passwordNewConfirm;

    //Terms and conditions
    @FindBy(css = "input[type='button']")
    WebElement acceptTermsAndConditions;

    //Login or submit button
    @FindBy(xpath = ".//label[@for='remember']//following::input[1]")
    WebElement btnLogin;
    @FindBy(xpath = ".//input[@type='submit']")
    WebElement btnSignin;
    @FindBy(xpath = ".//input[@type='submit']")
    WebElement btnSubmit;

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
        }

        return new MainNavigationBar(driver);
    }

    private void login(String usernameTxt, String passwordTxt) {
        //dontRemember();

        //login
        WaitUtils.waitForElementToBeClickable(driver, username, TIMEOUT_15_SECOND, false);
        username.sendKeys(usernameTxt);
        password.sendKeys(passwordTxt);
        username.submit();
    }

    public void dontRemember() {
        WaitUtils.waitForElementToBeClickable(driver, rememberLabel, TIMEOUT_5_SECOND, false);
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
                WaitUtils.waitForElementToBeClickable(driver, settings, TIMEOUT_5_SECOND, false);
                if (settings.isDisplayed()) {
                    //settings.click();
                    PageUtils.doubleClick(driver, settings);
                    driver.findElement(By.linkText("Sign Out")).click();

                    //If logout and login is too fast, appian system shows 404 in some instance of automation
                    WaitUtils.nativeWaitInSeconds(2);

                    String baseUrl = FileUtils.getTestUrl();
                    driver.get(baseUrl);
                    WaitUtils.waitForElementToBeClickable(driver, remember, TIMEOUT_5_SECOND, false);
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
                WaitUtils.waitForElementToBeClickable(driver, photoIcon, TIMEOUT_5_SECOND, false);
                if (photoIcon.isDisplayed()) {
                    //settings.click();
                    PageUtils.singleClick(driver, photoIcon);
                    WaitUtils.waitForElementToBeClickable(driver, signOutLink, TIMEOUT_5_SECOND, false);
                    signOutLink.click();

                    //If logout and login is too fast, appian system shows 404 in some instance of automation
                    WaitUtils.nativeWaitInSeconds(2);

                    String baseUrl = FileUtils.getTestUrl();
                    driver.get(baseUrl);
                    WaitUtils.waitForElementToBeClickable(driver, remember, TIMEOUT_5_SECOND, false);
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
            WaitUtils.waitForElementToBeClickable(driver, forgotYourPasswordLink, TIMEOUT_3_SECOND, false);
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
            WaitUtils.waitForElementToBeVisible(driver, loggedInUsername, TIMEOUT_5_SECOND, false);
            isInLoginPage = false;
        } catch (Exception e) {
            isInLoginPage = true;
        }
        return isInLoginPage;
    }

    private boolean amIInLoginPageManufactuererOrAuthorisedRep() {
        boolean isInLoginPage = true;
        try {
            WaitUtils.waitForElementToBeVisible(driver, photoIcon, TIMEOUT_5_SECOND, false);
            isInLoginPage = false;
        } catch (Exception e) {
            isInLoginPage = true;
        }
        return isInLoginPage;
    }


    public MainNavigationBar changePasswordTo(String tempPassword, String updatePasswordTo) {
        log.info("Now change password from : " + tempPassword + " to : " + updatePasswordTo);
        WaitUtils.waitForElementToBeClickable(driver, btnSubmit, TIMEOUT_10_SECOND);
        passwordTemporary.sendKeys(tempPassword);
        passwordNew.sendKeys(updatePasswordTo);
        passwordNewConfirm.sendKeys(updatePasswordTo);
        btnSubmit.click();
        return new MainNavigationBar(driver);
    }


    public void logout(WebDriver driver, String currentLoggedInUser) {
        //Note page displayed to Business user is different from Manufacturer and AuthorisedRep
        if(currentLoggedInUser!=null){
            if(currentLoggedInUser.toLowerCase().contains("business")){
                logoutIfLoggedIn();
            }else if(currentLoggedInUser.toLowerCase().contains("manufacturer")){
                logoutIfLoggedInOthers();
            }else if(currentLoggedInUser.toLowerCase().contains("authorised")){
                logoutIfLoggedInOthers();
            }
        }
        PageUtils.acceptAlert(driver, true, 1);
    }

    public LoginPage accetpTandC() {
        if(PageUtils.isElementClickable(driver, acceptTermsAndConditions, TIMEOUT_5_SECOND)) {
            acceptTermsAndConditions.click();
        }
        return new LoginPage(driver);
    }
}
