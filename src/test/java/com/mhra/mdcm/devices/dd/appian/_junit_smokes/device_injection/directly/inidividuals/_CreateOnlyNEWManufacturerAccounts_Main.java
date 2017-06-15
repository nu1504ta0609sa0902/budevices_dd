package com.mhra.mdcm.devices.dd.appian._junit_smokes.device_injection.directly.inidividuals;

import com.mhra.mdcm.devices.dd.appian._junit_smokes.common.Common;
import com.mhra.mdcm.devices.dd.appian._junit_smokes.device_injection.directly.ExcelDirectDeviceDataUtils;
import com.mhra.mdcm.devices.dd.appian.domains.junit.User;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.AccountRequest;
import com.mhra.mdcm.devices.dd.appian.pageobjects.LoginPage;
import com.mhra.mdcm.devices.dd.appian.pageobjects.MainNavigationBar;
import com.mhra.mdcm.devices.dd.appian.utils.driver.BrowserConfig;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.FileUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.TestHarnessUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.PageUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;

import java.util.List;


/**
 * Created by TPD_Auto on 01/11/2016.
 */
@RunWith(Parameterized.class)
public class _CreateOnlyNEWManufacturerAccounts_Main extends Common {

    public static final String MANUFACTURER_SMOKE_TEST = "ManufacturerAccountST";

    public static WebDriver driver;
    public static String baseUrl;
    private String username;
    private String password;
    private String initials;


    public _CreateOnlyNEWManufacturerAccounts_Main(User businessUser) {
        this.username = businessUser.getUserName();
        this.password = businessUser.getPassword();
        this.initials = businessUser.getInitials();
    }

    public static void main(String[] args) {
        List<User> listOfManufacturerUsers = ExcelDirectDeviceDataUtils.getListOfUsersFromExcel("manufacturer");
        List<User> listOfBusinessUsers = ExcelDirectDeviceDataUtils.getListOfBusinessUsersFromExcel("business");
        setUpDriver();

        for (User u : listOfBusinessUsers) {
            try {
                //Always use one of the Business Accounts to create the test manufacturers
                //REMEMBER ALL PREVIOUS MANUFACTURERS DATA WILL BE REMOVED
                String initials = u.getInitials();
                User businessUser = setCorrectLoginDetails("_" + initials, listOfBusinessUsers);
                _CreateOnlyNEWManufacturerAccounts_Main tgs = new _CreateOnlyNEWManufacturerAccounts_Main(businessUser);

                //We only want to do it if the INITIALS in our initialsArray list
                boolean isInitialFound = tgs.isInitialsInTheList(businessUser.getInitials());
                if (isInitialFound) {
                    log.info("Creating for user with initials : " + initials);
                    //Create a new account for the manufacturer user
                    User manufacturerUser = TestHarnessUtils.getUserWithInitials(initials, listOfManufacturerUsers);
                    tgs.createManufacturerAccountWithBusinessTestHarness(businessUser, manufacturerUser);

                    //FLOW CHANGED, now email is sent and you must change the password to use the account 06/2017

                } else {
                    log.info("Not creating any data for : " + businessUser + "\nCheck initialsArray contains the initials : " + businessUser.getInitials());
                }

            } catch (Exception e) {
                log.info("Try and setup data for next user ");
            }
        }

        //closeDriver();
    }

    private static void closeDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    private boolean isInitialsInTheList(String initials) {
        boolean found = false;
        for (String in : initialsArray) {
            if (in.equals(initials)) {
                found = true;
                break;
            }
        }
        return found;
    }

    public static void setUpDriver() {
        System.setProperty("current.browser", "gc");
        if (driver == null) {
            driver = new BrowserConfig().getDriver();
            driver.manage().window().maximize();
            baseUrl = FileUtils.getTestUrl();
            PageUtils.performBasicAuthentication(driver, baseUrl);
            log.warn("\n\nTHIS IS NOT JUNIT, THIS IS NOT JUNIT");
            log.warn("\n\nINSERT DEVICES AS MANUFACTURER USER VIA MAIN METHOD");
        }
    }

    private static User setCorrectLoginDetails(String nameSelected, List<User> listOfUsers) {
        User selectCorrectUser = null;
        for (User u : listOfUsers) {
            String initials = "_" + u.getInitials();
            if (nameSelected.contains(initials)) {
                selectCorrectUser = u;
                break;
            }
        }

        return selectCorrectUser;
    }

    private void createManufacturerAccountWithBusinessTestHarness(User businessUser, User manufacturerUser) {

            AccountRequest ar = new AccountRequest();
            ar.isManufacturer = true;
            ar.updateName(MANUFACTURER_SMOKE_TEST);
            ar.updateNameEnding("_" + businessUser.getInitials());
            ar.initials = businessUser.getInitials();
            ar.setUserDetails(manufacturerUser.getUserName());
            ar.initials = businessUser.getInitials();

            try {

                loginPage = new LoginPage(driver);
                loginPage = loginPage.loadPage(baseUrl);
                MainNavigationBar mainNavigationBar = loginPage.loginAs(businessUser.getUserName(), businessUser.getPassword());

                //go to accounts page > test harness page
                actionsPage = mainNavigationBar.clickActions();
                createTestsData = actionsPage.gotoTestsHarnessPage();

                //Now create the test data using harness page
                actionsPage = createTestsData.createNewAccountUsingBusinessTestHarness(ar);
                boolean isInCorrectPage = actionsPage.isApplicationSubmittedSuccessfully();
                if (!isInCorrectPage) {
                    actionsPage = createTestsData.createNewAccountUsingBusinessTestHarness(ar);
                }else{
                    log.info("Created a new Manufacturer account : " + ar.organisationName);
                    log.info("Username : " + ar.userName);
                }

                String orgName = ar.organisationName;
                String accountNameOrReference = actionsPage.getApplicationReferenceNumber();
                log.info("Account reference number : " + accountNameOrReference);

                //Verify new taskSection generated and its the correct one
                boolean contains = false;
                boolean isCorrectTask = false;
                int count = 0;
                do {
                    mainNavigationBar = new MainNavigationBar(driver);
                    tasksPage = mainNavigationBar.clickTasks();
                    taskSection = tasksPage.gotoApplicationWIPPage();
                    PageUtils.acceptAlert(driver, true);

                    //Search and view the application via reference number
                    taskSection = taskSection.searchAWIPPageForAccount(accountNameOrReference);

                    //Click on link number X
                    try {
                        taskSection = taskSection.clickOnApplicationReferenceLink(accountNameOrReference);
                        contains = true;
                    } catch (Exception e) {
                        contains = false;
                    }
                    count++;
                } while (!contains && count <= 3);

                //Accept the task
                if(contains) {
                    taskSection = taskSection.assignTaskToMe();
                    taskSection = taskSection.confirmAssignment(true);
                    tasksPage = taskSection.approveTaskNewAccount();
                    taskSection = taskSection.confirmAssignment(true);
                }

                log.info("Created The Following Manufacturer Account : " + orgName + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }

            WaitUtils.nativeWaitInSeconds(2);
            System.out.println(ar.organisationName);
            loginPage.logoutIfLoggedIn();
            WaitUtils.nativeWaitInSeconds(2);

    }


    @Override
    public String toString() {
        return "CREATE DEVICES FOR Manufacturers";
    }
}
