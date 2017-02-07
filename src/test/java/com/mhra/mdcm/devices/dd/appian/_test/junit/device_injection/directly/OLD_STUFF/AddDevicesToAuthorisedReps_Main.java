package com.mhra.mdcm.devices.dd.appian._test.junit.device_injection.directly.OLD_STUFF;

import com.mhra.mdcm.devices.dd.appian._test.junit.common.Common;
import com.mhra.mdcm.devices.dd.appian.domains.junit.User;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.AccountManufacturerRequest;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.DeviceData;
import com.mhra.mdcm.devices.dd.appian.pageobjects.LoginPage;
import com.mhra.mdcm.devices.dd.appian.pageobjects.MainNavigationBar;
import com.mhra.mdcm.devices.dd.appian.pageobjects.external.sections.AddDevices;
import com.mhra.mdcm.devices.dd.appian.pageobjects.external.sections.CreateManufacturerTestsData;
import com.mhra.mdcm.devices.dd.appian.utils.datadriven.ExcelDataSheet;
import com.mhra.mdcm.devices.dd.appian.utils.driver.BrowserConfig;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.FileUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.TestHarnessUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Created by TPD_Auto on 01/11/2016.
 */
@RunWith(Parameterized.class)
public class AddDevicesToAuthorisedReps_Main extends Common {

    public static String registered = "registered";
    public static String nameSelected = null;
    private static String initials;

    public static final String AUTHORISED_REP_SMOKE_TEST = "AuthorisedRepST";

    public static List<DeviceData> listOfDeviceData = new ArrayList<>();
    private static List<User> listOfAuthorisedRepUsers = new ArrayList<>();

    public static WebDriver driver;
    public static String baseUrl;
    private static List<User> listOfBusinessUsers;
    private String username;
    private String password;
    private User manufacturerUser;


    public AddDevicesToAuthorisedReps_Main(){

    }

    public AddDevicesToAuthorisedReps_Main(User user) {
        this.username = user.getUserName();
        this.password = user.getPassword();
    }

    public static void main(String [] args){
        List<String> listOfManufacturerNames = getListOfManufacturerNames();
        List<User> listOfUsers = getListOfUsersFromExcel();
        System.setProperty("current.browser", "gc");
        setUpDriver();

        boolean loginBasedOnInitials = true;   //If we used BusinessCreateManufacturersWithTestersInitials
        if(loginBasedOnInitials) {
            //Create by logging into individual Account for the INITIALS
            new AddDevicesToAuthorisedReps_Main().createByLoggingIntoAccountWithInitials(listOfManufacturerNames, listOfUsers);
        }else {
            //OR - OR

            //Create for manufacturer from Excel sheet
            //createByLoggingIntoAccountWithExcelUser(listOfManufacturerNames, listOfUsers);
        }

    }

    private static List<User> getListOfUsersFromExcel() {
        ExcelDataSheet excelUtils = new ExcelDataSheet();//
        List<User> listOfUsers = excelUtils.getListOfUsers("configs/data/excel/users.xlsx", "InjectSpecificUser");
        listOfUsers = excelUtils.filterUsersBy(listOfUsers, "authorised");
        listOfAuthorisedRepUsers = listOfUsers;
        return listOfUsers;
    }

    private static List<User> getListOfBusinessUsersFromExcel() {
        ExcelDataSheet excelUtils = new ExcelDataSheet();//
        List<User> listOfUsers = excelUtils.getListOfUsers("configs/data/excel/users.xlsx", "Sheet1");
        listOfBusinessUsers = excelUtils.filterUsersBy(listOfUsers, "business");
        return listOfBusinessUsers;
    }

    public static void setUpDriver() {
        if (driver == null) {
            listOfDeviceData = excelUtils.getListOfDeviceData("configs/data/excel/DevicesData.xlsx", "TestDataWellFormed_Simple");
            driver = new BrowserConfig().getDriver();
            driver.manage().window().maximize();
            baseUrl = FileUtils.getTestUrl();
            log.warn("\n\nINSERT DEVICES AS MANUFACTURER USER VIA MAIN METHOD");
        }
    }



    /**
     * UPDATE THIS MANUALLY FOR NOW
     *
     * This is all the users created using : BusinessCreateManufacturersWithTestersInitials
     * @return
     */
    private static List<String> getListOfManufacturerNames() {
        List<String> listOfManufactuersCreatedWithTesterInitials = new ArrayList<>();
//        listOfManufactuersCreatedWithTesterInitials.add("AuthorisedRepST11157024_LP");
//        listOfManufactuersCreatedWithTesterInitials.add("AuthorisedRepST11157024_LP");
//        listOfManufactuersCreatedWithTesterInitials.add("AuthorisedRepST11157024_LP");
//        listOfManufactuersCreatedWithTesterInitials.add("AuthorisedRepST11157024_LP");
//        listOfManufactuersCreatedWithTesterInitials.add("AuthorisedRepST11157024_LP");
//        listOfManufactuersCreatedWithTesterInitials.add("AuthorisedRepST11157024_LP");

        listOfManufactuersCreatedWithTesterInitials.add("AuthorisedRepST13162037_NU");
        return listOfManufactuersCreatedWithTesterInitials;
    }

    private static void createByLoggingIntoAccountWithExcelUser(List<String> listOfManufacturerNames, List<User> listOfUsers) {
        for(User selected: listOfUsers){
            //Create Devices For Following user
            for(String manufacturerName: listOfManufacturerNames){
                try {
                    nameSelected = manufacturerName;
                    AddDevicesToAuthorisedReps_Main tc = new AddDevicesToAuthorisedReps_Main(selected);
                    tc.setLoginDetails(selected);
                    tc.createDevicesFor(selected, manufacturerName);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void createByLoggingIntoAccountWithInitials(List<String> listOfManufacturerNames, List<User> listOfManufacturerUsers) {
        for(String manufacturerName: listOfManufacturerNames){
            try {
                nameSelected = manufacturerName;
                initials = manufacturerName.substring(manufacturerName.indexOf("_")+1);
                User selected = setCorrectLoginDetails(nameSelected, listOfManufacturerUsers);
                setLoginDetails(selected);

                //Assuming all previous data removed
                loginPage = new LoginPage(driver);
                loginPage = loginPage.loadPage(baseUrl);
                mainNavigationBar = loginPage.loginAsManufacturer(username, password);
                externalHomePage = mainNavigationBar.clickHome();

                //Click on a random manufacturer
                manufacturerList = externalHomePage.gotoListOfManufacturerPage();
                createRepresentedManufacturereAccountDetails(listOfManufacturerUsers);

                AddDevicesToAuthorisedReps_Main tc = new AddDevicesToAuthorisedReps_Main(selected);
                tc.setLoginDetails(selected);
                tc.createDevicesFor(selected, manufacturerName);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void setLoginDetails(User selected) {
        username = selected.getUserName();
        password = selected.getPassword();
    }

    private static User setCorrectLoginDetails(String nameSelected, List<User> listOfUsers) {
        User selectCorrectUser = null;
        for(User u: listOfUsers){
            String initials = "_"+u.getInitials();
            if(nameSelected.contains(initials)){
                selectCorrectUser = u;
                break;
            }
        }

        return selectCorrectUser;
    }

    private void loginAndViewManufacturer() {

        //Login to app and add devices to the manufacturer
        loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        mainNavigationBar = loginPage.loginAsManufacturer(username, password);
        externalHomePage = mainNavigationBar.clickHome();

        //Click on a random manufacturer
        manufacturerList = externalHomePage.gotoListOfManufacturerPage();

        //You will need to naviage to different pages to select the manufactuerer
        String name = null; //manufacturerList.getARandomManufacturerNameWithStatus(registered);
        if (nameSelected == null) {
            int nop = manufacturerList.getNumberOfPages();
            int page = 0;
            do {
                name = manufacturerList.getARandomManufacturerNameWithStatus(registered);
                if (name == null) {
                    manufacturerList = manufacturerList.clickNext();
                } else {
                    log.info("Manufacturer selected : " + name + ", is " + registered);
                    manufacturerDetails = manufacturerList.viewAManufacturer(name);
                    nameSelected = name;
                    break;
                }
                page++;
            } while (page < nop);
        } else {
            name = nameSelected;
            log.info("Manufacturer selected : " + name + ", is " + registered);
            manufacturerDetails = manufacturerList.viewAManufacturer(name);
        }

        //Add devices: This needs to change to add all the devices
        try {
            if (registered != null && registered.toLowerCase().equals("registered"))
                addDevices = manufacturerDetails.clickAddDeviceBtn();
            else
                addDevices = new AddDevices(driver);
        }catch (Exception e){
            addDevices = new AddDevices(driver);
        }
    }

    private void createRepresentedManufacturereAccountDetails(List<User> listOfUsers) {
        AccountManufacturerRequest ar = new AccountManufacturerRequest();
        try {
            //Now create the test data using harness page
            ar.updateName(AUTHORISED_REP_SMOKE_TEST);
            ar.updateNameEnding("_" + initials);
            ar.setUserDetails(username);
            ar.country = "Nepal";

            ar.firstName = TestHarnessUtils.getName(initials, true, listOfAuthorisedRepUsers);
            ar.lastName = TestHarnessUtils.getName(initials, false, listOfAuthorisedRepUsers);

            //Create new manufacturer data
            createNewManufacturer = new CreateManufacturerTestsData(driver);
            addDevices = createNewManufacturer.createTestOrganisation(ar);
            if (createNewManufacturer.isErrorMessageDisplayed()) {
                externalHomePage = mainNavigationBar.clickExternalHOME();
                manufacturerList = externalHomePage.gotoListOfManufacturerPage();
                createNewManufacturer = manufacturerList.registerNewManufacturer();
                addDevices = createNewManufacturer.createTestOrganisation(ar);
            }

            //Provide indication of devices made
            manufacturerUser = setCorrectLoginDetails(nameSelected, listOfUsers);
            log.info("Provide Indication Of Devices For : " + nameSelected);
            provideIndicationOfDevicesMade(manufacturerUser);

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
        loginPage.logoutIfLoggedInOthers();
    }

    private void provideIndicationOfDevicesMade(User selected) {
        //User selected = setCorrectLoginDetails(nameSelected, listOfUsers);
        setLoginDetails(selected);
        //loginAndGoToSetDeviceIndication();
        indicateDevices();
        User businessUser = setCorrectLoginDetails("NU", listOfBusinessUsers);
        acceptNewServiceRequest(businessUser);

        WaitUtils.nativeWaitInSeconds(2);
        loginPage = loginPage.logoutIfLoggedIn();
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
            if (!contains){
                WaitUtils.nativeWaitInSeconds(2);
                count++;
            }
        } while (!contains && count <= 5);

        if (contains) {
            //accept the taskSection and approve or reject it
            taskSection = taskSection.acceptTask();
            if(taskType!=null) {
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
        WaitUtils.nativeWaitInSeconds(7);
        for (int x = 0; x < 9; x++) {
            externalHomePage = externalHomePage.provideIndicationOfDevicesMade(x);
        }

        //custom made
        externalHomePage.selectCustomMade(true);

        //Submit devices made : They changed the work flow on 03/02/2017
        //externalHomePage = externalHomePage.submitIndicationOfDevicesMade(true);
        //externalHomePage = externalHomePage.submitIndicationOfDevicesMade(false);
        createNewManufacturer = externalHomePage.submitIndicationOfDevicesMade(true);

        WaitUtils.nativeWaitInSeconds(5);
        loginPage = loginPage.logoutIfLoggedInOthers();
        WaitUtils.nativeWaitInSeconds(2);

        System.out.println("DONE");
    }

    private void createDevicesFor(User u, String manufacturerName) {

        loginAndViewManufacturer();

        String[] deviceTypes = new String[]{
                "general medical"//, "vitro diagnostic", "active implantable", "procedure pack"
        };

        List<DeviceData> listOfDevicesWhichHadProblems = new ArrayList<>();

        for (String specificDeviceTypes : deviceTypes) {

            //Assumes we are in add device page
            List<DeviceData> listOfDevicesOfSpecificType = getListOfDevicesOfSpecificType(listOfDeviceData, specificDeviceTypes);
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
                        //if (count > 2) {
                        //    break;
                        //}

                        //Try adding another device
                        if (isVisible && count < listOfDevicesOfSpecificType.size() - 1)
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
            boolean isVisible = addDevices.isOptionToAddAnotherDeviceVisible();
            Assert.assertThat("Expected to see option to : Add another device", isVisible, Matchers.is(true));

            //Confirm
            addDevices = addDevices.proceedToPayment();
            addDevices = addDevices.submitRegistration();
            externalHomePage = addDevices.finish();

            //@todo Now login as business user and approve the task
            WaitUtils.nativeWaitInSeconds(5);
            loginPage = loginPage.logoutIfLoggedInOthers();
            mainNavigationBar = loginPage.loginAs("Noor.Uddin.Business", "MHRA1234");
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

            assertThat("Task not found for organisation : " + orgName, contains, is(equalTo(true)));

            //Update status
            registered = "registered";

            //Logback in now
            WaitUtils.nativeWaitInSeconds(5);
            loginPage.logoutIfLoggedIn();
            loginAndViewManufacturer();
        }
    }


    private List<DeviceData> getListOfDevicesOfSpecificType(List<DeviceData> listOfDeviceData, String specificType) {
        List<DeviceData> listOfDevicesOfType = new ArrayList<>();
        for (DeviceData dd : listOfDeviceData) {
            if (dd.deviceType.contains(specificType)) {
                listOfDevicesOfType.add(dd);
            }
        }
        return listOfDevicesOfType;
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
        return "SmokeTestsManufacturers";
    }
}
