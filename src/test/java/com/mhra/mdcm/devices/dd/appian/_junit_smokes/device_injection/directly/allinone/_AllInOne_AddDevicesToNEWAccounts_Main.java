package com.mhra.mdcm.devices.dd.appian._junit_smokes.device_injection.directly.allinone;

import com.mhra.mdcm.devices.dd.appian._junit_smokes.common.Common;
import com.mhra.mdcm.devices.dd.appian._junit_smokes.device_injection.directly.ExcelDirectDeviceDataUtils;
import com.mhra.mdcm.devices.dd.appian.domains.junit.User;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.AccountRequest;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.DeviceData;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.ManufacturerOrganisationRequest;
import com.mhra.mdcm.devices.dd.appian.pageobjects.LoginPage;
import com.mhra.mdcm.devices.dd.appian.pageobjects.MainNavigationBar;
import com.mhra.mdcm.devices.dd.appian.pageobjects.external.ExternalHomePage;
import com.mhra.mdcm.devices.dd.appian.pageobjects.external._CreateManufacturerTestsData;
import com.mhra.mdcm.devices.dd.appian.utils.driver.BrowserConfig;
import com.mhra.mdcm.devices.dd.appian.utils.email.GmailEmail;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.FileUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.TestHarnessUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.PageUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@RunWith(Parameterized.class)
public class _AllInOne_AddDevicesToNEWAccounts_Main extends Common {

    private static boolean isManufacturer = false;  //What type of accounts to create false=AuthorisedReps

    private static final String AUTHORISED_REP_ACCOUNT_SMOKE_TEST = "AuthorisedRepAccountST";
    private static final String AUTHORISED_REP_SMOKE_TEST = "AuthorisedRepST";
    private static final String MANUFACTURER_ACCOUNT_SMOKE_TEST = "ManufacturerAccountST";
    private static final String MANUFACTURER_SMOKE_TEST = "ManufacturerST";

    private static WebDriver driver;
    private static String baseUrl;
    private String username;
    private String password;
    private String initials;


    public _AllInOne_AddDevicesToNEWAccounts_Main(User user) {
        this.username = user.getUserName();
        this.password = user.getPassword();
        this.initials = user.getInitials();
    }

    public static void main(String[] args) {

        List<User> listOfUsersFromExcelSheet = ExcelDirectDeviceDataUtils.getListOfUsersFromExcel("manufacturer");
        if(isManufacturer)
            listOfUsersFromExcelSheet = ExcelDirectDeviceDataUtils.getListOfUsersFromExcel("authorised");
        List<User> listOfBusinessUsers = ExcelDirectDeviceDataUtils.getListOfBusinessUsersFromExcel("business");
        setUpDriver();

        for (User u : listOfBusinessUsers) {
            try {
                /**
                 * Always use one of the Business Accounts to create the test manufacturers
                 * This will create authorisedReps with users initials e.g _NU, _HB
                 */
                log.info("First CREATE New Accounts To Add Manufactures/Devices To : ");
                String initials = u.getInitials();
                User businessUser = ExcelDirectDeviceDataUtils.getCorrectLoginDetails("_" + initials, listOfBusinessUsers);
                _AllInOne_AddDevicesToNEWAccounts_Main tgs = new _AllInOne_AddDevicesToNEWAccounts_Main(businessUser);

                //We only want to do it if the INITIALS in our initialsArray list
                boolean isInitialFound = tgs.isInitialsInTheList(businessUser.getInitials());
                if (isInitialFound) {

                    //Get correct authorisedRep or Manufacturer user and create a new account
                    User user = TestHarnessUtils.getUserWithInitials(initials, listOfUsersFromExcelSheet);
                    AccountRequest ar = tgs.createNewAccountWithBusinessTestHarness(businessUser, user);

                    //View email and get temporary login details
                    String tempPassword = tgs.waitForEmailWithTemporaryPassword(ar, "account creation");
                    tgs.changePasswordAndLogin("MHRA12345A", ar);

                    /**
                     * Register new organisation with devices
                     */
                    log.info("Now create a new organisation and add devices to : ");
                    tgs.registerNewOrganisationsAndAddDevices(user, businessUser, ar);
                } else {
                    System.out.println("Not creating any data for : " + businessUser + "\nCheck initialsArray contains the initials : " + businessUser.getInitials());
                }
            } catch (Exception e) {
                System.out.println("Try and setup data for next user ");
            }
        }

        //closeDriver();
    }


    private void changePasswordAndLogin(String updatePasswordTo, AccountRequest ar) {
        loginPage = loginPage.logoutIfLoggedInOthers();
        loginPage = loginPage.loadPage(baseUrl);
        mainNavigationBar = loginPage.loginAs(ar.userName, ar.tempPassword);
        mainNavigationBar = loginPage.changePasswordTo(ar.tempPassword, updatePasswordTo);
        ar.newPassword = updatePasswordTo;
    }


    private String waitForEmailWithTemporaryPassword(AccountRequest ar, String subjectHeading) {
        String userName = ar.userName;
        boolean foundMessage = false;
        String messageBody = null;
        int attempt = 0;
        do {
            messageBody = GmailEmail.getMessageReceivedWithHeadingAndIdentifier(7, 10, subjectHeading, userName);

            //Break from loop if invoices read from the email server
            if (messageBody != null) {
                foundMessage = true;
                break;
            } else {
                //Wait for 10 seconds and try again, Thread.sleep required because this is checking email and its outside of selenium scope
                WaitUtils.nativeWaitInSeconds(10);
            }
            attempt++;
        } while (!foundMessage && attempt < 30);

        String tempPassword = null;
        if (messageBody != null) {
            tempPassword = messageBody.substring(messageBody.indexOf("d:") + 3, messageBody.indexOf("To log") - 1);
            ar.tempPassword = tempPassword;
        }
        return tempPassword;
    }

    public static void setUpDriver() {
        System.setProperty("current.browser", "gc");
        if (driver == null) {
            driver = new BrowserConfig().getDriver();
            driver.manage().window().maximize();
            baseUrl = FileUtils.getTestUrl();
            PageUtils.performBasicAuthentication(driver, baseUrl);
            log.warn("URL : " + baseUrl);
            log.warn("\n\nTHIS IS NOT JUNIT, THIS IS NOT JUNIT");
            log.warn("\n\nINSERT DEVICES AS AUTHORISEDREP USER VIA MAIN METHOD");
        }
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

    /**
     * For each accounts created with _UsersInitials create an organisation and add devices
     * <p>
     * Add devices to each of the manufacturers successfully created
     *
     * @param manufacturerUser
     * @param ar
     */
    private void registerNewOrganisationsAndAddDevices(User manufacturerUser, User businessUser, AccountRequest ar) {
        try {
            manufacturerUser.updateUsernamePassword(ar.userName, ar.newPassword);
            setLoginDetails(manufacturerUser);
            externalHomePage = new ExternalHomePage(driver);
            manufacturerList = externalHomePage.gotoListOfManufacturerPage();
            indicateDevices(false);

            //
            registerANewManufacturer();
            createAuthorisedRepsWithManufacturerTestHarness2(manufacturerUser);
            createDevicesFor(manufacturerUser, false, businessUser, ar);

            WaitUtils.nativeWaitInSeconds(2);
            //loginPage = loginPage.logoutIfLoggedIn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerANewManufacturer() {
        externalHomePage = externalHomePage.registerANewManufacturer();
    }

    private void logBackInAsManufacturer(User manufacturerUser) {
        //Assuming all previous data removed
        WaitUtils.nativeWaitInSeconds(3);
        loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        mainNavigationBar = loginPage.loginAsManufacturer(username, password);
        externalHomePage = mainNavigationBar.clickHome();

        //Click on a random manufacturer
        manufacturerList = externalHomePage.gotoListOfManufacturerPage();
    }

    private boolean acceptNewServiceRequest(User businessUser, String name) {
        log.info("Find and accept the tasks for : " + name);

        WaitUtils.nativeWaitInSeconds(5);
        loginPage = loginPage.logoutIfLoggedInOthers();
        WaitUtils.nativeWaitInSeconds(2);

        MainNavigationBar mainNavigationBar = loginPage.loginAs(businessUser.getUserName(), businessUser.getPassword());

        //Verify new taskSection generated and its the correct one
        boolean contains = false;
        boolean isCorrectTask = false;
        String taskType = "New Service";
        String orgName = name;
        int count = 0;
        do {
            //Refresh each time, it may take a while for the new task to arrive
            tasksPage = mainNavigationBar.clickTasks();

            //Click on link number X
            try {
                taskSection = tasksPage.clickOnLinkWithText(orgName);
                contains = true;
            } catch (Exception e) {
                contains = false;
            }
            if (!contains) {
                WaitUtils.nativeWaitInSeconds(2);
                count++;
            }
        } while (!contains && count <= 5);

        if (contains) {
            //accept the taskSection and approve or reject it
            taskSection = taskSection.acceptTask();
            if (taskType != null) {
                if (taskType.contains("New Service") || taskType.contains("New Account")) {
                    tasksPage = taskSection.approveTask();
                } else if (taskType.contains("New Manufacturer")) {
                    //tasksPage = taskSection.acceptRegistrationTask();
                } else if (taskType.contains("Update Manufacturer Registration Request")) {
                    tasksPage = taskSection.approveTask();
                }
            }

        }

        return contains;
    }

    private void indicateDevices(boolean clickNextBtn) {
        WaitUtils.nativeWaitInSeconds(3);
        for (int x = 0; x < 9; x++) {
            try {
                externalHomePage = externalHomePage.provideIndicationOfDevicesMade(x);
            } catch (Exception e) {
                //Lazy : not recommended
            }
        }

        //custom made
        try {
            externalHomePage.selectCustomMade(true);
        } catch (Exception e) {
        }

        //Submit devices made
        createNewManufacturer = externalHomePage.submitIndicationOfDevicesMade(clickNextBtn);
        WaitUtils.nativeWaitInSeconds(5);

    }

    private void loginAndGoToSetDeviceIndication() {

        //Login to app and add devices to the manufacturer
        loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        mainNavigationBar = loginPage.loginAsManufacturer(username, password);
        externalHomePage = mainNavigationBar.clickHome();

        //Click on a random manufacturer
        manufacturerList = externalHomePage.gotoListOfManufacturerPage();
    }


    private void setLoginDetails(User selected) {
        username = selected.getUserName();
        password = selected.getPassword();
    }


    private AccountRequest createNewAccountWithBusinessTestHarness(User businessUser, User user) {
        List<String> listOfAccountsCreatedWithTesterInitials = new ArrayList<>();
        AccountRequest ar = new AccountRequest();
        ar.country = "United Kingdom";
        if(isManufacturer) {
            ar.updateName(MANUFACTURER_ACCOUNT_SMOKE_TEST);
            ar.isManufacturer = true;
        }else{
            ar.updateName(AUTHORISED_REP_ACCOUNT_SMOKE_TEST);
            ar.isManufacturer = false;
        }

        ar.updateNameEnding("_" + businessUser.getInitials());
        ar.setUserDetails(user.getUserName());
        ar.initials = businessUser.getInitials();
        //ar.firstName = TestHarnessUtils.getName(initials, businessUser, true);
        //ar.lastName = TestHarnessUtils.getName(initials, businessUser, false);
        ar.initials = businessUser.getInitials();

        try {

            //Login and try to create it
            loginPage = new LoginPage(driver);
            loginPage = loginPage.loadPage(baseUrl);
            MainNavigationBar mainNavigationBar = loginPage.loginAs(businessUser.getUserName(), businessUser.getPassword());

            //go to accounts page > test harness page
            actionsPage = mainNavigationBar.clickActions();
            createTestsData = actionsPage.gotoTestsHarnessPage();

            actionsPage = createTestsData.createNewAccountUsingBusinessTestHarness(ar);
            boolean isInCorrectPage = actionsPage.isApplicationSubmittedSuccessfully();
            if (!isInCorrectPage) {
                actionsPage = mainNavigationBar.clickActions();
                createTestsData = actionsPage.gotoTestsHarnessPage();
                actionsPage = createTestsData.createNewAccountUsingBusinessTestHarness(ar);
            }

            boolean createdSuccessfully = actionsPage.isApplicationSubmittedSuccessfully();
            if (createdSuccessfully) {
                log.info("Created a new account with business : " + ar.organisationName);
            }

            String orgName = ar.organisationName;
            String accountNameOrReference = actionsPage.getApplicationReferenceNumber();
            log.info("Account reference : " + accountNameOrReference);

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
            if (contains) {
                taskSection = taskSection.assignTaskToMe();
                taskSection = taskSection.confirmAssignment(true);
                tasksPage = taskSection.approveTaskNewAccount();
                taskSection = taskSection.confirmAssignment(true);
                WaitUtils.nativeWaitInSeconds(5);
            }

            listOfAccountsCreatedWithTesterInitials.add(orgName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("Approved The Following Accounts : " + listOfAccountsCreatedWithTesterInitials + "\n");

        WaitUtils.nativeWaitInSeconds(2);
        //log.info(ar.organisationName);
        loginPage.logoutIfLoggedIn();
        WaitUtils.nativeWaitInSeconds(2);

        return ar;
    }

    private void createDevicesFor(User u, boolean loginAgain, User businessUser, AccountRequest ar) {
        log.info("Try And Add Devices For : " + ar.organisationName);

        List<DeviceData> listOfDeviceData = ExcelDirectDeviceDataUtils.getListOfDeviceData();


        //loginAndViewManufacturer(manufacturerUser);
        //String orgName = selectNewAccountForAddingDevices();
        String orgName = ar.organisationName;

        String[] deviceTypes = new String[]{
                "all devices", //"general medical", "vitro diagnostic", "active implantable", "procedure pack"
        };

        List<DeviceData> listOfDevicesWhichHadProblems = new ArrayList<>();

        for (String specificDeviceTypes : deviceTypes) {

            //Assumes we are in add device page
            List<DeviceData> listOfDevicesOfSpecificType = ExcelDirectDeviceDataUtils.getListOfDevicesOfSpecificType(listOfDeviceData, specificDeviceTypes);
            listOfDevicesOfSpecificType = ExcelDirectDeviceDataUtils.getValidatedDataOnly(true, listOfDevicesOfSpecificType);
            int count = 0;

            //Lets try to add multiple devices, it will take a long time
            for (DeviceData dd : listOfDevicesOfSpecificType) {

                if (dd.validatedData.toLowerCase().equals("y")) {
                    try {
                        //Only for DEBUGGING
                        log.info("\n----------------------------------------------------------");
                        log.info("Product number : " + (count + 1));
                        //log.info("Device Type : " + dd);
                        ExcelDirectDeviceDataUtils.printDeviceData(dd);
                        log.info("----------------------------------------------------------\n");

                        addDevices = addDevices.addFollowingDevice(dd);
                        boolean isVisible = addDevices.isOptionToAddAnotherDeviceVisible();
                        if (!isVisible) {
                            log.info("\nERROR ::::: Problem adding device TRY AGAIN");
                            //Try again :
                            addDevices = addDevices.addFollowingDevice(dd);
                            isVisible = addDevices.isOptionToAddAnotherDeviceVisible();
                            if (isVisible) {
                                count++;
                            } else {
                                throw new Exception("ERROR ::::: Problem adding device after 2 attempts");
                            }
                        } else {
                            count++;
                        }

                        //REMOVE REMOVE REMOVE
                        //if(count > 5){
                        //    break;
                        //}

                        //Try adding another device
                        if (isVisible && count < listOfDevicesOfSpecificType.size())
                            addDevices = addDevices.addAnotherDevice();
                        else
                            break;

                    } catch (Exception e) {
                        e.printStackTrace();
                        log.info("\nERROR ::::: Problem adding device");
                        listOfDevicesWhichHadProblems.add(dd);
                        count++;
                        //clickAddAnotherButton();
                    }
                } else {
                    log.info("\n----------------------------------------------------------");
                    log.info("Device Data Not Validated : \n" + dd.excelFileLineNumber);
                    log.info("----------------------------------------------------------\n");
                    clickAddAnotherButton();
                }
            }

            ExcelDirectDeviceDataUtils.printFailingData(listOfDevicesWhichHadProblems, specificDeviceTypes);

            //Verify option to add another device is there
            try {
                boolean isVisible = addDevices.isOptionToAddAnotherDeviceVisible();
                if (!isVisible) {
                    DeviceData dd = ExcelDirectDeviceDataUtils.getDeviceDataCalled(listOfDevicesWhichHadProblems, "Abacus");
                    if (dd == null) {
                        //System keeps bloody changing the GMDN
                        dd = ExcelDirectDeviceDataUtils.getListOfDevicesOfSpecificType(listOfDeviceData, "general medical").get(0);
                    }
                    dd.device = "con";
                    addDevices = addDevices.addFollowingDevice(dd);
                    isVisible = addDevices.isOptionToAddAnotherDeviceVisible();
                    if (!isVisible) {
                        addDevices = addDevices.saveDevice();
                    }
                }
            } catch (Exception e) {

            }

            //Confirm payment and submit registration
            addDevices = addDevices.proceedToReview();
            addDevices = addDevices.proceedToPayment();
            addDevices = addDevices.enterPaymentDetails("BACS");   //OR BACS
            String reference = addDevices.getApplicationReferenceNumber();
            log.info("New Applicaiton reference number : " + reference);
            //addDevices = addDevices.confirmPayment();
            manufacturerList = addDevices.backToService();

            //@todo Now login as business user and approve the task
            WaitUtils.nativeWaitInSeconds(4);
            loginPage = loginPage.logoutIfLoggedInOthers();
            mainNavigationBar = loginPage.loginAs(businessUser.getUserName(), businessUser.getPassword());

            //Verify new taskSection generated and its the correct one
            boolean contains = false;
            boolean isCorrectTask = false;
            int not = 0;
            do {
                mainNavigationBar = new MainNavigationBar(driver);
                tasksPage = mainNavigationBar.clickTasks();
                taskSection = tasksPage.gotoApplicationWIPPage();
                PageUtils.acceptAlert(driver, true);

                //Search and view the application via reference number
                taskSection = taskSection.searchAWIPPageForAccount(reference);

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
            }

            System.out.println("Create Devices For : " + ar.organisationName);
            //Logback in now
            WaitUtils.nativeWaitInSeconds(3);
            loginPage.logoutIfLoggedIn();

            System.out.println("\nCREATED NEW AUTHORISED-REP WITH DEVICES : COMPLETED NOW");
        }
    }

    private String selectNewAccountForAddingDevices() {

        externalHomePage = mainNavigationBar.clickHome();
        manufacturerList = externalHomePage.gotoListOfManufacturerPage();

        //You will need to naviage to different pages to select the manufactuerer
        String name = manufacturerList.getARandomManufacturerName();
        String registered = manufacturerList.getRegistrationStatus(name);
        log.info("Manufacturer selected : " + name + ", is " + registered);
        manufacturerDetails = manufacturerList.viewAManufacturer(name);

        //Add devices: This needs to change to add all the devices
        try {
            if (registered != null && registered.toLowerCase().equals("registered")) {
                addDevices = manufacturerDetails.clickAddDeviceBtn();
            } else {
                addDevices = manufacturerDetails.clickDeclareDevicesBtn();
            }
        } catch (Exception e) {
            addDevices = manufacturerDetails.clickDeclareDevicesBtn();
        }

        return name;
    }

    private void clickAddAnotherButton() {
        try {
            boolean isVisible = addDevices.isOptionToAddAnotherDeviceVisible();
            if (isVisible) {
                addDevices = addDevices.addAnotherDevice();
            }
        } catch (Exception e) {

        }
    }


    private void createAuthorisedRepsWithManufacturerTestHarness2(User user) throws Exception {

        //Now create the test data using harness page
        ManufacturerOrganisationRequest ar = new ManufacturerOrganisationRequest();
        if(isManufacturer) {
            ar.updateName(MANUFACTURER_SMOKE_TEST);
            ar.isManufacturer = true;
        }else{
            ar.updateName(AUTHORISED_REP_SMOKE_TEST);
            ar.isManufacturer = false;
        }

        ar.updateNameEnding("_" + user.getInitials());
        ar.setUserDetails(username);
        ar.country = "United States";

        //Create new manufacturer data
        createNewManufacturer = new _CreateManufacturerTestsData(driver);
        addDevices = createNewManufacturer.createTestOrganisation(ar, false);
        if (createNewManufacturer.isErrorMessageDisplayed()) {
            externalHomePage = mainNavigationBar.clickExternalHOME();
            manufacturerList = externalHomePage.gotoListOfManufacturerPage();
            createNewManufacturer = manufacturerList.registerNewManufacturer();
            addDevices = createNewManufacturer.createTestOrganisation(ar, false);
        }


        log.info("Created a new org to add devices to : " + ar.organisationName);

    }


    @Override
    public String toString() {
        return "CREATE DEVICES FOR AuthorisedReps";
    }
}
