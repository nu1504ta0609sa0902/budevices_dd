package com.mhra.mdcm.devices.dd.appian._test.junit.smoke;

import com.mhra.mdcm.devices.dd.appian._test.junit.common.Common;
import com.mhra.mdcm.devices.dd.appian.domains.junit.User;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.AccountManufacturerRequest;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.DeviceData;
import com.mhra.mdcm.devices.dd.appian.pageobjects.LoginPage;
import com.mhra.mdcm.devices.dd.appian.pageobjects.MainNavigationBar;
import com.mhra.mdcm.devices.dd.appian.pageobjects.external._CreateManufacturerTestsData;
import com.mhra.mdcm.devices.dd.appian.utils.datadriven.ExcelDataSheet;
import com.mhra.mdcm.devices.dd.appian.utils.datadriven.JUnitUtils;
import com.mhra.mdcm.devices.dd.appian.utils.driver.BrowserConfig;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.FileUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.TestHarnessUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.PageUtils;
import static org.hamcrest.Matchers.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by TPD_Auto on 01/11/2016.
 */
@RunWith(Parameterized.class)
public class SmokeTestsAuthorisedRep extends Common {

    public static final String AUTHORISED_REP_SMOKE_TEST = "AuthorisedRepST";

    public static String baseUrl;
    private String username;
    private String password;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<User> spreadsheetData() throws IOException {
        ExcelDataSheet excelUtils = new ExcelDataSheet();//
        List<User> listOfUsers = excelUtils.getListOfUsers("configs/data/excel/users.xlsx", "Sheet1", true);
        listOfUsers = excelUtils.filterUsersBy(listOfUsers, "authorised");
        log.info("AuthorisedRep Users : " + listOfUsers);
        return listOfUsers;
    }

    public SmokeTestsAuthorisedRep(User user) {
        this.username = user.getUserName();
        this.password = user.getPassword();
    }

    @BeforeClass
    public static void setUpDriver() {
        if (driver == null) {
            driver = new BrowserConfig().getDriver();
            baseUrl = FileUtils.getTestUrl();

            //This is for entering values in a popup
            PageUtils.performBasicAuthentication(driver, baseUrl);

            log.warn("\n\nRUNNING AUTHORISED REP SMOKE TESTS");
        }
    }

    @AfterClass
    public static void clearBrowsers() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    @Before
    public void setupTest() {
        //driver.manage().deleteAllCookies();
    }

    @Test
    public void asAUserIShouldSeeErrorMessagesIfCredentialsAreIncorrect() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        password = "IsIncorrectPassword";
        loginPage.loginAsManufacturer(username, password);

        String expectedErrorMsg = "The username/password entered is invalid";
        loginPage = new LoginPage(driver);
        boolean isCorrect = loginPage.isErrorMessageCorrect(expectedErrorMsg);
        Assert.assertThat("Error message should contain : " + expectedErrorMsg, isCorrect, is(true));
    }

    @Test
    public void checkCorrectLinksAreDisplayedForAuthorisedRep() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        MainNavigationBar mainNavigationBar = loginPage.loginAsManufacturer(username, password);

        externalHomePage = mainNavigationBar.clickHome();
        String delimitedLinks = "ENTER >";
        boolean areLinksVisible = externalHomePage.isStartNowLinkDisplayed();
        Assert.assertThat("Expected to see the following links : " + delimitedLinks, areLinksVisible, is(true));

    }

    @Test
    public void asAUserIShouldBeAbleToLoginAndLogout() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        MainNavigationBar mainNavigationBar = loginPage.loginAsManufacturer(username, password);
        String expectedHeading = JUnitUtils.getExpectedHeading(username);

        boolean isCorrectPage = mainNavigationBar.isCorrectPage(expectedHeading);
        Assert.assertThat("Expected page : " + expectedHeading, isCorrectPage, is(true));

        //Logout and verify its in logout page
        loginPage = JUnitUtils.logoutIfLoggedIn(username, loginPage);

        boolean isLoginPage = loginPage.isInLoginPage();
        Assert.assertThat("Expected to be in login page", isLoginPage, is(true));
    }


    @Test
    public void asAUserIShouldBeAbleToViewListOfAuthorisedReps() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        MainNavigationBar mainNavigationBar = loginPage.loginAsManufacturer(username, password);
        externalHomePage = mainNavigationBar.clickHome();

        manufacturerList = externalHomePage.gotoListOfManufacturerPage();
        String name = manufacturerList.getARandomManufacturerName();
        Assert.assertThat("List of manufacturers may not be visible", name, not(nullValue()) );
    }

    @Test
    public void asAUserIShouldBeAbleToCreateNewAuthorisedRepsWithDevices() throws Exception {
        //Account Data
        AccountManufacturerRequest ar = new AccountManufacturerRequest();
        ar.isManufacturer = false;
        ar.updateName(AUTHORISED_REP_SMOKE_TEST);
        ar.updateNameEnding("_AT");
        ar.setUserDetails(username);
        ar.country = "Brazil";

        //Login
        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        MainNavigationBar mainNavigationBar = loginPage.loginAsManufacturer(username, password);
        externalHomePage = mainNavigationBar.clickHome();

        //Go to list of manufacturers page and add a new authorisedrep
        manufacturerList = externalHomePage.gotoListOfManufacturerPage();
        createNewManufacturer = manufacturerList.registerNewManufacturer();
        addDevices = createNewManufacturer.createTestOrganisation(ar, false);
        log.info("New Manufacturer Account Requested With Following Data : \n" + ar);

        //Add devices AND submit
        DeviceData dd = new DeviceData();
        dd.deviceType = "General Medical Device";
        dd.device = "Blood Weighing";
        dd.customMade = "Y";
        addDevices = addDevices.addFollowingDevice(dd);
        addDevices = addDevices.proceedToReview();
        addDevices = addDevices.proceedToPayment();
        addDevices = addDevices.confirmPayment();
        manufacturerList = addDevices.backToService();

        //Verify task is generated
        loginPage = loginPage.logoutIfLoggedInOthers();
        mainNavigationBar = loginPage.loginAs(JUnitUtils.getUserName(username) + ".Business", password);

        //Verify new taskSection generated and its the correct one
        boolean contains = false;
        boolean isCorrectTask = false;
        int count2 = 0;
        String orgName = ar.organisationName;
        do {
            mainNavigationBar = new MainNavigationBar(driver);
            tasksPage = mainNavigationBar.clickTasks();

            //Click on link number X
            boolean isLinkVisible = tasksPage.isLinkVisible(orgName);
            if (isLinkVisible) {
                taskSection = tasksPage.clickOnLinkWithText(orgName);
                isCorrectTask = taskSection.isCorrectTask(orgName, "New Manufacturer Registration");
                if (isCorrectTask) {
                    contains = true;
                } else {
                    count2++;
                }
            }
        } while (!contains && count2 <= 5);

        //Accept the task
        if (contains) {
            taskSection = taskSection.acceptTask();
            tasksPage = taskSection.approveTask();
        }

        log.info("Create Devices For : " + orgName);

    }

    @Override
    public String toString() {
        return "SmokeTestsAuthorisedRep";
    }
}
