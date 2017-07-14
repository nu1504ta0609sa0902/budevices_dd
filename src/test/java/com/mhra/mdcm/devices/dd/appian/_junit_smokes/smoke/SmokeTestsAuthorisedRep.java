package com.mhra.mdcm.devices.dd.appian._junit_smokes.smoke;

import com.mhra.mdcm.devices.dd.appian._junit_smokes.common.Common;
import com.mhra.mdcm.devices.dd.appian.domains.junit.User;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.ManufacturerOrganisationRequest;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.DeviceData;
import com.mhra.mdcm.devices.dd.appian.pageobjects.LoginPage;
import com.mhra.mdcm.devices.dd.appian.pageobjects.MainNavigationBar;
import com.mhra.mdcm.devices.dd.appian.pageobjects.external.ExternalHomePage;
import com.mhra.mdcm.devices.dd.appian.utils.datadriven.ExcelDataSheet;
import com.mhra.mdcm.devices.dd.appian.utils.datadriven.JUnitUtils;
import com.mhra.mdcm.devices.dd.appian.utils.driver.BrowserConfig;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.FileUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.PageUtils;
import static org.hamcrest.Matchers.*;

import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
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

    private static List<User> listOfBusinessUsers;
    public static String baseUrl;
    private String username;
    private String password;
    private String initials;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<User> spreadsheetData() throws IOException {
        ExcelDataSheet excelUtils = new ExcelDataSheet();//
        List<User> listOfUsers = excelUtils.getListOfUsers("configs/data/excel/users.xlsx", "Sheet1", true);
        listOfBusinessUsers = excelUtils.filterUsersBy(listOfUsers, "business");
        listOfUsers = excelUtils.filterUsersBy(listOfUsers, "authorised");
        log.info("AuthorisedRep Users : " + listOfUsers);
        return listOfUsers;
    }

    public SmokeTestsAuthorisedRep(User user) {
        this.username = user.getUserName();
        this.password = user.getPassword();
        this.initials = user.getInitials();
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
        //externalHomePage = mainNavigationBar.clickHome();
        externalHomePage = new ExternalHomePage(driver);
        manufacturerList = externalHomePage.gotoListOfManufacturerPage();
        String name = manufacturerList.getARandomManufacturerName();
        Assert.assertThat("List of manufacturers may not be visible", name, not(nullValue()) );
    }

    @Test
    public void asAUserIShouldBeAbleToCreateNewAuthorisedRepsWithDevices() throws Exception {
        //Account Data
        ManufacturerOrganisationRequest ar = new ManufacturerOrganisationRequest();
        ar.isManufacturer = false;
        ar.updateName(AUTHORISED_REP_SMOKE_TEST);
        ar.updateNameEnding("_" + initials);
        ar.setUserDetails(username);
        ar.country = "Brazil";

        //Login
        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        MainNavigationBar mainNavigationBar = loginPage.loginAsManufacturer(username, password);
        //externalHomePage = mainNavigationBar.clickHome();

        //Go to list of manufacturers page and add a new authorisedrep
        externalHomePage = new ExternalHomePage(driver);
        manufacturerList = externalHomePage.gotoListOfManufacturerPage();
        createNewManufacturer = manufacturerList.registerNewManufacturer();
        addDevices = createNewManufacturer.createTestOrganisation(ar, false);
        System.out.println("New Manufacturer Account Requested With Following Data : \n" + ar);

        //Add devices AND submit
        DeviceData dd = new DeviceData();
        dd.deviceType = "General Medical Device";
        dd.device = "Blood Weighing";
        dd.customMade = "Y";

        //Add devices and Proceed to payments
        addDevices = addDevices.addFollowingDevice(dd);
        System.out.println("Create Devices For : " + ar.organisationName);

        addDevices = addDevices.proceedToReview();
        addDevices = addDevices.proceedToPayment();
        addDevices = addDevices.enterPaymentDetails("BACS");   //OR BACS
        String reference = addDevices.getApplicationReferenceNumber();
        System.out.println("New Applicaiton reference number : " + reference);
        manufacturerList = addDevices.backToService();

        //Verify task is generated
        loginPage = loginPage.logoutIfLoggedInOthers();
        User businessUser = JUnitUtils.getBusinessUser(listOfBusinessUsers, username);
        mainNavigationBar = loginPage.loginAs(businessUser.getUserName(), businessUser.getPassword());

        //Verify new taskSection generated and its the correct one
        boolean contains = false;
        int not = 0;
        do {
            mainNavigationBar = new MainNavigationBar(driver);
            tasksPage = mainNavigationBar.clickTasks();
            taskSection = tasksPage.gotoApplicationWIPPage();
            PageUtils.acceptAlert(driver, true);

            //Search and view the application via reference number
            taskSection = taskSection.searchAWIPPageForAccount(reference);
            taskSection.isSearchingCompleted();

            //Click on link number X
            try {
                taskSection = taskSection.clickOnApplicationReferenceLink(reference);
                contains = true;
            } catch (Exception e) {
                contains = false;
            }
            not++;
        } while (!contains && not <= 3);

        //Accept the task
        if (contains) {
            taskSection = taskSection.assignTaskToMe();
            taskSection = taskSection.confirmAssignment(true);
            taskSection = taskSection.approveAWIPManufacturerTask();
            taskSection = taskSection.approveAWIPAllDevices();
            taskSection = taskSection.completeTheApplication();
            WaitUtils.nativeWaitInSeconds(5);
            System.out.println("Application completed for reference : " + reference);
        }


    }

    @Override
    public String toString() {
        return "SmokeTestsAuthorisedRep";
    }
}
