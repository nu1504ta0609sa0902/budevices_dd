package com.mhra.mdcm.devices.dd.appian.utils.selenium.others;

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
}
