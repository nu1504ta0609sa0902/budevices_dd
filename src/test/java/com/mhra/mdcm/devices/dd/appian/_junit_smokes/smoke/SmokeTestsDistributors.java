package com.mhra.mdcm.devices.dd.appian._junit_smokes.smoke;

import com.mhra.mdcm.devices.dd.appian._junit_smokes.common.Common;
import com.mhra.mdcm.devices.dd.appian.domains.junit.User;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.DeviceData;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.ManufacturerOrganisationRequest;
import com.mhra.mdcm.devices.dd.appian.pageobjects.LoginPage;
import com.mhra.mdcm.devices.dd.appian.pageobjects.MainNavigationBar;
import com.mhra.mdcm.devices.dd.appian.pageobjects.external.ExternalHomePage;
import com.mhra.mdcm.devices.dd.appian.utils.datadriven.ExcelDataSheet;
import com.mhra.mdcm.devices.dd.appian.utils.datadriven.JUnitUtils;
import com.mhra.mdcm.devices.dd.appian.utils.driver.BrowserConfig;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.FileUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.PageUtils;
import org.hamcrest.Matchers;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

/**
 * Created by TPD_Auto on 01/11/2016.
 */
@RunWith(Parameterized.class)
public class SmokeTestsDistributors extends Common {

    public static final String DISTRIBUTOR_SMOKE_TEST = "DistributorST";
    private static List<User> listOfBusinessUsers;

    public static String baseUrl;
    private String username;
    private String password;
    private String initials;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<User> spreadsheetData() throws IOException {
        ExcelDataSheet excelUtils = new ExcelDataSheet();//
        List<User> listOfUsers = excelUtils.getListOfUsers(userLoginData, profile, false);
        listOfBusinessUsers = excelUtils.filterUsersBy(listOfUsers, "business", initialsArray.get(0));
        listOfUsers = excelUtils.filterUsersBy(listOfUsers, "distributor", initialsArray.get(0));
        log.info("Manufacturer Users : " + listOfUsers);
        return listOfUsers;
    }

    public SmokeTestsDistributors(User user) {
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

            log.warn("\n\nRUNNING MANUFACTURER SMOKE TESTS");
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
        loginPage = loginPage.accetpTandC();
        loginPage.loginAsManufacturer(username, password);

        String expectedErrorMsg = "The username/password entered is invalid";
        loginPage = new LoginPage(driver);
        boolean isCorrect = loginPage.isErrorMessageCorrect(expectedErrorMsg);
        Assert.assertThat("Error message should contain : " + expectedErrorMsg, isCorrect, Matchers.is(true));
    }


    @Test
    public void checkCorrectLinksAreDisplayedForManufacturer() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        loginPage = loginPage.accetpTandC();
        MainNavigationBar mainNavigationBar = loginPage.loginAsManufacturer(username, password);

        externalHomePage = mainNavigationBar.clickHome();
        String delimitedLinks = "ENTER >";
        boolean areLinksVisible = externalHomePage.isStartNowLinkDisplayed();
        Assert.assertThat("Expected to see the following links : " + delimitedLinks, areLinksVisible, Matchers.is(true));
    }


    @Test
    public void asAUserIShouldBeAbleToLoginAndLogout() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        loginPage = loginPage.accetpTandC();
        MainNavigationBar mainNavigationBar = loginPage.loginAsManufacturer(username, password);
        String expectedHeading = JUnitUtils.getExpectedHeading(username);

        boolean isCorrectPage = mainNavigationBar.isCorrectPage(expectedHeading);
        Assert.assertThat("Expected page : " + expectedHeading, isCorrectPage, Matchers.is(true));

        //Logout and verify its in logout page
        loginPage = JUnitUtils.logoutIfLoggedIn(username, loginPage);

        boolean isLoginPage = loginPage.isInLoginPage();
        Assert.assertThat("Expected tobe in login page", isLoginPage, Matchers.is(true));
    }


    @Test
    public void asAUserIShouldBeAbleToViewAListOfManufacturer() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        loginPage = loginPage.accetpTandC();
        MainNavigationBar mainNavigationBar = loginPage.loginAsManufacturer(username, password);
        externalHomePage = mainNavigationBar.clickHome();

        manufacturerList = externalHomePage.gotoListOfManufacturerPage();
        String name = manufacturerList.getARandomManufacturerName();
        Assert.assertThat("List of manufacturers may not be visible", name, not(nullValue()));
    }


    @Test
    public void asAUserIShouldBeAbleToCreateNewManufacturerWithDevices() throws Exception {

        //Account Data
        ManufacturerOrganisationRequest ar = new ManufacturerOrganisationRequest();
        ar.isManufacturer = true;
        ar.updateName(DISTRIBUTOR_SMOKE_TEST);
        ar.updateNameEnding("_" + initials);
        ar.setUserDetails(username);
        ar.country = "United States";

        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        loginPage = loginPage.accetpTandC();
        MainNavigationBar mainNavigationBar = loginPage.loginAsManufacturer(username, password);
        //externalHomePage = mainNavigationBar.clickHome();

        //Go to list of manufacturers page and add a new manufacturer
        externalHomePage = new ExternalHomePage(driver);
        manufacturerList = externalHomePage.gotoListOfManufacturerPage();
        createNewManufacturer = manufacturerList.registerNewManufacturer();
        addDevices = createNewManufacturer.createTestOrganisation(ar, false);
        System.out.println("New Manufacturer Account Requested With Following Data : \n" + ar);

        //Add devices AND submit
        DeviceData dd = new DeviceData();
        dd.deviceType = "General Medical Device";
        dd.device = "Blood weighing";
        dd.customMade = "Y";
        addDevices = addDevices.addFollowingDevice(dd);

        //Proceed to payments
        addDevices = addDevices.proceedToReview();
        addDevices = addDevices.proceedToPayment();

        String paymentMethod = "BACS";
        addDevices = addDevices.enterPaymentDetails(paymentMethod);   //WORLDPAY OR BACS
        String reference = addDevices.getApplicationReferenceNumber();
        System.out.println("New Applicaiton reference number : " + reference);
        manufacturerList = addDevices.backToService();

        //Verify task is generated
        loginPage = loginPage.logoutIfLoggedInOthers();
        User businessUser = JUnitUtils.getBusinessUser(listOfBusinessUsers, username);
        loginPage = loginPage.accetpTandC();
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

            if(paymentMethod.toLowerCase().contains("bacs")){
                //Confirm payment : and select date of payment
                taskSection = taskSection.confirmPayment();
                taskSection = taskSection.enterDateAndTimeOfPayment();
            }
            taskSection = taskSection.approveAWIPManufacturerTask();
            taskSection = taskSection.approveAWIPAllDevices();
            taskSection = taskSection.completeTheApplication();
            System.out.println("Application completed for reference : " + reference);
        }

        System.out.println("Create Devices For : " + ar.organisationName);
    }

    @Override
    public String toString() {
        return "SmokeTestsManufacturers";
    }
}
