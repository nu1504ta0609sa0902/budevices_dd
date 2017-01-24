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
import java.util.Map;

/**
 * Created by TPD_Auto
 */
public class TestHarnessUtils {


    public static String getName(String initials, boolean isFirstName) {
        Map<String, String> mapOfFirstNames = new HashMap<>();
        mapOfFirstNames.put("NU","Noor Uddin.Manufacturer");
        mapOfFirstNames.put("HB","Hasanein Bal-Alawi.Manufacturer");
        mapOfFirstNames.put("YC","Yaaseen Choudhury.Manufacturer");
        mapOfFirstNames.put("AN","Andrew Nisbet.Manufacturer");
        mapOfFirstNames.put("PG","Priya Giri.Manufacturer");
        mapOfFirstNames.put("LP","Lambros Poullais.Manufacturer");

        String names = mapOfFirstNames.get(initials);
        String[] data = names.split(" ");
        if(isFirstName){
            return data[0];
        }else{
            return data[1];
        }

    }

    public static String getName(String initials, User user, boolean isFirstName) {
        String names = user.getUserName();
        String[] data = names.split("\\.");
        if(isFirstName){
            return data[0];
        }else{
            return data[1] + "." + data[2];
        }
    }

    public static void takeScreenShot(WebDriver driver, String name) {
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String currentDir = com.mhra.mdcm.devices.dd.appian.utils.selenium.others.FileUtils.getFileFullPath("tmp", "screenshots");
        //String currentDir = System.getProperty("user.dir");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        try {
            org.apache.commons.io.FileUtils.copyFile(scrFile, new File(currentDir + File.separator + "ss_" + timeStamp + ".png"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
