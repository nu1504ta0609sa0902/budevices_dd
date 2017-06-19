package com.mhra.mdcm.devices.dd.appian._junit_smokes.device_injection.directly;

import com.mhra.mdcm.devices.dd.appian._junit_smokes.common.Common;
import com.mhra.mdcm.devices.dd.appian.domains.junit.User;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.AccountRequest;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.DeviceData;
import com.mhra.mdcm.devices.dd.appian.pageobjects.LoginPage;
import com.mhra.mdcm.devices.dd.appian.pageobjects.MainNavigationBar;
import com.mhra.mdcm.devices.dd.appian.pageobjects.external.ExternalHomePage;
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
 * Created by TPD_Auto on 01/11/2016.
 */
@RunWith(Parameterized.class)
public class _AllInOne_AddDevicesToNEWManufacturerAccounts_Main extends Common {

    public static final String MANUFACTURER_SMOKE_TEST = "ManufacturerAccountST";

    public static WebDriver driver;
    public static String baseUrl;
    private String username;
    private String password;
    private String initials;


    public _AllInOne_AddDevicesToNEWManufacturerAccounts_Main(User user) {
        this.username = user.getUserName();
        this.password = user.getPassword();
        this.initials = user.getInitials();
    }

    public static void main(String[] args) {
        List<User> listOfManufacturerUsers = ExcelDirectDeviceDataUtils.getListOfUsersFromExcel("manufacturer");
        List<User> listOfBusinessUsers = ExcelDirectDeviceDataUtils.getListOfBusinessUsersFromExcel("business");
        List<DeviceData> listOfDeviceData = ExcelDirectDeviceDataUtils.getListOfDeviceData();
        setUpDriver();

        for (User u : listOfBusinessUsers) {
            try {
                //Always use one of the Business Accounts to create the test manufacturers
                //REMEMBER ALL PREVIOUS MANUFACTURERS DATA WILL BE REMOVED
                String initials = u.getInitials();
                User businessUser = setCorrectLoginDetails("_" + initials, listOfBusinessUsers);
                _AllInOne_AddDevicesToNEWManufacturerAccounts_Main tgs = new _AllInOne_AddDevicesToNEWManufacturerAccounts_Main(businessUser);

                //We only want to do it if the INITIALS in our initialsArray list
                boolean isInitialFound = tgs.isInitialsInTheList(businessUser.getInitials());
                if (isInitialFound) {

                    //Create a new account for the manufacturer user
                    User manufacturerUser = TestHarnessUtils.getUserWithInitials(initials, listOfManufacturerUsers);
                    AccountRequest ar = tgs.createManufacturerAccountWithBusinessTestHarness(businessUser, manufacturerUser);

                    //View email and get login details
                    String tempPassword = tgs.waitForEmailWithTemporaryPassword(ar, "account creation");
                    tgs.changePasswordAndLogin("MHRA12345A", ar);


                    //All data cleared:Provide indication of devices made
                    //Once password is changed I should be logged in
                    tgs.createByLoggingIntoAccountWithInitials(ar, manufacturerUser, businessUser, listOfDeviceData);
                } else {
                    System.out.println("Not creating any data for : " + businessUser + "\nCheck initialsArray contains the initials : " + businessUser.getInitials());
                }

            } catch (Exception e) {
                System.out.println("Try and setup data for next user ");
            }
        }
    }

    private void changePasswordAndLogin(String updatePasswordTo, AccountRequest ar) {
        loginPage = loginPage.logoutIfLoggedInOthers();
        loginPage = loginPage.loadPage(baseUrl);
        mainNavigationBar = loginPage.loginAs(ar.userName, ar.tempPassword);
        mainNavigationBar = loginPage.changePasswordTo(ar.tempPassword, updatePasswordTo);
        ar.newPassword = updatePasswordTo;
    }

    /**
     * We now receive an email with the temporary password 12/06/2017
     * <p>
     * Then we need to update the password before proceeding
     *
     * @param ar
     * @return
     */
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
     * Add devices to each of the manufacturers successfully created
     * @param ar
     * @param manufacturerUser
     * @param listOfDeviceData
     */
    private void createByLoggingIntoAccountWithInitials(AccountRequest ar, User manufacturerUser, User businessUser, List<DeviceData> listOfDeviceData) {
        try {
            manufacturerUser.updateUsernamePassword(ar.userName, ar.newPassword);
            String nameSelected = manufacturerUser.getUserName();

            //log.info("Provide Indication Of Devices For : " + manufacturerName);
            provideIndicationOfDevicesMade(manufacturerUser);

            System.out.println("Try And Add Devices For : " + manufacturerUser);
            createDevicesFor(manufacturerUser, nameSelected, businessUser, listOfDeviceData);
            System.out.println("Create Devices For : " + nameSelected);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void setUpDriver() {
        System.setProperty("current.browser", "gc");
        if (driver == null) {
            //listOfDeviceData = excelUtils.getListOfDeviceData("configs/data/excel/DevicesData.xlsx", "TestDataWellFormed_Simple");
            driver = new BrowserConfig().getDriver();
            driver.manage().window().maximize();
            baseUrl = FileUtils.getTestUrl();
            PageUtils.performBasicAuthentication(driver, baseUrl);
            log.warn("\n\nTHIS IS NOT JUNIT, THIS IS NOT JUNIT");
            log.warn("\n\nINSERT DEVICES AS MANUFACTURER USER VIA MAIN METHOD");
        }
    }


    private void provideIndicationOfDevicesMade(User selected) {

        //User selected = setCorrectLoginDetails(newOrganisationCreated, listOfUsers);
        setLoginDetails(selected);
        externalHomePage = new ExternalHomePage(driver);
        manufacturerList = externalHomePage.gotoListOfManufacturerPage();
        indicateDevices();

    }

    private void indicateDevices() {
        WaitUtils.nativeWaitInSeconds(3);
        for (int x = 0; x < 9; x++) {
            try {
                externalHomePage = externalHomePage.provideIndicationOfDevicesMade(x);
            } catch (Exception e) {
            }
        }

        //custom made
        try {
            externalHomePage.selectCustomMade(true);
        } catch (Exception e) {
        }

        createNewManufacturer = externalHomePage.submitIndicationOfDevicesMade(false);
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


    private void loginAndViewManufacturer(User manufacturerUser) {

        //Login to app and add devices to the manufacturer
        loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        mainNavigationBar = loginPage.loginAsManufacturer(manufacturerUser.getUserName(), manufacturerUser.getPassword());
        externalHomePage = mainNavigationBar.clickHome();

        //Click on a random manufacturer
        boolean isDisabled = true;
        int count = 0;
        do {
            externalHomePage = mainNavigationBar.clickHome();
            WaitUtils.nativeWaitInSeconds(2);
            isDisabled = externalHomePage.isGotoListOfManufacturerPageLinkDisabled();
            count++;
        } while (isDisabled && count < 10);

        if (isDisabled) {
            externalHomePage = mainNavigationBar.clickHome();
            WaitUtils.nativeWaitInSeconds(3);
        }

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
    }

    private String selectNewAccountForAddingDevices(){

        //externalHomePage = mainNavigationBar.clickHome();
        //manufacturerList = externalHomePage.gotoListOfManufacturerPage();

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


    private AccountRequest createManufacturerAccountWithBusinessTestHarness(User businessUser, User manufacturerUser) {
        List<String> listOfManufactuersCreatedWithTesterInitials = new ArrayList<>();

        AccountRequest ar = new AccountRequest();
        ar.isManufacturer = true;
        ar.updateName(MANUFACTURER_SMOKE_TEST);
        ar.updateNameEnding("_" + initials);
        ar.initials = businessUser.getInitials();
        ar.setUserDetails(manufacturerUser.getUserName());
        //ar.firstName = TestHarnessUtils.getName(initials, businessUser, true);
        //ar.lastName = TestHarnessUtils.getName(initials, businessUser, false);
        ar.initials = initials;

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
            }

            boolean createdSuccessfully = actionsPage.isApplicationSubmittedSuccessfully();
            if (createdSuccessfully) {
                System.out.println("Created a new account : " + ar.organisationName);
            }

            String orgName = ar.organisationName;
            String accountNameOrReference = actionsPage.getApplicationReferenceNumber();
            log.info("New reference created : " + accountNameOrReference);

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
            }

            listOfManufactuersCreatedWithTesterInitials.add(orgName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        WaitUtils.nativeWaitInSeconds(2);
        System.out.println("Organisation name : " + ar.organisationName);
        loginPage.logoutIfLoggedIn();
        WaitUtils.nativeWaitInSeconds(2);

        return ar;
    }

    private void createDevicesFor(User manufacturerUser, String manufacturerName, User businessUser, List<DeviceData> listOfDeviceData) {

        //loginAndViewManufacturer(manufacturerUser);
        String orgName = selectNewAccountForAddingDevices();

        String[] deviceTypes = new String[]{
                "all devices", //"general medical", "vitro diagnostic", "active implantable", "procedure pack"
        };

        List<DeviceData> listOfDevicesWhichHadProblems = new ArrayList<>();

        for (String specificDeviceTypes : deviceTypes) {

            //Assumes we are in add device page
            List<DeviceData> listOfDevicesOfSpecificType = getListOfDevicesOfSpecificType(listOfDeviceData, specificDeviceTypes);
            listOfDevicesOfSpecificType = getValidatedDataOnly(true, listOfDevicesOfSpecificType);
            int count = 0;

            //Lets try to add multiple devices, it will take a long time
            for (DeviceData dd : listOfDevicesOfSpecificType) {

                if (dd.validatedData.toLowerCase().equals("y")) {
                    try {
                        //Only for DEBUGGING
                        System.out.println("\n----------------------------------------------------------");
                        System.out.println("Product number : " + (count + 1));
                        System.out.println("Device Type : " + dd);
                        System.out.println("----------------------------------------------------------\n");

                        addDevices = addDevices.addFollowingDevice(dd);
                        boolean isVisible = addDevices.isOptionToAddAnotherDeviceVisible();
                        if (!isVisible) {
                            System.out.println("\nERROR ::::: Problem adding device TRY AGAIN");
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

                        //REMOVE - REMOVE -
//                        if (count > 5) {
//                            break;
//                        }

                        //Try adding another device
                        if (isVisible && count < listOfDevicesOfSpecificType.size())
                            addDevices = addDevices.addAnotherDevice();
                        else
                            break;

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("\nERROR ::::: Problem adding device");
                        listOfDevicesWhichHadProblems.add(dd);
                        count++;
                    }
                } else {
                    System.out.println("\n----------------------------------------------------------");
                    System.out.println("Device Data Not Validated : \n" + dd.excelFileLineNumber);
                    System.out.println("----------------------------------------------------------\n");
                }
            }

            printFailingData(listOfDevicesWhichHadProblems, specificDeviceTypes);

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
                }
            } catch (Exception e) {

            }

            //Confirm
            addDevices = addDevices.proceedToReview();
            addDevices = addDevices.proceedToPayment();
            addDevices = addDevices.enterPaymentDetails("BACS");   //OR BACS
            String reference = addDevices.getApplicationReferenceNumber();
            log.info("New Applicaiton reference number : " + reference);
            //addDevices = addDevices.confirmPayment();
            manufacturerList = addDevices.backToService();

            //@todo Now login as business user and approve the task
            WaitUtils.nativeWaitInSeconds(5);
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
            if(contains) {
                taskSection = taskSection.assignTaskToMe();
                taskSection = taskSection.confirmAssignment(true);
                taskSection = taskSection.approveAWIPManufacturerTask();
                taskSection = taskSection.approveAWIPAllDevices();
                taskSection = taskSection.completeTheApplication();
                WaitUtils.nativeWaitInSeconds(5);
            }

            //assertThat("Task not found for organisation : " + orgName, contains, is(equalTo(true)));

            //Update status
            //registered = "registered";

            //Logback in now
            WaitUtils.nativeWaitInSeconds(5);
            loginPage.logoutIfLoggedIn();
            //loginAndViewManufacturer();

            log.info("\nCREATED NEW MANUFACTURER WITH DEVICES : COMPLETED NOW");
        }
    }

    private DeviceData getDeviceDataCalled(List<DeviceData> listOfDevicesWhichHadProblems, String abacus) {
        DeviceData dd = null;
        for (DeviceData data : listOfDevicesWhichHadProblems) {
            String definition = data.device;
            if (definition.equals(abacus)) {
                dd = data;
                break;
            }
        }
        return dd;
    }

    private List<DeviceData> getValidatedDataOnly(boolean onlyValidatedData, List<DeviceData> listOfDevicesOfSpecificType) {
        List<DeviceData> listOfValidatedData = new ArrayList<>();

        if (onlyValidatedData) {
            for (DeviceData dd : listOfDevicesOfSpecificType) {
                if (dd.validatedData.equals("Y")) {
                    listOfValidatedData.add(dd);
                }
            }
        } else {
            listOfValidatedData = listOfDevicesOfSpecificType;
        }
        return listOfValidatedData;

    }


    private List<DeviceData> getListOfDevicesOfSpecificType(List<DeviceData> listOfDeviceData, String specificType) {
        if (specificType.equals("all devices")) {
            return listOfDeviceData;
        } else {
            //Filter specific data
            List<DeviceData> listOfDevicesOfType = new ArrayList<>();
            for (DeviceData dd : listOfDeviceData) {
                if (dd.deviceType.contains(specificType)) {
                    listOfDevicesOfType.add(dd);
                }
            }
            return listOfDevicesOfType;
        }
    }

    private void printFailingData(List<DeviceData> listOfDevicesWhichHadProblems, String deviceType) {
        System.out.println("FINISHED ADDING DEVICE TYPE : " + deviceType);
        System.out.println("Number of invalid data : " + listOfDevicesWhichHadProblems.size());
        for (DeviceData data : listOfDevicesWhichHadProblems) {
            System.out.println("\n\nExcel File Line Number : " + data.excelFileLineNumber);
            System.out.println("Validated Data : " + data.validatedData);
            System.out.println("Device Type : " + data.deviceType);
            System.out.println("Device Term/Definition : " + data.device);
            System.out.println("Risk Classification : " + data.riskClassification);
        }
    }

    @Override
    public String toString() {
        return "CREATE DEVICES FOR Manufacturers";
    }
}
