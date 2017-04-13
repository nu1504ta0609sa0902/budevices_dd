package com.mhra.mdcm.devices.dd.appian.utils.selenium.others;

import com.mhra.mdcm.devices.dd.appian.domains.junit.User;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TPD_Auto
 */
public class TestHarnessUtils {


    public static String getName(String initials, boolean isFirstName, List<User> listOfManufacturerUsers) {
        String name = null;
        if(listOfManufacturerUsers == null) {
            User user = getUser(initials, listOfManufacturerUsers);
            String[] split = user.getUserName().split("\\.");
            name = split[0] + " " + split[1];
            if(split.length > 2){
                name = name + "." + split[2];
            }
        }else {
            //REMOVE THIS WHEN BUG IS RESOLVED
            Map<String, String> mapOfFirstNames = getHardcodedNames();
            name = mapOfFirstNames.get(initials);
        }

        String[] data = name.split(" ");
        if(isFirstName){
            return data[0];
        }else{
            return data[1];
        }

    }

    private static Map<String,String> getHardcodedNames() {
        Map<String, String> mapOfNames = new HashMap<>();
        mapOfNames.put("NU", "Noor Uddin.Manufacturer");
        mapOfNames.put("HB", "Hasanein Bal-Alawi.Manufacturer");
        mapOfNames.put("YC", "Yaaseen Choudhury.Manufacturer");
        mapOfNames.put("AN", "Andrew Nisbet.Manufacturer");
        mapOfNames.put("PG", "Priya Giri.Manufacturer");
        mapOfNames.put("LP", "Lambros Poullais.Manufacturer");
        mapOfNames.put("AT", "Auto Manufacturer");
        return mapOfNames;
    }

    private static User getUser(String initials, List<User> listOfManufacturerUsers) {
        User user = null;
        for (User u : listOfManufacturerUsers) {
            if (u.getInitials().contains(initials)) {
                user = u;
                break;
            }
        }
        return user;
    }

    public static String getName(String initials, User user, boolean isFirstName) {
        String names = user.getUserName();
        String[] data = names.split("\\.");
        if(isFirstName){
            return data[0];
        }else{
            if(data.length> 2)
                return data[1] + "." + data[2];
            else
                return data[1];
        }
    }

    public static void takeScreenShot(WebDriver driver, String name, boolean saveInTargetFolder) {
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String currentDir = FileUtils.getFileFullPath("tmp", "screenshots");
        if(saveInTargetFolder){
            currentDir = FileUtils.getTargetFileFullPath("target", "screenshots");
        }

        String timeStamp = new SimpleDateFormat("HHmm").format(Calendar.getInstance().getTime());
        String subDir = "SS_" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        try {
            org.apache.commons.io.FileUtils.copyFile(scrFile, new File(currentDir + File.separator + subDir + File.separator + timeStamp + "_" + name + ".png"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
