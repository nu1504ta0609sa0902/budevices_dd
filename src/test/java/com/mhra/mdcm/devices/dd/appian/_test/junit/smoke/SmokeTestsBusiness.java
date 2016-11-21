package com.mhra.mdcm.devices.dd.appian._test.junit.smoke;

import com.mhra.mdcm.devices.dd.appian._test.junit.common.Common;
import com.mhra.mdcm.devices.dd.appian.domains.junit.User;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.AccountRequest;
import com.mhra.mdcm.devices.dd.appian.pageobjects.LoginPage;
import com.mhra.mdcm.devices.dd.appian.pageobjects.MainNavigationBar;
import com.mhra.mdcm.devices.dd.appian.utils.datadriven.ExcelDataSheet;
import com.mhra.mdcm.devices.dd.appian.utils.datadriven.JUnitUtils;
import com.mhra.mdcm.devices.dd.appian.utils.driver.BrowserConfig;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.FileUtils;
import org.hamcrest.Matchers;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Created by TPD_Auto on 01/11/2016.
 */
@RunWith(Parameterized.class)
public class SmokeTestsBusiness extends Common {

    public static WebDriver driver;
    public static String baseUrl;
    private String username;
    private String password;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<User> spreadsheetData() throws IOException {
        ExcelDataSheet excelUtils = new ExcelDataSheet();//
        List<User> listOfUsers = excelUtils.getListOfUsers("configs/data/excel/users.xlsx", "Sheet1");
        listOfUsers = excelUtils.filterUsersBy(listOfUsers, "business");
        log.info("Business Users : " + listOfUsers);
        return listOfUsers;
    }


    public SmokeTestsBusiness(User user) {
        this.username = user.getUserName();
        this.password = user.getPassword();
    }

    @BeforeClass
    public static void setUpDriver() {
        if (driver == null) {
            driver = new BrowserConfig().getDriver();
            baseUrl = FileUtils.getTestUrl();
            log.warn("\n\nRUNNING BUSINESS SMOKE TESTS");
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
        driver.manage().deleteAllCookies();
    }


    @Test
    public void asAUserIShouldSeeErrorMessagesIfCredentialsAreIncorrect() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        password = "IsIncorrectPassword";
        loginPage.loginAs(username, password);

        String expectedErrorMsg = "The username/password entered is invalid";
        loginPage = new LoginPage(driver);
        boolean isCorrect = loginPage.isErrorMessageCorrect(expectedErrorMsg);
        Assert.assertThat("Error message should contain : " + expectedErrorMsg, isCorrect, Matchers.is(true));
    }


    @Test
    public void asAUserIShouldBeAbleToLoginAndLogout() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        MainNavigationBar mainNavigationBar = loginPage.loginAs(username, password);
        String expectedHeading = JUnitUtils.getExpectedHeading(username);

        boolean isCorrectPage = mainNavigationBar.isCorrectPage(expectedHeading);
        Assert.assertThat("Expected page : " + expectedHeading, isCorrectPage, Matchers.is(true));

        //Logout and verify its in logout page
        loginPage = JUnitUtils.logoutIfLoggedIn(username, loginPage);

        boolean isLoginPage = loginPage.isInLoginPage();
        Assert.assertThat("Expected to be in login page", isLoginPage, Matchers.is(true));
    }


    @Test
    public void asABusinessUserIShouldBeAbleToNavigateToDifferentSections() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        MainNavigationBar mainNavigationBar = loginPage.loginAs(username, password);

        List<String> listOfSections = JUnitUtils.getListOfTabSections();
        String expectedHeading = "News";
        //For each page
        for (String page : listOfSections) {
            expectedHeading = page;
            if (page.equals("News")) {
                newsPage = mainNavigationBar.clickNews();
            } else if (page.equals("Tasks")) {
                tasksPage = mainNavigationBar.clickTasks();
            } else if (page.equals("Records")) {
                recordsPage = mainNavigationBar.clickRecords();
            } else if (page.equals("Reports")) {
                reportsPage = mainNavigationBar.clickReports();
            } else if (page.equals("Actions")) {
                actionsPage = mainNavigationBar.clickActions();
            }
        }

        boolean isCorrectPage = mainNavigationBar.isCorrectPage(expectedHeading);
        Assert.assertThat("Expected pages : " + expectedHeading, isCorrectPage, Matchers.is(true));

    }

/**
    @Ignore
    public void asABusinessUserIShouldBeAbleToViewAccountsDevicesAndOtherPages() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        MainNavigationBar mainNavigationBar = loginPage.loginAs(username, password);

        List<String> listOfLinks = JUnitUtils.getListOfRecordsPageLinks();

        boolean isHeadingVisibleAndCorrect = false;
        boolean isItemsDisplayedAndCorrect = false;
        String expectedHeadings = "Accounts";
        for (String page : listOfLinks) {
            //Go to records page and click
            recordsPage = mainNavigationBar.clickRecords();
            expectedHeadings = page;
            if (page.equals("Accounts")) {
                accounts = recordsPage.clickOnAccounts();
                isHeadingVisibleAndCorrect = accounts.isHeadingCorrect(expectedHeadings);
                isItemsDisplayedAndCorrect = accounts.isItemsDisplayed(expectedHeadings);
                //Verify results
                Assert.assertThat("Heading should be : " + expectedHeadings, isHeadingVisibleAndCorrect, is(true));
                Assert.assertThat("Expected to see at least 1 item", isItemsDisplayedAndCorrect, is(true));
            } else if (page.equals("All Devices")) {
                allDevices = recordsPage.clickOnAllDevices();
                isHeadingVisibleAndCorrect = allDevices.isHeadingCorrect(expectedHeadings);
                isItemsDisplayedAndCorrect = allDevices.isItemsDisplayed(expectedHeadings);
                //Verify results
                Assert.assertThat("Heading should be : " + expectedHeadings, isHeadingVisibleAndCorrect, is(true));
                Assert.assertThat("Expected to see at least 1 item", isItemsDisplayedAndCorrect, is(true));
            } else if (page.equals("All Products")) {
                allProducts = recordsPage.clickOnAllProducts();
                isHeadingVisibleAndCorrect = allProducts.isHeadingCorrect(expectedHeadings);
                isItemsDisplayedAndCorrect = allProducts.isItemsDisplayed(expectedHeadings);
                //Verify results
                Assert.assertThat("Heading should be : " + expectedHeadings, isHeadingVisibleAndCorrect, is(true));
                Assert.assertThat("Expected to see at least 1 item", isItemsDisplayedAndCorrect, is(true));
            } else if (page.equals("All Organisations")) {
                allOrganisations = recordsPage.clickOnAllOrganisations();
                isHeadingVisibleAndCorrect = allOrganisations.isHeadingCorrect(expectedHeadings);
                isItemsDisplayedAndCorrect = allOrganisations.isItemsDisplayed(expectedHeadings);
                //Verify results
                Assert.assertThat("Heading should be : " + expectedHeadings, isHeadingVisibleAndCorrect, is(true));
                Assert.assertThat("Expected to see at least 1 item", isItemsDisplayedAndCorrect, is(true));
            } else if (page.equals("Devices")) {
                devices = recordsPage.clickOnDevices();
                isHeadingVisibleAndCorrect = devices.isHeadingCorrect(expectedHeadings);
                isItemsDisplayedAndCorrect = devices.isItemsDisplayed(expectedHeadings);
                //Verify results
                Assert.assertThat("Heading should be : " + expectedHeadings, isHeadingVisibleAndCorrect, is(true));
                Assert.assertThat("Expected to see at least 1 item", isItemsDisplayedAndCorrect, is(true));
            }
        }

    }
**/

    @Test
    public void asABusinessUserIShouldBeAbleToViewAccountsPage() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        MainNavigationBar mainNavigationBar = loginPage.loginAs(username, password);

        String expectedHeadings = "Accounts";
        recordsPage = mainNavigationBar.clickRecords();
        accounts = recordsPage.clickOnAccounts();

        boolean isHeadingVisibleAndCorrect = accounts.isHeadingCorrect(expectedHeadings);
        boolean isItemsDisplayedAndCorrect = accounts.isItemsDisplayed(expectedHeadings);
        //Verify results
        Assert.assertThat("Heading should be : " + expectedHeadings, isHeadingVisibleAndCorrect, is(true));
        Assert.assertThat("Expected to see at least 1 item", isItemsDisplayedAndCorrect, is(true));

    }


    @Test
    public void asABusinessUserIShouldBeAbleToViewAllDevicesPage() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        MainNavigationBar mainNavigationBar = loginPage.loginAs(username, password);

        String expectedHeadings = "All Devices";
        recordsPage = mainNavigationBar.clickRecords();
        allDevices = recordsPage.clickOnAllDevices();

        //Go to all devices page
        boolean isHeadingVisibleAndCorrect = allDevices.isHeadingCorrect(expectedHeadings);
        boolean isItemsDisplayedAndCorrect = allDevices.isItemsDisplayed(expectedHeadings);
        //Verify results
        Assert.assertThat("Heading should be : " + expectedHeadings, isHeadingVisibleAndCorrect, is(true));
        Assert.assertThat("Expected to see at least 1 item", isItemsDisplayedAndCorrect, is(true));

    }

    @Test
    public void asABusinessUserIShouldBeAbleToViewAllProductsPage() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        MainNavigationBar mainNavigationBar = loginPage.loginAs(username, password);

        String expectedHeadings = "All Products";
        recordsPage = mainNavigationBar.clickRecords();
        allProducts = recordsPage.clickOnAllProducts();

        //Go to all devices page
        boolean isHeadingVisibleAndCorrect = allProducts.isHeadingCorrect(expectedHeadings);
        boolean isItemsDisplayedAndCorrect = allProducts.isItemsDisplayed(expectedHeadings);
        //Verify results
        Assert.assertThat("Heading should be : " + expectedHeadings, isHeadingVisibleAndCorrect, is(true));
        Assert.assertThat("Expected to see at least 1 item", isItemsDisplayedAndCorrect, is(true));

    }

    @Test
    public void asABusinessUserIShouldBeAbleToViewAllOrganisationPage() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        MainNavigationBar mainNavigationBar = loginPage.loginAs(username, password);

        String expectedHeadings = "All Organisations";
        recordsPage = mainNavigationBar.clickRecords();
        allOrganisations = recordsPage.clickOnAllOrganisations();

        //Go to all devices page
        boolean isHeadingVisibleAndCorrect = allOrganisations.isHeadingCorrect(expectedHeadings);
        boolean isItemsDisplayedAndCorrect = allOrganisations.isItemsDisplayed(expectedHeadings);
        //Verify results
        Assert.assertThat("Heading should be : " + expectedHeadings, isHeadingVisibleAndCorrect, is(true));
        Assert.assertThat("Expected to see at least 1 item", isItemsDisplayedAndCorrect, is(true));

    }

    @Test
    public void asABusinessUserIShouldBeAbleToViewDevicesPage() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        MainNavigationBar mainNavigationBar = loginPage.loginAs(username, password);

        String expectedHeadings = "Devices";
        recordsPage = mainNavigationBar.clickRecords();
        devices = recordsPage.clickOnDevices();

        //Go to all devices page
        boolean isHeadingVisibleAndCorrect = devices.isHeadingCorrect(expectedHeadings);
        boolean isItemsDisplayedAndCorrect = devices.isItemsDisplayed(expectedHeadings);
        //Verify results
        Assert.assertThat("Heading should be : " + expectedHeadings, isHeadingVisibleAndCorrect, is(true));
        Assert.assertThat("Expected to see at least 1 item", isItemsDisplayedAndCorrect, is(true));

    }


    @Ignore
    public void asABusinessUserIShouldBeAbleToCreateAccountRequest() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        MainNavigationBar mainNavigationBar = loginPage.loginAs(username, password);

        //go to accounts page > test harness page
        actionsPage = mainNavigationBar.clickActions();
        createTestsData = actionsPage.gotoTestsHarnessPage();

        //Now create the test data using harness page
        AccountRequest ar = new AccountRequest();
        actionsPage = createTestsData.createTestOrganisation(ar);

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

        //If its still not found than try the first 1 again
        if (!contains) {
            taskSection = tasksPage.clickOnTaskNumber(0);
            isCorrectTask = taskSection.isCorrectTask(orgName);
        }

        assertThat("Task not found for organisation : " + orgName, contains, is(equalTo(true)));

    }
}
