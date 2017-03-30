package com.mhra.mdcm.devices.dd.appian._test.junit.device_injection.directly;

import com.mhra.mdcm.devices.dd.appian._test.junit.common.Common;
import com.mhra.mdcm.devices.dd.appian.domains.junit.User;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.AccountManufacturerRequest;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.AccountRequest;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.DeviceData;
import com.mhra.mdcm.devices.dd.appian.pageobjects.LoginPage;
import com.mhra.mdcm.devices.dd.appian.pageobjects.MainNavigationBar;
import com.mhra.mdcm.devices.dd.appian.pageobjects.external._CreateManufacturerTestsData;
import com.mhra.mdcm.devices.dd.appian.utils.driver.BrowserConfig;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.FileUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.TestHarnessUtils;
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
public class _AllInOne_AddDevicesToAuthorisedReps_Main extends Common {

    private static User businessUser;
    public String[] initialsArray = new String[]{
            "NU"//"AT", "NU", "HB", "YC", "PG", "AN", "LP"
    };
    public static final String AUTHORISED_REP_SMOKE_TEST = "AuthorisedRepST";

    public static String registered = "registered";
    public static String nameSelected = null;
    public static String initials = null;
    public static String mainAccount = null;

    private static List<String> listOfAccountsCreatedWithTesterInitials = new ArrayList<>();
    private static List<User> listOfAuthorisedRepUsers = new ArrayList<>();
    private static List<User> listOfBusinessUsers = new ArrayList<>();

    public static WebDriver driver;
    public static String baseUrl;
    private String username;
    private String password;
    private User manufacturerUser;


    public _AllInOne_AddDevicesToAuthorisedReps_Main(User user) {
        this.username = user.getUserName();
        this.password = user.getPassword();
    }

    public static void main(String[] args) {

        listOfAuthorisedRepUsers = DirectDeviceDataUtils.getListOfUsersFromExcel("authorised");
        listOfBusinessUsers = DirectDeviceDataUtils.getListOfBusinessUsersFromExcel("business");
        setUpDriver();

        /**
         * Always use one of the Business Accounts to create the test manufacturers
         * This will create authorisedReps with users initials e.g _NU, _HB
         */
        log.info("First CREATE New Accounts To Add Manufactures/Devices To : ");
        businessUser = DirectDeviceDataUtils.getCorrectLoginDetails("_NU", listOfBusinessUsers);
        _AllInOne_AddDevicesToAuthorisedReps_Main tgs = new _AllInOne_AddDevicesToAuthorisedReps_Main(businessUser);
        tgs.createNewAccountForAuthorisedRepWithBusinessTestHarness(listOfAuthorisedRepUsers);

        /**
         * All data cleared:Provide indication of devices made
         * Create by logging into individual Account for the INITIALS
         */
        log.info("Now create a new organisation and add devices to : ");
        tgs.createByLoggingIntoAccountWithInitials(listOfAuthorisedRepUsers);

        //closeDriver();
    }

    public static void setUpDriver() {
        System.setProperty("current.browser", "gc");
        if (driver == null) {
            driver = new BrowserConfig().getDriver();
            driver.manage().window().maximize();
            baseUrl = FileUtils.getTestUrl();
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

    /**
     * For each accounts created with _UsersInitials create an organisation and add devices
     *
     * Add devices to each of the manufacturers successfully created
     *
     * @param listOfAuthorisedRepUsers
     */
    private void createByLoggingIntoAccountWithInitials(List<User> listOfAuthorisedRepUsers) {

        /**
         * LIST OF MANUFACTURERS CREATED FOR EACH OF THE INITIALS IN THE ARRAY
         */
        List<String> listOfAccountsCreatedWithBusinessTestHarness = getListOfAccountsCreatedByBusiness();

        for (String manufacturerName : listOfAccountsCreatedWithBusinessTestHarness) {
            try {

                //Set manufacturer account login details
                nameSelected = manufacturerName;
                initials = nameSelected.substring(nameSelected.indexOf("_"));
                manufacturerUser = DirectDeviceDataUtils.getCorrectLoginDetailsManufacturer(initials, listOfAuthorisedRepUsers);
                setLoginDetails(manufacturerUser);

                //"Provide Indication Of Devices For : " + manufacturerName
                try {
                    //flow changed on 03/02/2017 : Now we indicate a device than create a new manufacturer
                    logBackInAsManufacturer(manufacturerUser);
                    indicateDevices(false);


                    //Accept the device indication for account : 03/03/3017 : REMOVED
//                    String accountName = getManufacturerWithInitials(initials, true);
                    //boolean approved = acceptNewServiceRequest(businessUser, accountName);
//                    if (!approved) {
//                        accountName = getManufacturerWithInitials(initials, false);
//                        approved = acceptNewServiceRequest(businessUser, accountName);
//                    }

                    //
                    registerANewManufacturer();
                    createAuthorisedRepsWithManufacturerTestHarness2(manufacturerUser);
                    createDevicesFor(manufacturerUser, manufacturerName, false);

                    WaitUtils.nativeWaitInSeconds(2);
                    //loginPage = loginPage.logoutIfLoggedIn();
                }catch (Exception e){
                    e.printStackTrace();
                    //This is what it was before push on 03/02/2017
                    createAuthorisedRepsWithManufacturerTestHarness(manufacturerUser);
                    provideIndicationOfDevicesMade(businessUser);
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

    private List<String> getListOfAccountsCreatedByBusiness() {
        List<String> listOfAccountsCreatedByBusiness = new ArrayList<>();
        for (String name : getListOfAccountNames()) {
            listOfAccountsCreatedByBusiness.add(name);
        }
        return listOfAccountsCreatedByBusiness;
    }


    /**
     * This is a long process
     * - Indicate devices
     * - Logout and accept and approve the task
     * - Log back in
     *
     * @param businessUser
     */
    private void provideIndicationOfDevicesMade(User businessUser) {
        indicateDevices(false);
        String accountName = getManufacturerWithInitials(initials, true);
        boolean approved = acceptNewServiceRequest(businessUser, accountName);
        if (!approved) {
            accountName = getManufacturerWithInitials(initials, false);
            approved = acceptNewServiceRequest(businessUser, accountName);
        }

        WaitUtils.nativeWaitInSeconds(2);
        loginPage = loginPage.logoutIfLoggedIn();

    }

    private String getManufacturerWithInitials(String initials, boolean isMainAccount) {
        List<String> listOfManufacturerNames = getListOfAccountNames();
        String names = "";
        for (String name : listOfManufacturerNames) {
            if (name.contains(initials)) {
                names = names + name + ",";
            }
        }

        String[] data = names.split(",");
        if (isMainAccount) {
            return data[0];
        } else {
            return data[1];
        }
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
        externalHomePage.selectCustomMade(true);

        //Submit devices made
        //externalHomePage = externalHomePage.submitIndicationOfDevicesMade(true);
        //externalHomePage = externalHomePage.submitIndicationOfDevicesMade(false);
        //Submit devices made : They changed the work flow on 03/02/2017
        createNewManufacturer = externalHomePage.submitIndicationOfDevicesMade(clickNextBtn);
        WaitUtils.nativeWaitInSeconds(5);


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

    /**
     * UPDATE THIS MANUALLY FOR NOW
     * <p>
     * This is all the users created using : BusinessCreateAccountsWithTestersInitials
     *
     * @return
     */
    private static List<String> getListOfAccountNames() {
        return listOfAccountsCreatedWithTesterInitials;
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
        log.info("Manufacturer selected : " + name);
        registered = manufacturerList.getRegistrationStatus(name);
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
    }


    private void createNewAccountForAuthorisedRepWithBusinessTestHarness(List<User> listOfAuthorisedRepUsers) {

        for (String initials : initialsArray) {

            AccountRequest ar = new AccountRequest();
            try {

                //Now create the test data using harness page
                ar.isManufacturer = false;
                ar.updateName(AUTHORISED_REP_SMOKE_TEST);
                ar.updateNameEnding("_" + initials);
                ar.setUserDetails(username);

                manufacturerUser = DirectDeviceDataUtils.getCorrectLoginDetailsManufacturer(initials, listOfAuthorisedRepUsers);

                //ar.title = "Miss";
                ar.firstName = TestHarnessUtils.getName(initials, manufacturerUser, true);
                ar.lastName = TestHarnessUtils.getName(initials, manufacturerUser, false);


                //Login and try to create it
                loginPage = new LoginPage(driver);
                loginPage = loginPage.loadPage(baseUrl);
                MainNavigationBar mainNavigationBar = loginPage.loginAs(username, password);

                //go to accounts page > test harness page
                actionsPage = mainNavigationBar.clickActions();
                createTestsData = actionsPage.gotoTestsHarnessPage();

                actionsPage = createTestsData.createTestOrganisation(ar);
                boolean isInCorrectPage = actionsPage.isInActionsPage();
                if (!isInCorrectPage) {
                    actionsPage = mainNavigationBar.clickActions();
                    createTestsData = actionsPage.gotoTestsHarnessPage();
                    actionsPage = createTestsData.createTestOrganisation(ar);
                }

                boolean createdSuccessfully = actionsPage.isInActionsPage();
                if (createdSuccessfully) {
                    log.info("Created a new account with business : " + ar.organisationName);
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
                    tasksPage = taskSection.approveTaskNewAccount();
                    log.info("Approved newly created organisation with business : " + ar.organisationName);
                }

                listOfAccountsCreatedWithTesterInitials.add(orgName);
            } catch (Exception e) {
                e.printStackTrace();
            }

            log.info("Created The Following Accounts : " + listOfAccountsCreatedWithTesterInitials + "\n");

            WaitUtils.nativeWaitInSeconds(2);
            //log.info(ar.organisationName);
            loginPage.logoutIfLoggedIn();
            WaitUtils.nativeWaitInSeconds(2);
        }
    }

    private void createDevicesFor(User u, String manufacturerName, boolean loginAgain) {
        log.info("Try And Add Devices For : " + nameSelected);

        List<DeviceData> listOfDeviceData = DirectDeviceDataUtils.getListOfDeviceData();

        if(loginAgain)
        loginAndViewManufacturer();

        String[] deviceTypes = new String[]{
                "all devices", //"general medical", "vitro diagnostic", "active implantable", "procedure pack"
        };

        List<DeviceData> listOfDevicesWhichHadProblems = new ArrayList<>();

        for (String specificDeviceTypes : deviceTypes) {

            //Assumes we are in add device page
            List<DeviceData> listOfDevicesOfSpecificType = DirectDeviceDataUtils.getListOfDevicesOfSpecificType(listOfDeviceData, specificDeviceTypes);
            listOfDevicesOfSpecificType = DirectDeviceDataUtils.getValidatedDataOnly(true, listOfDevicesOfSpecificType);
            int count = 0;

            //Lets try to add multiple devices, it will take a long time
            for (DeviceData dd : listOfDevicesOfSpecificType) {

                if (dd.validatedData.toLowerCase().equals("y")) {
                    try {
                        //Only for DEBUGGING
                        log.info("\n----------------------------------------------------------");
                        log.info("Product number : " + (count + 1));
                        //log.info("Device Type : " + dd);
                        DirectDeviceDataUtils.printDeviceData(dd);
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

            DirectDeviceDataUtils.printFailingData(listOfDevicesWhichHadProblems, specificDeviceTypes);

            //Verify option to add another device is there
            try {
                boolean isVisible = addDevices.isOptionToAddAnotherDeviceVisible();
                if (!isVisible) {
                    DeviceData dd = DirectDeviceDataUtils.getDeviceDataCalled(listOfDevicesWhichHadProblems, "Abacus");
                    if(dd == null){
                        //System keeps bloody changing the GMDN
                        dd = DirectDeviceDataUtils.getListOfDevicesOfSpecificType(listOfDeviceData, "general medical").get(0);
                    }
                    dd.device = "con";
                    addDevices = addDevices.addFollowingDevice(dd);
                    isVisible = addDevices.isOptionToAddAnotherDeviceVisible();
                }
            }catch (Exception e){

            }

            //Confirm payment and submit registration
            addDevices = addDevices.proceedToPayment();
            addDevices = addDevices.submitRegistration();
            externalHomePage = addDevices.finish();

            //@todo Now login as business user and approve the task
            WaitUtils.nativeWaitInSeconds(4);
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

            log.info("Create Devices For : " + nameSelected);

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


    private void createAuthorisedRepsWithManufacturerTestHarness(User user) throws Exception {

        //Now create the test data using harness page
        AccountManufacturerRequest ar = new AccountManufacturerRequest();
        ar.isManufacturer = false;
        ar.updateName(AUTHORISED_REP_SMOKE_TEST);
        ar.updateNameEnding("_" + initials);
        ar.setUserDetails(username);
        ar.country = "Brazil";

        manufacturerUser = user;
        ar.firstName = TestHarnessUtils.getName(initials, manufacturerUser, true);
        ar.lastName = TestHarnessUtils.getName(initials, manufacturerUser, false);

        //Assuming all previous data removed
        loginPage = new LoginPage(driver);
        loginPage = loginPage.loadPage(baseUrl);
        mainNavigationBar = loginPage.loginAsManufacturer(username, password);
        externalHomePage = mainNavigationBar.clickHome();

        //Click on a random manufacturer
        manufacturerList = externalHomePage.gotoListOfManufacturerPage();

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

        //Add devices : Change introduced in 20/03/2017
        //addDevices.addFollowingDevice(new DeviceData());

        //Provide indication of devices made to the newly created authoirisedRep
        manufacturerUser = user;
        //addToListOfManufacturersCreatedWithInitials(initials, listOfAccountsCreatedWithTesterInitials, ar.organisationName);
        listOfAccountsCreatedWithTesterInitials.add(ar.organisationName);
        nameSelected = ar.organisationName;

    }


    private void createAuthorisedRepsWithManufacturerTestHarness2(User user) throws Exception {

        //Now create the test data using harness page
        AccountManufacturerRequest ar = new AccountManufacturerRequest();
        ar.isManufacturer = false;
        ar.updateName(AUTHORISED_REP_SMOKE_TEST);
        ar.updateNameEnding("_" + initials);
        ar.setUserDetails(username);
        ar.country = "Brazil";

        manufacturerUser = user;
        ar.firstName = TestHarnessUtils.getName(initials, manufacturerUser, true);
        ar.lastName = TestHarnessUtils.getName(initials, manufacturerUser, false);

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
        //addDevices.addFollowingDevice(new DeviceData());
        //externalHomePage = createNewManufacturer.submitForApproval();

        log.info("Created a new org to add devices to : " + ar.organisationName);

        //Provide indication of devices made to the newly created authoirisedRep
        manufacturerUser = user;
        //addToListOfManufacturersCreatedWithInitials(initials, listOfAccountsCreatedWithTesterInitials, ar.organisationName);
        listOfAccountsCreatedWithTesterInitials.add(ar.organisationName);
        nameSelected = ar.organisationName;

    }


    @Override
    public String toString() {
        return "CREATE DEVICES FOR AuthorisedReps";
    }
}
