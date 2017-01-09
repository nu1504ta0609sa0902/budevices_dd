package com.mhra.mdcm.devices.dd.appian._test.junit.device_injection.directly;

import com.mhra.mdcm.devices.dd.appian._test.junit.common.Common;
import com.mhra.mdcm.devices.dd.appian.domains.junit.User;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.DeviceData;
import com.mhra.mdcm.devices.dd.appian.pageobjects.LoginPage;
import com.mhra.mdcm.devices.dd.appian.pageobjects.MainNavigationBar;
import com.mhra.mdcm.devices.dd.appian.pageobjects.external.sections.AddDevices;
import com.mhra.mdcm.devices.dd.appian.utils.datadriven.ExcelDataSheet;
import com.mhra.mdcm.devices.dd.appian.utils.driver.BrowserConfig;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.FileUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.hamcrest.Matchers;
import org.junit.*;
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
public class AddDevicesToManufacturers_Main extends Common {

    public static String registered = "registered";
    public static String nameSelected = null;

    public static List<DeviceData> listOfDeviceData = new ArrayList<>();
    public static WebDriver driver;
    public static String baseUrl;
    private String username;
    private String password;

    public static void main(String [] args){
        List<String> listOfManufacturerNames = getListOfManufacturerNames();
        List<User> listOfUsers = getListOfUsersFromExcel();
        System.setProperty("current.browser", "gc");
        setUpDriver();

        //For each user : THIS CASE A MANUFACTURER
        for(User u: listOfUsers){
            //Create Devices For Following user
            for(String manufacturerName: listOfManufacturerNames){
                try {
                    nameSelected = manufacturerName;
                    AddDevicesToManufacturers_Main tc = new AddDevicesToManufacturers_Main(u);
                    tc.createDevicesFor(u, manufacturerName);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
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
        listOfManufactuersCreatedWithTesterInitials.add("ManufacturerST_9_1_948798_NU");
        listOfManufactuersCreatedWithTesterInitials.add("ManufacturerST_9_1_733556_HB");
        listOfManufactuersCreatedWithTesterInitials.add("ManufacturerST_9_1_142303_YC");
        listOfManufactuersCreatedWithTesterInitials.add("ManufacturerST_9_1_618693_PG");
        listOfManufactuersCreatedWithTesterInitials.add("ManufacturerST_9_1_386957_AN");
        return listOfManufactuersCreatedWithTesterInitials;
    }

    private static List<User> getListOfUsersFromExcel() {
        ExcelDataSheet excelUtils = new ExcelDataSheet();//
        List<User> listOfUsers = excelUtils.getListOfUsers("configs/data/excel/users.xlsx", "DeviceSetupLogins");
        listOfUsers = excelUtils.filterUsersBy(listOfUsers, "manufacturer");
        return listOfUsers;
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

    public AddDevicesToManufacturers_Main(User user) {
        this.username = user.getUserName();
        this.password = user.getPassword();
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
        if (registered != null && registered.toLowerCase().equals("registered"))
            addDevices = manufacturerDetails.clickAddDeviceBtn();
        else
            addDevices = new AddDevices(driver);
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
