package com.mhra.mdcm.devices.dd.appian._test.junit.smoke;

import com.mhra.mdcm.devices.dd.appian._test.junit.common.Common;
import com.mhra.mdcm.devices.dd.appian.domains.junit.User;
import com.mhra.mdcm.devices.dd.appian.pageobjects.LoginPage;
import com.mhra.mdcm.devices.dd.appian.pageobjects.MainNavigationBar;
import com.mhra.mdcm.devices.dd.appian.utils.datadriven.ExcelDataSheet;
import com.mhra.mdcm.devices.dd.appian.utils.datadriven.JUnitUtils;
import com.mhra.mdcm.devices.dd.appian.utils.driver.BrowserConfig;
import com.mhra.mdcm.devices.dd.appian.utils.network.NetworkUtils;
import org.hamcrest.Matchers;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by TPD_Auto on 01/11/2016.
 */
@RunWith(Parameterized.class)
public class SmokeTestsAuthorisedRep extends Common {


    public static WebDriver driver;
    public static String baseUrl;
    private String username;
    private String password;

    @Parameterized.Parameters(name="{0}")
    public static Collection<User> spreadsheetData() throws IOException {
        ExcelDataSheet excelUtils = new ExcelDataSheet();//
        List<User> listOfUsers = excelUtils.getListOfUsers("configs/data/excel/users.xlsx", "Sheet1");
        listOfUsers = excelUtils.filterUsersBy(listOfUsers, "authorised");
        log.info("AuthorisedRep Users : " + listOfUsers);
        return listOfUsers;
    }

    public SmokeTestsAuthorisedRep(User user) {
        this.username = user.getUserName();
        this.password = user.getPassword();
    }

    @BeforeClass
    public static void setUpDriver() {
        if (driver == null) {
            driver = new BrowserConfig().getDriver();
            baseUrl = NetworkUtils.getTestUrl(baseUrl);
            log.warn("\n\nRUNNING AUTHORISED REP SMOKE TESTS");
        }
    }

    @AfterClass
    public static void clearBrowsers() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Before
    public void setupTest(){
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
    public void checkCorrectLinksAreDisplayedForAuthorisedRep() {

        if (username.toLowerCase().contains("manufacturer") || username.toLowerCase().contains("authorised")) {

            LoginPage loginPage = new LoginPage(driver);
            loginPage = loginPage.loadPage(baseUrl);
            MainNavigationBar mainNavigationBar = loginPage.loginAs(username, password);

            portalPage = mainNavigationBar.clickPortals();
            String delimitedLinks = "Manufacturer Registration";
            boolean areLinksVisible = portalPage.areLinksVisible(delimitedLinks);
            Assert.assertThat("Expected to see the following links : " + delimitedLinks, areLinksVisible, Matchers.is(true));
        }
    }


}
