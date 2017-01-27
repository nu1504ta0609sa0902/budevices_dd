package com.mhra.mdcm.devices.dd.appian._test.junit.device_injection.directly.OLD_STUFF;

import com.mhra.mdcm.devices.dd.appian._test.junit.common.Common;
import com.mhra.mdcm.devices.dd.appian.domains.junit.User;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.AccountRequest;
import com.mhra.mdcm.devices.dd.appian.pageobjects.LoginPage;
import com.mhra.mdcm.devices.dd.appian.pageobjects.MainNavigationBar;
import com.mhra.mdcm.devices.dd.appian.utils.datadriven.ExcelDataSheet;
import com.mhra.mdcm.devices.dd.appian.utils.driver.BrowserConfig;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.FileUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.TestHarnessUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Created by TPD_Auto on 01/11/2016.
 */
@RunWith(Parameterized.class)
public class BusinessCreateManufacturersWithTestersInitials extends Common {

    private static List<User> listOfBusinessUsers;
    private static User businessUser;
    public String[] initialsArray = new String[]{
            "NU", //"NU", "HB", "YC", "PG", "AN", "LP"
    };


    public static final String AUTHORISED_REP_SMOKE_TEST = "AuthorisedRepST";
    public static final String MANUFACTURER_SMOKE_TEST = "ManufacturerST";

    private static List<User> listOfManufacturerUsers = new ArrayList<>();

    public static WebDriver driver;
    public static String baseUrl;
    private String username;
    private String password;
    private String initials;


    public static void main(String [] args){
        List<User> listOfUsers = getListOfUsersFromExcel();
        List<User> listOfBusinessUsersFromExcel = getListOfBusinessUsersFromExcel();
        System.setProperty("current.browser", "gc");
        setUpDriver();

        businessUser = setCorrectLoginDetails("_NU", listOfBusinessUsers);
        BusinessCreateManufacturersWithTestersInitials tjs = new BusinessCreateManufacturersWithTestersInitials(businessUser);
        boolean isManufacturer = false;
        if(isManufacturer){
            tjs.asABusinessUsersShouldBeAbleToCreateMultipleManufacturerAccounts();
        }else{
            tjs.asABusinessUsersShouldBeAbleToCreateMultipleAuthorisedRepAccountRequest();
        }

        int x = 10;
        boolean t = (x == 9);
        if(!t){

        }
    }



    public BusinessCreateManufacturersWithTestersInitials(User user) {
        this.username = user.getUserName();
        this.password = user.getPassword();
        this.initials = user.getInitials();
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

    public static void setUpDriver() {
        if (driver == null) {
            driver = new BrowserConfig().getDriver();
            driver.manage().window().maximize();
            baseUrl = FileUtils.getTestUrl();
            log.warn("\n\nCREATE MANUFACTURERS AND AUTHORISEDREPS USING BUSINESS TEST HARNESS");
        }
    }


    private static List<User> getListOfUsersFromExcel() {
        ExcelDataSheet excelUtils = new ExcelDataSheet();//
        List<User> listOfUsers = excelUtils.getListOfUsers("configs/data/excel/users.xlsx", "InjectSpecificUser");
        listOfUsers = excelUtils.filterUsersBy(listOfUsers, "manufacturer");
        listOfManufacturerUsers = listOfUsers;
        return listOfUsers;
    }

    private static List<User> getListOfBusinessUsersFromExcel() {
        ExcelDataSheet excelUtils = new ExcelDataSheet();//
        List<User> listOfUsers = excelUtils.getListOfUsers("configs/data/excel/users.xlsx", "Sheet1");
        listOfBusinessUsers = excelUtils.filterUsersBy(listOfUsers, "business");
        return listOfBusinessUsers;
    }


    public void clearBrowsers() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * IF YOU CREATE USING BUSINESS TEST HARNESS, YOU WILL LOOSE DATA FOR
     *  - MANUFACTURERS AND AUTHORISEDREPS
     *
     *  SO IF YOU LOGIN AS Noor.Uddin.Business
     */
    public void asABusinessUsersShouldBeAbleToCreateMultipleManufacturerAccounts() {

        for (String initials : initialsArray) {

            AccountRequest ar = new AccountRequest();
            try {

                loginPage = new LoginPage(driver);
                loginPage = loginPage.loadPage(baseUrl);
                MainNavigationBar mainNavigationBar = loginPage.loginAs(username, password);

                //go to accounts page > test harness page
                actionsPage = mainNavigationBar.clickActions();
                createTestsData = actionsPage.gotoTestsHarnessPage();

                //Now create the test data using harness page
                ar.isManufacturer = true;
                ar.updateName(MANUFACTURER_SMOKE_TEST);
                ar.updateNameEnding("_" + initials);
                ar.setUserDetails(username);

                ar.firstName = TestHarnessUtils.getName(initials, true, listOfManufacturerUsers);
                ar.lastName = TestHarnessUtils.getName(initials, false, listOfManufacturerUsers);

                actionsPage = createTestsData.createTestOrganisation(ar);
                boolean isInCorrectPage = actionsPage.isInActionsPage();
                if (!isInCorrectPage) {
                    actionsPage = createTestsData.createTestOrganisation(ar);
                }

                boolean createdSuccessfully = actionsPage.isInActionsPage();
                if (createdSuccessfully) {
                    System.out.println("Created a new account : " + ar.organisationName);
                }

                String orgName = ar.organisationName;

                //Verify new taskSection generated and its the correct one
                boolean contains = false;
                boolean isCorrectTask = false;
                int count = 0;
                do {
                    mainNavigationBar = new MainNavigationBar(driver);
                    tasksPage = mainNavigationBar.clickTasks();

                    //Click on link number X
                    taskSection = tasksPage.clickOnTaskNumber(count);
                    isCorrectTask = taskSection.isCorrectTask(orgName);
                    if (isCorrectTask) {
                        contains = true;
                    } else {
                        count++;
                    }
                } while (!contains && count <= 5);

                //Accept the task
                if (contains) {
                    taskSection = taskSection.acceptTask();
                    tasksPage = taskSection.approveTask();
                }

                assertThat("Task not found for organisation : " + orgName, contains, is(equalTo(true)));
            }catch (Exception e){
                e.printStackTrace();
            }

            WaitUtils.nativeWaitInSeconds(2);
            System.out.println(ar.organisationName);
            log.info(ar.organisationName);
            loginPage.logoutIfLoggedIn();
        }
    }


    public void asABusinessUsersShouldBeAbleToCreateMultipleAuthorisedRepAccountRequest() {

        for (String initials : initialsArray) {

            loginPage = new LoginPage(driver);
            loginPage = loginPage.loadPage(baseUrl);
            MainNavigationBar mainNavigationBar = loginPage.loginAs(username, password);

            //go to accounts page > test harness page
            actionsPage = mainNavigationBar.clickActions();
            createTestsData = actionsPage.gotoTestsHarnessPage();

            //Now create the test data using harness page
            AccountRequest ar = new AccountRequest();
            ar.isManufacturer = false;
            ar.updateName(AUTHORISED_REP_SMOKE_TEST);
            ar.updateNameEnding("_" + initials);
            ar.setUserDetails(username);

            ar.firstName = TestHarnessUtils.getName(initials, true, listOfManufacturerUsers);
            ar.lastName = TestHarnessUtils.getName(initials, false, listOfManufacturerUsers);

            actionsPage = createTestsData.createTestOrganisation(ar);
            boolean isInCorrectPage = actionsPage.isInActionsPage();
            if (!isInCorrectPage) {
                actionsPage = createTestsData.createTestOrganisation(ar);
            }

            boolean createdSuccessfully = actionsPage.isInActionsPage();
            if (createdSuccessfully) {
                System.out.println("Created a new account : " + ar.organisationName);
            }

            String orgName = ar.organisationName;

            //Verify new taskSection generated and its the correct one
            boolean contains = false;
            boolean isCorrectTask = false;
            int count = 0;
            do {
                mainNavigationBar = new MainNavigationBar(driver);
                tasksPage = mainNavigationBar.clickTasks();

                //Click on link number X
                taskSection = tasksPage.clickOnTaskNumber(count);
                isCorrectTask = taskSection.isCorrectTask(orgName);
                if (isCorrectTask) {
                    contains = true;
                } else {
                    count++;
                }
            } while (!contains && count <= 5);

            //Accept the task
            if (contains) {
                taskSection = taskSection.acceptTask();
                tasksPage = taskSection.approveTask();
            }

            assertThat("Task not found for organisation : " + orgName, contains, is(equalTo(true)));


            System.out.println(ar.organisationName);
            log.info(ar.organisationName);
            loginPage.logoutIfLoggedIn();

        }
    }

    @Override
    public String toString() {
        return "SmokeTestsBusiness";
    }
}
