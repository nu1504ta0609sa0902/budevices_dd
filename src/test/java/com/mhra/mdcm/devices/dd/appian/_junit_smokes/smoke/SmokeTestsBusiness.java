package com.mhra.mdcm.devices.dd.appian._junit_smokes.smoke;

import com.mhra.mdcm.devices.dd.appian._junit_smokes.common.Common;
import com.mhra.mdcm.devices.dd.appian.domains.junit.User;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.AccountRequest;
import com.mhra.mdcm.devices.dd.appian.pageobjects.LoginPage;
import com.mhra.mdcm.devices.dd.appian.pageobjects.MainNavigationBar;
import com.mhra.mdcm.devices.dd.appian.utils.datadriven.ExcelDataSheet;
import com.mhra.mdcm.devices.dd.appian.utils.datadriven.JUnitUtils;
import com.mhra.mdcm.devices.dd.appian.utils.driver.BrowserConfig;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.FileUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.RandomDataUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.PageUtils;
import org.hamcrest.Matchers;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

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

    public static final String MANUFACTURER_SMOKE_TEST = RandomDataUtils.getRandomTestNameWithTodaysDate("ManufacturerAccountST","");;
    public static final String AUTHORISED_REP_SMOKE_TEST = RandomDataUtils.getRandomTestNameWithTodaysDate("AuthorisedRepAccountST","");
    public static final String DISTRIBUTOR_SMOKE_TEST = RandomDataUtils.getRandomTestNameWithTodaysDate("DistributorAccountST","");;

    public static String baseUrl;
    private String username;
    private String password;
    private String initials;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<User> spreadsheetData() throws IOException {
        ExcelDataSheet excelUtils = new ExcelDataSheet();//
        List<User> listOfUsers = excelUtils.getListOfUsers(userLoginData, profile, false);
        listOfUsers = excelUtils.filterUsersBy(listOfUsers, "business", initialsArray.get(0));
        log.info("Business Users : " + listOfUsers);
        return listOfUsers;
    }


    public SmokeTestsBusiness(User user) {
        this.username = user.getUserName();
        this.password = user.getPassword();
        this.initials = user.getInitials();
        loggedInUser = this.username;
    }

    @BeforeClass
    public static void setUpDriver() {
        if (driver == null) {
            driver = new BrowserConfig().getDriver();
            baseUrl = FileUtils.getTestUrl();

            //This is for entering values in a popup
            PageUtils.performBasicAuthentication(driver, baseUrl);

            log.warn("\n\nRUNNING BUSINESS SMOKE TESTS");
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
        loginPage = loginPage.accetpTandC();
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
        loginPage = loginPage.accetpTandC();
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


    @Test
    public void asABusinessUserIShouldBeAbleToViewAccountsPage() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        loginPage = loginPage.accetpTandC();
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
    public void asABusinessUserIShouldBeAbleToViewApplicationsPage() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        loginPage = loginPage.accetpTandC();
        MainNavigationBar mainNavigationBar = loginPage.loginAs(username, password);

        String expectedHeadings = "Applications";
        recordsPage = mainNavigationBar.clickRecords();
        applications = recordsPage.clickOnApplications();

        boolean isHeadingVisibleAndCorrect = applications.isHeadingCorrect(expectedHeadings);
        boolean isItemsDisplayedAndCorrect = applications.isItemsDisplayed(expectedHeadings);
        //Verify results
        Assert.assertThat("Heading should be : " + expectedHeadings, isHeadingVisibleAndCorrect, is(true));
        Assert.assertThat("Expected to see at least 1 item", isItemsDisplayedAndCorrect, is(true));

    }

    @Test
    @Ignore
    public void asABusinessUserIShouldBeAbleToViewCFSServicesPage() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        loginPage = loginPage.accetpTandC();
        MainNavigationBar mainNavigationBar = loginPage.loginAs(username, password);

        String expectedHeadings = "CFS Services";
        recordsPage = mainNavigationBar.clickRecords();
        cfsOrganisations = recordsPage.clickOnCFSServices();

        boolean isHeadingVisibleAndCorrect = cfsOrganisations.isHeadingCorrect(expectedHeadings);
        boolean isItemsDisplayedAndCorrect = cfsOrganisations.isItemsDisplayed(expectedHeadings);
        //Verify results
        Assert.assertThat("Heading should be : " + expectedHeadings, isHeadingVisibleAndCorrect, is(true));
        Assert.assertThat("Expected to see at least 1 item", isItemsDisplayedAndCorrect, is(true));

    }


    @Test
    public void asABusinessUserIShouldBeAbleToViewGMDNDevicesPage() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        loginPage = loginPage.accetpTandC();
        MainNavigationBar mainNavigationBar = loginPage.loginAs(username, password);

        String expectedHeadings = "GMDN Devices";
        recordsPage = mainNavigationBar.clickRecords();
        devicesGMDN = recordsPage.clickOnGMDNDevices();

        //Go to all devices page
        boolean isHeadingVisibleAndCorrect = devicesGMDN.isHeadingCorrect(expectedHeadings);
        boolean isItemsDisplayedAndCorrect = devicesGMDN.isItemsDisplayed(expectedHeadings);
        //Verify results
        Assert.assertThat("Heading should be : " + expectedHeadings, isHeadingVisibleAndCorrect, is(true));
        Assert.assertThat("Expected to see at least 1 item", isItemsDisplayedAndCorrect, is(true));

    }

    @Test
    public void asABusinessUserIShouldBeAbleToViewRegisteredProductsPage() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        loginPage = loginPage.accetpTandC();
        MainNavigationBar mainNavigationBar = loginPage.loginAs(username, password);

        String expectedHeadings = "Registered Products";
        recordsPage = mainNavigationBar.clickRecords();
        registeredProducts = recordsPage.clickOnRegisteredProducts();

        //Go to all devices page
        boolean isHeadingVisibleAndCorrect = registeredProducts.isHeadingCorrect(expectedHeadings);
        boolean isItemsDisplayedAndCorrect = registeredProducts.isItemsDisplayed(expectedHeadings);
        //Verify results
        Assert.assertThat("Heading should be : " + expectedHeadings, isHeadingVisibleAndCorrect, is(true));
        Assert.assertThat("Expected to see at least 1 item", isItemsDisplayedAndCorrect, is(true));

    }

    @Test
    public void asABusinessUserIShouldBeAbleToViewOrganisationPage() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        loginPage = loginPage.accetpTandC();
        MainNavigationBar mainNavigationBar = loginPage.loginAs(username, password);

        String expectedHeadings = "Organisations";
        recordsPage = mainNavigationBar.clickRecords();
        organisations = recordsPage.clickOnOrganisations();

        //Go to all devices page
        boolean isHeadingVisibleAndCorrect = organisations.isHeadingCorrect(expectedHeadings);
        boolean isItemsDisplayedAndCorrect = organisations.isItemsDisplayed(expectedHeadings);
        //Verify results
        Assert.assertThat("Heading should be : " + expectedHeadings, isHeadingVisibleAndCorrect, is(true));
        Assert.assertThat("Expected to see at least 1 item", isItemsDisplayedAndCorrect, is(true));

    }

    @Test
    public void asABusinessUserIShouldBeAbleToViewRegisteredDevicesPage() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        loginPage = loginPage.accetpTandC();
        MainNavigationBar mainNavigationBar = loginPage.loginAs(username, password);

        String expectedHeadings = "Registered Devices";
        recordsPage = mainNavigationBar.clickRecords();
        registeredDevices = recordsPage.clickOnRegisteredDevices();

        //Go to all devices page
        boolean isHeadingVisibleAndCorrect = registeredDevices.isHeadingCorrect(expectedHeadings);
        boolean isItemsDisplayedAndCorrect = registeredDevices.isItemsDisplayed(expectedHeadings);
        //Verify results
        Assert.assertThat("Heading should be : " + expectedHeadings, isHeadingVisibleAndCorrect, is(true));
        Assert.assertThat("Expected to see at least 1 item", isItemsDisplayedAndCorrect, is(true));

    }



    @Test
    public void businessUsersCanCreateManufacturerAccountRequest() {

        //New account data
        AccountRequest ar = new AccountRequest();
        ar.isManufacturer = true;
        ar.updateName(MANUFACTURER_SMOKE_TEST);
        ar.updateNameEnding("_" + initials);
        ar.initials = initials;
        ar.organisationRole = "Manufacturer";
        ar.setUserDetails(username);

        //Now create the test data using harness page
        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        loginPage = loginPage.accetpTandC();
        MainNavigationBar mainNavigationBar = loginPage.loginAs(username, password);

        //go to accounts page > test harness page
        actionsPage = mainNavigationBar.clickActions();
        createTestsData = actionsPage.gotoTestsHarnessPage();

        actionsPage = createTestsData.createNewAccountUsingBusinessTestHarness(ar);
        boolean isInCorrectPage = actionsPage.isApplicationSubmittedSuccessfully();
        if(!isInCorrectPage){
            PageUtils.acceptAlert(driver, true);
            actionsPage = createTestsData.createNewAccountUsingBusinessTestHarness(ar);
            isInCorrectPage = actionsPage.isApplicationSubmittedSuccessfully();
        }else{
            log.info("Created a new account : " + ar.organisationName);
        }

        String orgName = ar.organisationName;
        String accountNameOrReference = actionsPage.getApplicationReferenceNumber();
        System.out.println("New account reference number : " + accountNameOrReference);

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
            taskSection.isSearchingCompleted();

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
            taskSection = taskSection.approveAWIPTaskNewAccount();
            taskSection = taskSection.confirmAssignment(true);
        }

        assertThat("Task not found for organisation : " + orgName, contains, is(equalTo(true)));

        //Add email checking here
        //String subject = "Account request approved for";
        //boolean isEmailReceived = EmailUtils.verifyEmailReceived(accountNameOrReference,subject);
        //assertThat("Email with subject heading : " + subject + ", And body containing : " + accountNameOrReference, isEmailReceived, is(equalTo(true)));
    }


    @Test
    public void businessUsersCanCreateAuthorisedRepAccountRequest() {

        //Actual account data
        AccountRequest ar = new AccountRequest();
        ar.isManufacturer = false;
        ar.updateName(AUTHORISED_REP_SMOKE_TEST);
        ar.updateNameEnding("_" + initials);
        ar.initials = initials;
        ar.setUserDetails(username);

        //Now create the test data using harness page
        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        loginPage = loginPage.accetpTandC();
        MainNavigationBar mainNavigationBar = loginPage.loginAs(username, password);

        //go to accounts page > test harness page
        actionsPage = mainNavigationBar.clickActions();
        createTestsData = actionsPage.gotoTestsHarnessPage();

        actionsPage = createTestsData.createNewAccountUsingBusinessTestHarness(ar);
        boolean isInCorrectPage = actionsPage.isApplicationSubmittedSuccessfully();
        if(!isInCorrectPage){
            PageUtils.acceptAlert(driver, true);
            actionsPage = createTestsData.createNewAccountUsingBusinessTestHarness(ar);
        }else{
            log.info("Created a new account : " + ar.organisationName);
        }

        String orgName = ar.organisationName;
        String accountNameOrReference = actionsPage.getApplicationReferenceNumber();
        System.out.println("New account reference number : " + accountNameOrReference);

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
            taskSection.isSearchingCompleted();

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
            taskSection = taskSection.approveAWIPTaskNewAccount();
            taskSection = taskSection.confirmAssignment(true);
        }

        assertThat("Task not found for organisation : " + orgName, contains, is(equalTo(true)));

        //Add email checking here
        //String subject = "Account request approved for";
        //boolean isEmailReceived = EmailUtils.verifyEmailReceived(accountNameOrReference,subject);
        //assertThat("Email with subject heading : " + subject + ", And body containing : " + accountNameOrReference, isEmailReceived, is(equalTo(true)));

    }


    @Test
    public void businessUsersCanCreateDistributorAccountRequest() {

        //Actual data
        AccountRequest ar = new AccountRequest();
        ar.isManufacturer = true;
        ar.updateName(DISTRIBUTOR_SMOKE_TEST);
        ar.updateNameEnding("_" + initials);
        ar.initials = initials;
        ar.organisationRole = "Distributor";
        ar.setUserDetails(username);

        //Now create the test data using harness page
        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        loginPage = loginPage.accetpTandC();
        MainNavigationBar mainNavigationBar = loginPage.loginAs(username, password);

        //go to accounts page > test harness page
        actionsPage = mainNavigationBar.clickActions();
        createTestsData = actionsPage.gotoTestsHarnessPage();

        actionsPage = createTestsData.createNewAccountUsingBusinessTestHarness(ar);
        boolean isInCorrectPage = actionsPage.isApplicationSubmittedSuccessfully();
        if(!isInCorrectPage){
            PageUtils.acceptAlert(driver, true);
            actionsPage = createTestsData.createNewAccountUsingBusinessTestHarness(ar);
        }

        boolean createdSuccessfully = actionsPage.isApplicationSubmittedSuccessfully();
        if (createdSuccessfully) {
            log.info("Created a new account : " + ar.organisationName);
        }

        String orgName = ar.organisationName;
        String accountNameOrReference = actionsPage.getApplicationReferenceNumber();
        System.out.println("Account reference generated : " + accountNameOrReference);

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
            taskSection.isSearchingCompleted();

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
            taskSection = taskSection.approveAWIPTaskNewAccount();
            taskSection = taskSection.confirmAssignment(true);
        }

        assertThat("Task not found for organisation : " + orgName, contains, is(equalTo(true)));

        //Add email checking here
        //String subject = "Account request approved for";
        //boolean isEmailReceived = EmailUtils.verifyEmailReceived(accountNameOrReference,subject);
        //assertThat("Email with subject heading : " + subject + ", And body containing : " + accountNameOrReference, isEmailReceived, is(equalTo(true)));
    }


    /**
     * Account Page: Could be removed, so any tests related to accounts page may need to be removed
     */
    @Test
    @Ignore
    public void asABusinessUserIAmAbleToSearchViewAndEditManufacturerAccounts() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        loginPage = loginPage.accetpTandC();
        MainNavigationBar mainNavigationBar = loginPage.loginAs(username, password);

        //Go to accounts and perform a search for Manufacturer
        recordsPage = mainNavigationBar.clickRecords();
        accounts = recordsPage.clickOnAccounts();
        accounts = accounts.searchForAccount("ManufacturerAccountST");

        String randomAccountName = accounts.getARandomAccount();
        System.out.println("Search for : " + randomAccountName);
        accounts = accounts.viewSpecifiedAccount(randomAccountName);
        accounts = this.accounts.gotoEditAccountInformation();

        //Verify page is open to be edited
        boolean isInEditMode = accounts.isInEditMode();
        assertThat("Expected to be in edit view : " + randomAccountName, isInEditMode, is(equalTo(true)));
    }


    /**
     * Account Page: Could be removed, so any tests related to accounts page may need to be removed
     */
    @Test
    @Ignore
    public void asABusinessUserIAmAbleToSearchViewAndEditAuthorisedRepAccounts() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        loginPage = loginPage.accetpTandC();
        MainNavigationBar mainNavigationBar = loginPage.loginAs(username, password);

        //Go to accounts and perform a search for AuthorisedRep
        recordsPage = mainNavigationBar.clickRecords();
        accounts = recordsPage.clickOnAccounts();
        accounts = accounts.searchForAccount("AuthorisedRepAccountST");

        String randomAccountName = accounts.getARandomAccount();
        System.out.println("Search for : " + randomAccountName);
        accounts = accounts.viewSpecifiedAccount(randomAccountName);
        accounts = this.accounts.gotoEditAccountInformation();

        //Verify page is open to be edited
        boolean isInEditMode = accounts.isInEditMode();
        assertThat("Expected to be in edit view : " + randomAccountName, isInEditMode, is(equalTo(true)));
    }

    @Test
    public void userIsAbleToViewApplicationWIPTableAndSortTheData() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        loginPage = loginPage.accetpTandC();
        MainNavigationBar mainNavigationBar = loginPage.loginAs(username, password);
        tasksPage = mainNavigationBar.clickTasks();

        //Verify data is correctly displayed
        taskSection = tasksPage.gotoApplicationWIPPage();
        boolean wipRowsDisplayed = tasksPage.isApplicationWIPTableDisplayingData();
        assertThat("Expected to see at least 1 item by default " , wipRowsDisplayed, is(equalTo(true)));

        //Sort and verify data is displayed
        taskSection = taskSection.sortBy("Date", 1);
        wipRowsDisplayed = tasksPage.isApplicationWIPTableDisplayingData();
        assertThat("Expected to see at least 1 item by default " , wipRowsDisplayed, is(equalTo(true)));

        taskSection = taskSection.sortBy("Date", 1);
        wipRowsDisplayed = tasksPage.isApplicationWIPTableDisplayingData();
        assertThat("Expected to see at least 1 item by default " , wipRowsDisplayed, is(equalTo(true)));
    }

    @Override
    public String toString() {
        return "SmokeTestsBusiness";
    }
}
