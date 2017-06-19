package com.mhra.mdcm.devices.dd.appian.utils.datadriven;

import com.mhra.mdcm.devices.dd.appian.domains.junit.User;
import com.mhra.mdcm.devices.dd.appian.pageobjects.LoginPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TPD_Auto on 01/11/2016.
 * <p>
 * Note: I was forced to write some data driven testing in this way
 */
public class JUnitUtils {


    public static String getExpectedHeading(String username) {
        String expectedHeading = "Tasks";
        if (username.toLowerCase().contains("manufacturer")) {
            expectedHeading = "MHRA Service";
        } else if (username.toLowerCase().contains("authorised")) {
            expectedHeading = "MHRA Service";
        }
        return expectedHeading;
    }

    public static LoginPage logoutIfLoggedIn(String currentLoggedInUser, LoginPage loginPage) {
        if (currentLoggedInUser != null) {
            if (currentLoggedInUser.toLowerCase().contains("business")) {
                loginPage = loginPage.logoutIfLoggedIn();
            } else if (currentLoggedInUser.toLowerCase().contains("manufacturer")) {
                loginPage = loginPage.logoutIfLoggedInOthers();
            } else if (currentLoggedInUser.toLowerCase().contains("authorised")) {
                loginPage = loginPage.logoutIfLoggedInOthers();
            }
        }

        return loginPage;
    }

    public static List<String> getListOfTabSections() {
        List<String> listOfItems = new ArrayList<>();
        listOfItems.add("News");
        listOfItems.add("Tasks");
        listOfItems.add("Records");
        listOfItems.add("Reports");
        listOfItems.add("Actions");
        return listOfItems;
    }

    public static List<String> getListOfRecordsPageLinks() {
        List<String> listOfItems = new ArrayList<>();
        listOfItems.add("Accounts");
        listOfItems.add("All Devices");
        listOfItems.add("All Products");
        listOfItems.add("All Organisations");
        listOfItems.add("Devices");
        return listOfItems;
    }


    public static String getFirstPartOfUsername(String name) {
        String initial = name.split("_")[1];
        Map<String, String> mapOfNames = new HashMap<>();
        mapOfNames.put("NU", "Noor Uddin");
        mapOfNames.put("HB", "Hasanein.Bal-Alawi");
        mapOfNames.put("YC", "Yaaseen.Choudhury");
        mapOfNames.put("AN", "Andrew.Nisbet");
        mapOfNames.put("PG", "Priya.Giri");
        mapOfNames.put("LP", "Lambros.Poullais");
        mapOfNames.put("AT", "Auto");
        return mapOfNames.get(initial);
    }

    /**
     * Our username are as such Noor.Uddin.Manufacturer
     *
     * So we only want Noor.Uddin so we can change the user to
     *  = Noor.Uddin.Business or some other user
     *
     * @param username
     * @return
     */
    public static String getUserName(String username) {
        String name = null;
        String[] data = username.split("\\.");
        if(data.length == 3){
            name = data[0] + "." + data[1];
        }else if(data.length == 2){
            name = data[0];
        }
        return name;
    }

    public static User getBusinessUser(List<User> listOfUsers, String username) {
        String initial = username.split("_")[1];
        User user = null;
        for (User u : listOfUsers) {
            if (u.getInitials().contains(initial)) {
                user = u;
                break;
            }
        }
        return user;
    }
}
