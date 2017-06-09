package com.mhra.mdcm.devices.dd.appian._junit_smokes.device_injection.directly;

import com.mhra.mdcm.devices.dd.appian._junit_smokes.common.Common;
import com.mhra.mdcm.devices.dd.appian.domains.junit.User;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.DeviceData;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.ManufacturerOrganisationRequest;
import com.mhra.mdcm.devices.dd.appian.pageobjects.LoginPage;
import com.mhra.mdcm.devices.dd.appian.pageobjects.MainNavigationBar;
import com.mhra.mdcm.devices.dd.appian.pageobjects.external._CreateManufacturerTestsData;
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
public class _AllInOne_AddDevicesToEXISTINGAuthorisedReps_Main extends Common {

    private ManufacturerOrganisationRequest ar = new ManufacturerOrganisationRequest();
    public static final String AUTHORISED_REP_SMOKE_TEST = "AuthorisedRepST";

    public static WebDriver driver;
    public static String baseUrl;
    private String username;
    private String password;
    private String initials;
    private User manufacturerUser;


    public _AllInOne_AddDevicesToEXISTINGAuthorisedReps_Main(User user) {
        this.username = user.getUserName();
        this.password = user.getPassword();
    }

    public static void main(String[] args) {

        List<User> listOfAuthorisedRepUsers = ExcelDirectDeviceDataUtils.getListOfUsersFromExcel("authorised");
        List<User> listOfBusinessUsers = ExcelDirectDeviceDataUtils.getListOfBusinessUsersFromExcel("business");
        List<DeviceData> listOfDeviceData = ExcelDirectDeviceDataUtils.getListOfDeviceData();
        setUpDriver();

        for(User u: listOfBusinessUsers) {
            try {
                /**
                 * Always use one of the Business Accounts to create the test manufacturers
                 * This will create authorisedReps with users initials e.g _NU, _HB
                 */
                log.info("First CREATE New Accounts To Add Manufactures/Devices To : ");
                String initials = u.getInitials();
                User businessUser = ExcelDirectDeviceDataUtils.getCorrectLoginDetails("_" + initials, listOfBusinessUsers);
                _AllInOne_AddDevicesToEXISTINGAuthorisedReps_Main tgs = new _AllInOne_AddDevicesToEXISTINGAuthorisedReps_Main(businessUser);

                //We only want to do it if the INITIALS in our initialsArray list
                boolean isInitialFound = tgs.isInitialsInTheList(businessUser.getInitials());
                if (isInitialFound) {

                    /**
                     * All data cleared:Provide indication of devices made
                     * Create by logging into individual Account for the INITIALS
                     */
                    log.info("Now create a new organisation and add devices to : ");
                    User user = TestHarnessUtils.getUserWithInitials(initials, listOfAuthorisedRepUsers);
                    tgs.createNewAuthorisedRepsWithDevices(user, businessUser, listOfDeviceData);
                } else {
                    System.out.println("Not creating any data for : " + businessUser + "\nCheck initialsArray contains the initials : " + businessUser.getInitials());
                }
            }catch (Exception e){
                System.out.println("Try and setup data for next user ");
            }
        }

        //closeDriver();
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
        for(String in: initialsArray){
            if(in.equals(initials)){
                found = true;
                break;
            }
        }
        return found;
    }

    /**
     * For each accounts created with _UsersInitials create an organisation and add devices
     *
     * Add devices to each of the manufacturers successfully created
     *  @param authorisedRepUser
     * @param businessUser
     * @param listOfDeviceData
     */
    private void createNewAuthorisedRepsWithDevices(User authorisedRepUser, User businessUser, List<DeviceData> listOfDeviceData) {


        try {

            //Set manufacturer account login details
            String authorisedUserName = authorisedRepUser.getUserName();
            String initials = authorisedRepUser.getInitials();
            setLoginDetails(authorisedRepUser);

            //"Provide Indication Of Devices For : " + manufacturerName
            try {
                //flow changed on 03/02/2017 : Now we indicate a device than create a new manufacturer
                logBackInAsManufacturer(authorisedRepUser);
                indicateDevices(false);

                //
                registerANewManufacturer();
                createAuthorisedRepsWithManufacturerTestHarness2(authorisedRepUser);
                createDevicesFor(authorisedRepUser, businessUser, listOfDeviceData, false);

                WaitUtils.nativeWaitInSeconds(2);
                //loginPage = loginPage.logoutIfLoggedIn();
            }catch (Exception e){
                e.printStackTrace();
            }

            //Log back in as the newly created authorisedRep and try adding devices
            //createDevicesFor(manufacturerUser, manufacturerName, true);

        } catch (Exception e) {
            e.printStackTrace();

            //System is taking long time to load pages
            WaitUtils.nativeWaitInSeconds(2);
            loginPage = loginPage.logoutIfLoggedInOthers();
            WaitUtils.nativeWaitInSeconds(2);
            loginPage = loginPage.logoutIfLoggedIn();
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
        mainNavigationBar = loginPage.loginAsManufacturer(manufacturerUser.getUserName(), manufacturerUser.getPassword());
        externalHomePage = mainNavigationBar.clickHome();

        //Click on a random manufacturer
        manufacturerList = externalHomePage.gotoListOfManufacturerPage();
    }

//    private boolean acceptNewServiceRequest(User businessUser, String name) {
//        log.info("Find and accept the tasks for : " + name);
//
//        WaitUtils.nativeWaitInSeconds(5);
//        loginPage = loginPage.logoutIfLoggedInOthers();
//        WaitUtils.nativeWaitInSeconds(2);
//
//        MainNavigationBar mainNavigationBar = loginPage.loginAs(businessUser.getUserName(), businessUser.getPassword());
//
//        //Verify new taskSection generated and its the correct one
//        boolean contains = false;
//        boolean isCorrectTask = false;
//        String taskType = "New Service";
//        String orgName = name;
//        int count = 0;
//        do {
//            //Refresh each time, it may take a while for the new task to arrive
//            tasksPage = mainNavigationBar.clickTasks();
//
//            //Click on link number X
//            try {
//                taskSection = tasksPage.clickOnLinkWithText(orgName);
//                contains = true;
//            } catch (Exception e) {
//                contains = false;
//            }
//            if (!contains) {
//                WaitUtils.nativeWaitInSeconds(2);
//                count++;
//            }
//        } while (!contains && count <= 5);
//
//        if (contains) {
//            //accept the taskSection and approve or reject it
//            taskSection = taskSection.acceptTask();
//            if (taskType != null) {
//                if (taskType.contains("New Service") || taskType.contains("New Account")) {
//                    tasksPage = taskSection.approveTask();
//                } else if (taskType.contains("New Manufacturer")) {
//                    //tasksPage = taskSection.acceptRegistrationTask();
//                } else if (taskType.contains("Update Manufacturer Registration Request")) {
//                    tasksPage = taskSection.approveTask();
//                }
//            }
//
//        }
//
//        return contains;
//    }

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
        }catch (Exception e){}

        //Submit devices types made
        createNewManufacturer = externalHomePage.submitIndicationOfDevicesMade(clickNextBtn);
        WaitUtils.nativeWaitInSeconds(5);


        //System.out.println("DONE");
    }

//    private void loginAndGoToSetDeviceIndication() {
//
//        //Login to app and add devices to the manufacturer
//        loginPage = new LoginPage(driver);
//        loginPage = loginPage.loadPage(baseUrl);
//        mainNavigationBar = loginPage.loginAsManufacturer(username, password);
//        externalHomePage = mainNavigationBar.clickHome();
//
//        //Click on a random manufacturer
//        manufacturerList = externalHomePage.gotoListOfManufacturerPage();
//    }


    private void setLoginDetails(User selected) {
        username = selected.getUserName();
        password = selected.getPassword();
        initials = selected.getInitials();
    }


    private String loginAndViewManufacturer(User authorisedRepUser) {

        //Login to app and add devices to the manufacturer
        loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        mainNavigationBar = loginPage.loginAsManufacturer(authorisedRepUser.getUserName(), authorisedRepUser.getPassword());
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
        String name = ar.organisationName;
        log.info("Manufacturer selected : " + name);
        String registered = manufacturerList.getRegistrationStatus(name);
        manufacturerDetails = manufacturerList.viewAManufacturer(name);

        //Add devices: This needs to change to add all the devices
        try {
            if (registered != null && registered.toLowerCase().equals("registered")){
                addDevices = manufacturerDetails.clickAddDeviceBtn();
            } else{
                addDevices = manufacturerDetails.clickDeclareDevicesBtn();
            }
        } catch (Exception e) {
            addDevices = manufacturerDetails.clickDeclareDevicesBtn();
        }

        return registered;
    }

    private void createDevicesFor(User authorisedRepUser, User businessUser, List<DeviceData> listOfDeviceData, boolean loginAgain) {
        log.info("Try And Add Devices For : " + ar.organisationName);

        String registered = "registered";
        if(loginAgain)
            registered = loginAndViewManufacturer(authorisedRepUser);

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
                    if(dd == null){
                        //System keeps bloody changing the GMDN
                        dd = ExcelDirectDeviceDataUtils.getListOfDevicesOfSpecificType(listOfDeviceData, "general medical").get(0);
                    }
                    dd.device = "con";
                    addDevices = addDevices.addFollowingDevice(dd);
                    isVisible = addDevices.isOptionToAddAnotherDeviceVisible();
                    if(!isVisible){
                        addDevices = addDevices.saveDevice();
                    }
                }
            }catch (Exception e){

            }

            //Confirm payment and submit registration
            addDevices = addDevices.proceedToReview();
            addDevices = addDevices.proceedToPayment();
            addDevices = addDevices.confirmPayment();
            manufacturerList = addDevices.backToService();

            //@todo Now login as business user and approve the task
            WaitUtils.nativeWaitInSeconds(4);
            loginPage = loginPage.logoutIfLoggedInOthers();
            mainNavigationBar = loginPage.loginAs(businessUser.getUserName(), businessUser.getPassword());

            //Find task and approve it
            String link = "Update";
            if (registered != null && registered.toLowerCase().equals("not registered")) {
                link = link.replace("Update", "New");
            }

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

            //Update status
            registered = "registered";

            //Logback in now
            WaitUtils.nativeWaitInSeconds(3);
            loginPage.logoutIfLoggedIn();
            //loginAndViewManufacturer();

            log.info("\nCREATED NEW AUTHORISED-REP WITH DEVICES : COMPLETED NOW");
        }
    }

    private void clickAddAnotherButton() {
        try{
            boolean isVisible = addDevices.isOptionToAddAnotherDeviceVisible();
            if(isVisible){
                addDevices = addDevices.addAnotherDevice();
            }
        }catch (Exception e){

        }
    }



    private void createAuthorisedRepsWithManufacturerTestHarness2(User authorisedRepUser) throws Exception {
        String initials = authorisedRepUser.getInitials();

        //Now create the test data using harness page
        ar.isManufacturer = false;
        ar.updateName(AUTHORISED_REP_SMOKE_TEST);
        ar.updateNameEnding("_" + initials);
        ar.setUserDetails(authorisedRepUser.getUserName());
        ar.country = "United Kingdom";

        ar.firstName = TestHarnessUtils.getName(initials, authorisedRepUser, true);
        ar.lastName = TestHarnessUtils.getName(initials, authorisedRepUser, false);

        //Create new manufacturer data
        createNewManufacturer = new _CreateManufacturerTestsData(driver);
        addDevices = createNewManufacturer.createTestOrganisation(ar, false);
        if (createNewManufacturer.isErrorMessageDisplayed()) {
            externalHomePage = mainNavigationBar.clickExternalHOME();
            manufacturerList = externalHomePage.gotoListOfManufacturerPage();
            createNewManufacturer = manufacturerList.registerNewManufacturer();
            addDevices = createNewManufacturer.createTestOrganisation(ar, false);
        }

        //Add devices : Change introduced in 20/03/2017
        //addDevices.addFollowingDevice(new DeviceData()) ;
        //externalHomePage = createNewManufacturer.submitForApproval();

        log.info("Created a new org to add devices to : " + ar.organisationName);

    }


    @Override
    public String toString() {
        return "CREATE DEVICES FOR AuthorisedReps";
    }
}
