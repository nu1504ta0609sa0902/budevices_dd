package com.mhra.mdcm.devices.dd.appian._test.junit.device_injection.directly.OLD_STUFF.junit;

import com.mhra.mdcm.devices.dd.appian._test.junit.common.Common;
import com.mhra.mdcm.devices.dd.appian.domains.junit.User;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.DeviceData;
import com.mhra.mdcm.devices.dd.appian.utils.datadriven.ExcelDataSheet;
import com.mhra.mdcm.devices.dd.appian.utils.driver.BrowserConfig;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.FileUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by TPD_Auto on 01/11/2016.
 */
@RunWith(Parameterized.class)
public class AddDevicesToBusiness extends Common {

    public static final String AUTHORISED_REP_SMOKE_TEST = "AuthorisedRepST";
    public static final String MANUFACTURER_SMOKE_TEST = "ManufacturerST";

    public static List<DeviceData> listOfDeviceData = new ArrayList<>();
    public static WebDriver driver;
    public static String baseUrl;
    private String username;
    private String password;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<User> spreadsheetData() throws IOException {
        ExcelDataSheet excelUtils = new ExcelDataSheet();//
        List<User> listOfUsers = excelUtils.getListOfUsers("configs/data/excel/users.xlsx", "DeviceSetupLogins");
        listOfUsers = excelUtils.filterUsersBy(listOfUsers, "business");
        log.info("Business Users : " + listOfUsers);
        return listOfUsers;
    }


    public AddDevicesToBusiness(User user) {
        this.username = user.getUserName();
        this.password = user.getPassword();
    }

    @BeforeClass
    public static void setUpDriver() {
        if (driver == null) {
            listOfDeviceData = excelUtils.getListOfDeviceData("configs/data/excel/DevicesData.xlsx", "TestDataWellFormed_Simple");
            driver = new BrowserConfig().getDriver();
            driver.manage().window().maximize();
            baseUrl = FileUtils.getTestUrl();
            log.warn("\n\nINSERT DEVICES AS BUSINESS USER");
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
        //driver.manage().deleteAllCookies();
    }

    @Test
    public void setUpInitialDeviceDataForBusiness() {

    }



    @Override
    public String toString() {
        return "SmokeTestsBusiness";
    }
}
