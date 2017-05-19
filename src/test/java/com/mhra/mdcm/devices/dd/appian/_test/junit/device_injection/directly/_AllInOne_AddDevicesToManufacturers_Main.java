package com.mhra.mdcm.devices.dd.appian._test.junit.device_injection.directly;

import com.mhra.mdcm.devices.dd.appian._test.junit.common.Common;
import com.mhra.mdcm.devices.dd.appian.domains.junit.User;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.AccountRequest;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.DeviceData;
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

import java.util.ArrayList;
import java.util.List;


/**
 * Created by TPD_Auto on 01/11/2016.
 */
@RunWith(Parameterized.class)
public class _AllInOne_AddDevicesToManufacturers_Main extends Common {

    private static User businessUser;

    //This controls and limits the users (overrides excel)
    public String[] initialsArray = new String[]{
            "AT", "NU", "HB", "YC", "PG", "AN", "LP"
    };

    public static final String MANUFACTURER_SMOKE_TEST = "ManufacturerAccountST";

    public static String registered = "registered";
    public static String nameSelected = null;


    private static List<String> listOfManufactuersCreatedWithTesterInitials = new ArrayList<>();
    private static List<User> listOfManufacturerUsers = new ArrayList<>();
    private static List<User> listOfBusinessUsers = new ArrayList<>();
    public static List<DeviceData> listOfDeviceData = new ArrayList<>();

    public static WebDriver driver;
    public static String baseUrl;
    private String username;
    private String password;
    private String initials;
    private User manufacturerUser;


    public _AllInOne_AddDevicesToManufacturers_Main(User user) {
        this.username = user.getUserName();
        this.password = user.getPassword();
        this.initials = user.getInitials();
    }

    public static void main(String[] args) {
        listOfManufacturerUsers = ExcelDirectDeviceDataUtils.getListOfUsersFromExcel("manufacturer");
        listOfBusinessUsers = ExcelDirectDeviceDataUtils.getListOfBusinessUsersFromExcel("business");
        setUpDriver();

        for (User u : listOfBusinessUsers) {
            try {
                //Always use one of the Business Accounts to create the test manufacturers
                //REMEMBER ALL PREVIOUS MANUFACTURERS DATA WILL BE REMOVED
                String initials = u.getInitials();
                businessUser = setCorrectLoginDetails("_" + initials, listOfBusinessUsers);
                _AllInOne_AddDevicesToManufacturers_Main tgs = new _AllInOne_AddDevicesToManufacturers_Main(businessUser);

                //We only want to do it if the INITIALS in our initialsArray list
                boolean isInitialFound = tgs.isInitialsInTheList(businessUser.getInitials());
                if (isInitialFound) {

                    //Create a new account for the manufacturer user
                    User manufacturerUser = ExcelDirectDeviceDataUtils.getCorrectLoginDetailsManufacturer(initials, listOfManufacturerUsers);
                    tgs.createManufacturerAccountWithBusinessTestHarness(manufacturerUser);

                    //All data cleared:Provide indication of devices made
                    //Create by logging into individual Account for the INITIALS
                    tgs.createByLoggingIntoAccountWithInitials(listOfManufacturerUsers);
                } else {
                    System.out.println("Not creating any data for : " + businessUser + "\nCheck initialsArray contains the initials : " + businessUser.getInitials());
                }

            } catch (Exception e) {
                System.out.println("Try and setup data for next user ");
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

    /**
     * Add devices to each of the manufacturers successfully created
     *
     * @param listOfUsers
     */
    private void createByLoggingIntoAccountWithInitials(List<User> listOfUsers) {

        List<String> listOfManufacturerNames = getListOfManufacturerNames();
        for (String manufacturerName : listOfManufacturerNames) {
            try {

                WaitUtils.nativeWaitInSeconds(5);
                loginPage = loginPage.logoutIfLoggedInOthers();
                WaitUtils.nativeWaitInSeconds(2);

                nameSelected = manufacturerName;
                manufacturerUser = setCorrectLoginDetails(nameSelected, listOfUsers);

                //log.info("Provide Indication Of Devices For : " + manufacturerName);
                //provideIndicationOfDevicesMade(manufacturerUser);

                log.info("Try And Add Devices For : " + manufacturerName);
                //_AllInOne_AddDevicesToManufacturers_Main tc = new _AllInOne_AddDevicesToManufacturers_Main(manufacturerUser);
                setLoginDetails(manufacturerUser);
                createDevicesFor(manufacturerUser, manufacturerName);
                log.info("Create Devices For : " + manufacturerName);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void setUpDriver() {
        System.setProperty("current.browser", "gc");
        if (driver == null) {
            listOfDeviceData = excelUtils.getListOfDeviceData("configs/data/excel/DevicesData.xlsx", "TestDataWellFormed_Simple");
            driver = new BrowserConfig().getDriver();
            driver.manage().window().maximize();
            baseUrl = FileUtils.getTestUrl();
            PageUtils.performBasicAuthentication(driver, baseUrl);
            log.warn("\n\nTHIS IS NOT JUNIT, THIS IS NOT JUNIT");
            log.warn("\n\nINSERT DEVICES AS MANUFACTURER USER VIA MAIN METHOD");
        }
    }


    private void provideIndicationOfDevicesMade(User selected) {

        //User selected = setCorrectLoginDetails(nameSelected, listOfUsers);
        setLoginDetails(selected);
        loginAndGoToSetDeviceIndication();
        indicateDevices();

        //Removed : 03/03 drop or around that date
        //acceptNewServiceRequest(businessUser);
        //WaitUtils.nativeWaitInSeconds(2);
        //loginPage = loginPage.logoutIfLoggedIn();

    }

    private void acceptNewServiceRequest(User businessUser) {

        MainNavigationBar mainNavigationBar = loginPage.loginAs(businessUser.getUserName(), businessUser.getPassword());

        //Verify new taskSection generated and its the correct one
        boolean contains = false;
        boolean isCorrectTask = false;
        String taskType = "New Service";
        String orgName = nameSelected;
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
        externalHomePage.selectCustomMade(true);

        //Submit devices made : They changed the work flow on 03/02/2017
        //createNewManufacturer = externalHomePage.submitIndicationOfDevicesMade(true);
        //createNewManufacturer = externalHomePage.submitIndicationOfDevicesMade(false);

        createNewManufacturer = externalHomePage.submitIndicationOfDevicesMade(false);
        WaitUtils.nativeWaitInSeconds(5);

        //loginPage = loginPage.logoutIfLoggedInOthers();
        //WaitUtils.nativeWaitInSeconds(2);

        //System.out.println("DONE");
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

    /**
     * UPDATE THIS MANUALLY FOR NOW
     * <p>
     * This is all the users created using : BusinessCreateManufacturersWithTestersInitials
     *
     * @return
     */
    private static List<String> getListOfManufacturerNames() {
//        listOfManufactuersCreatedWithTesterInitials.add("ManufacturerST_9_1_948798_NU");
//        listOfManufactuersCreatedWithTesterInitials.add("ManufacturerST91175215_HB");
//        listOfManufactuersCreatedWithTesterInitials.add("ManufacturerST_9_1_142303_YC");
//        listOfManufactuersCreatedWithTesterInitials.add("ManufacturerST_9_1_618693_PG");
//        listOfManufactuersCreatedWithTesterInitials.add("ManufacturerST_9_1_386957_AN");
//        listOfManufactuersCreatedWithTesterInitials.add("ManufacturerST91542581_LP");
        return listOfManufactuersCreatedWithTesterInitials;
    }


    private void loginAndViewManufacturer() {

        //Login to app and add devices to the manufacturer
        loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        mainNavigationBar = loginPage.loginAsManufacturer(username, password);
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
        String name = nameSelected;
        registered = manufacturerList.getRegistrationStatus(name);
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


    private void createManufacturerAccountWithBusinessTestHarness(User manufacturerUser) {

        //for (String initials : initialsArray) {
            initials = manufacturerUser.getInitials();
            AccountRequest ar = new AccountRequest();
            ar.isManufacturer = true;
            ar.updateName(MANUFACTURER_SMOKE_TEST);
            ar.updateNameEnding("_" + initials);
            ar.setUserDetails(username);
            ar.firstName = TestHarnessUtils.getName(initials, manufacturerUser, true);
            ar.lastName = TestHarnessUtils.getName(initials, manufacturerUser, false);

            try {

                loginPage = new LoginPage(driver);
                loginPage = loginPage.loadPage(baseUrl);
                MainNavigationBar mainNavigationBar = loginPage.loginAs(username, password);

                //go to accounts page > test harness page
                actionsPage = mainNavigationBar.clickActions();
                createTestsData = actionsPage.gotoTestsHarnessPage();

                //Now create the test data using harness page
                actionsPage = createTestsData.createNewAccountUsingBusinessTestHarness(ar);
                boolean isInCorrectPage = actionsPage.isInActionsPage();
                if (!isInCorrectPage) {
                    actionsPage = createTestsData.createNewAccountUsingBusinessTestHarness(ar);
                }

                boolean createdSuccessfully = actionsPage.isInActionsPage();
                if (createdSuccessfully) {
                    System.out.println("Created a new account : " + ar.organisationName);
                }

                String orgName = ar.organisationName;

                boolean contains = false;
                boolean isCorrectTask = false;
                int count2 = 0;
                nameSelected = orgName;
                do {
                    mainNavigationBar = new MainNavigationBar(driver);
                    tasksPage = mainNavigationBar.clickTasks();

                    //Click on link number X
                    boolean isLinkVisible = tasksPage.isLinkVisible(orgName);
                    //WaitUtils.nativeWaitInSeconds(3);
                    if (isLinkVisible) {
                        taskSection = tasksPage.clickOnLinkWithText(orgName);
                        isCorrectTask = taskSection.isCorrectTask(orgName);
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

                //assertThat("Task not found for organisation : " + orgName, contains, is(equalTo(true)));

                listOfManufactuersCreatedWithTesterInitials.add(orgName);
            } catch (Exception e) {
                e.printStackTrace();
            }

            WaitUtils.nativeWaitInSeconds(2);
            System.out.println(ar.organisationName);
            //log.info(ar.organisationName);
            loginPage.logoutIfLoggedIn();
            WaitUtils.nativeWaitInSeconds(2);
        //}
    }

    private void createDevicesFor(User u, String manufacturerName) {

        loginAndViewManufacturer();

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
            addDevices = addDevices.confirmPayment();
            manufacturerList = addDevices.backToService();

            //@todo Now login as business user and approve the task
            WaitUtils.nativeWaitInSeconds(5);
            loginPage = loginPage.logoutIfLoggedInOthers();
            mainNavigationBar = loginPage.loginAs(businessUser.getUserName(), businessUser.getPassword());
            //approveTheGeneratedTask(nameSelected);
            //approveTheGeneratedTaskSimple(nameSelected, registered);
            String link = "Update";
            if (registered != null && registered.toLowerCase().equals("not registered")) {
                link = link.replace("Update", "New");
            }
            //Verify new taskSection generated and its the correct one
            boolean contains = false;
            boolean isCorrectTask = false;
            int count2 = 0;
            String orgName = nameSelected;
            do {
                mainNavigationBar = new MainNavigationBar(driver);
                tasksPage = mainNavigationBar.clickTasks();

                //Click on link number X
                boolean isLinkVisible = tasksPage.isLinkVisible(orgName);
                if (isLinkVisible) {
                    taskSection = tasksPage.clickOnLinkWithText(orgName);
                    isCorrectTask = taskSection.isCorrectTask(orgName, "Others");
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

            //assertThat("Task not found for organisation : " + orgName, contains, is(equalTo(true)));

            //Update status
            registered = "registered";

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
