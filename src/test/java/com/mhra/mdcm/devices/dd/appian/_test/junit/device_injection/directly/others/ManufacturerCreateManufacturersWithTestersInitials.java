package com.mhra.mdcm.devices.dd.appian._test.junit.device_injection.directly.others;

import com.mhra.mdcm.devices.dd.appian._test.junit.common.Common;
import com.mhra.mdcm.devices.dd.appian.domains.junit.User;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.AccountManufacturerRequest;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.DeviceData;
import com.mhra.mdcm.devices.dd.appian.pageobjects.LoginPage;
import com.mhra.mdcm.devices.dd.appian.pageobjects.MainNavigationBar;
import com.mhra.mdcm.devices.dd.appian.utils.datadriven.ExcelDataSheet;
import com.mhra.mdcm.devices.dd.appian.utils.driver.BrowserConfig;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.FileUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.TestHarnessUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by TPD_Auto on 01/11/2016.
 */
@RunWith(Parameterized.class)
public class ManufacturerCreateManufacturersWithTestersInitials extends Common {

    public String[] initialsArray = new String[]{
            "LP", //"NU", "HB", "YC", "PG", "AN", "LP"
    };

    public static final String AUTHORISED_REP_SMOKE_TEST = "AuthorisedRepST";
    public static final String MANUFACTURER_SMOKE_TEST = "ManufacturerST";

    private static List<User> listOfManufacturerUsers = new ArrayList<>();

    public static WebDriver driver;
    public static String baseUrl;
    private String username;
    private String password;
    private String initials;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<User> spreadsheetData() throws IOException {
        ExcelDataSheet excelUtils = new ExcelDataSheet();//
        List<User> listOfUsers = excelUtils.getListOfUsers("configs/data/excel/users.xlsx", "Sheet1");
        listOfUsers = excelUtils.filterUsersBy(listOfUsers, "manufacturer");
        listOfManufacturerUsers = listOfUsers;
        log.info("Business Users : " + listOfUsers);
        return listOfUsers;
    }


    public ManufacturerCreateManufacturersWithTestersInitials(User user) {
        this.username = user.getUserName();
        this.password = user.getPassword();
        this.initials = user.getInitials();
    }

    @BeforeClass
    public static void setUpDriver() {
        if (driver == null) {
            driver = new BrowserConfig().getDriver();
            baseUrl = FileUtils.getTestUrl();
            log.warn("\n\nRUNNING SCRIPT TO CREATE INITIAL DATA FOR ENTERING DEVICES TEST DATA");
        }
    }

    @AfterClass
    public static void clearBrowsers() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Before
    public void setupTest() {
        //driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
    }

    @Test
    public void asAManufacturerUsersShouldBeAbleToCreateMultipleManufacturerAccounts() {

        for (String initials : initialsArray) {

            AccountManufacturerRequest ar = new AccountManufacturerRequest();
            try {

                loginPage = new LoginPage(driver);
                loginPage = loginPage.loadPage(baseUrl);
                MainNavigationBar mainNavigationBar = loginPage.loginAsManufacturer(username, password);

                //go to accounts page > test harness page
                externalHomePage = mainNavigationBar.clickExternalHOME();

                manufacturerList = externalHomePage.gotoListOfManufacturerPage();
                createNewManufacturer = manufacturerList.registerNewManufacturer();

                //Now create the test data using harness page
                ar.updateName(MANUFACTURER_SMOKE_TEST);
                ar.updateNameEnding("_" + initials);
                ar.setUserDetails(username);
                ar.country = "Nepal";

                ar.firstName = TestHarnessUtils.getName(initials, true, listOfManufacturerUsers);
                ar.lastName = TestHarnessUtils.getName(initials, false, listOfManufacturerUsers);

                //Create new manufacturer data
                addDevices = createNewManufacturer.createTestOrganisation(ar, true);
                if(createNewManufacturer.isErrorMessageDisplayed()){
                    externalHomePage = mainNavigationBar.clickExternalHOME();
                    manufacturerList = externalHomePage.gotoListOfManufacturerPage();
                    createNewManufacturer = manufacturerList.registerNewManufacturer();
                    addDevices = createNewManufacturer.createTestOrganisation(ar, true);
                }

                boolean createdSuccessfully = addDevices.isDeviceTypeCorrect();
                if (createdSuccessfully) {
                    System.out.println("Created a new account : " + ar.organisationName);
                }

                //Declare device and approve it
                String[] dataUpdated = new String[1];
                DeviceData dd = new DeviceData();
                dd.deviceType = "general medical device";
                dd.customMade = "y";
                dd.device = "Blood";
                addDevices = addDevices.addFollowingDevice(dd);

                //Proceed to payment
                addDevices = addDevices.proceedToPayment();
                addDevices = addDevices.submitRegistration();
                externalHomePage = addDevices.finish();

                WaitUtils.nativeWaitInSeconds(3);
                loginPage = loginPage.logoutIfLoggedInOthers();

                //Verify and accept the new task
                loginPage.loginAs("Noor.Uddin.Business", "MHRA1234");
                mainNavigationBar = new MainNavigationBar(driver);
                tasksPage = mainNavigationBar.clickTasks();

                //Verify new taskSection generated and its the correct one
                boolean contains = false;
                boolean isCorrectTask = false;
                String link = "New Manufacturer Registration Request";
                String orgName = ar.organisationName;
                int count = 0;
                do {
                    //Refresh each time, it may take a while for the new task to arrive
                    tasksPage = mainNavigationBar.clickTasks();

                    //Click on link number X
                    taskSection = tasksPage.clickOnTaskNumber(count, link);
                    isCorrectTask = taskSection.isCorrectTask(orgName);
                    if (isCorrectTask) {
                        contains = true;
                    } else {
                        //Try position 0 again
                        tasksPage = mainNavigationBar.clickTasks();
                        taskSection = tasksPage.clickOnTaskNumber(0, link);
                        isCorrectTask = taskSection.isCorrectTask(orgName);
                        count++;
                    }
                } while (!contains && count <= 5);

                //If its still not found than try the first 1 again, because it may have taken few seconds longer than usual
                if (!contains) {
                    //mainNavigationBar = new MainNavigationBar(driver);
                    tasksPage = mainNavigationBar.clickTasks();
                    taskSection = tasksPage.clickOnTaskNumber(0, link);
                    isCorrectTask = taskSection.isCorrectTask(orgName);
                    if (isCorrectTask) {
                        contains = true;
                    }
                }



            }catch (Exception e){
                e.printStackTrace();
            }

            WaitUtils.nativeWaitInSeconds(2);
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
