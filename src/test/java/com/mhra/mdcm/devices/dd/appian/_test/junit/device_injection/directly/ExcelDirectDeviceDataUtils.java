package com.mhra.mdcm.devices.dd.appian._test.junit.device_injection.directly;

import com.mhra.mdcm.devices.dd.appian._test.junit.common.Common;
import com.mhra.mdcm.devices.dd.appian.domains.junit.User;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.DeviceData;
import com.mhra.mdcm.devices.dd.appian.utils.datadriven.ExcelDataSheet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TPD_Auto on 16/01/2017.
 */
public class ExcelDirectDeviceDataUtils extends Common {

    private static List<DeviceData> listOfDeviceData = null;


    public static List<User> getListOfUsersFromExcel(String typeOfUsers) {
        ExcelDataSheet excelUtils = new ExcelDataSheet();//
        List<User> listOfUsers = excelUtils.getListOfUsers("configs/data/excel/users.xlsx", "Sheet1", false);
        listOfUsers = excelUtils.filterUsersBy(listOfUsers, typeOfUsers);
        return listOfUsers;
    }

    public static List<User> getListOfBusinessUsersFromExcel(String typeOfUsers) {
        ExcelDataSheet excelUtils = new ExcelDataSheet();//
        List<User> listOfUsers = excelUtils.getListOfUsers("configs/data/excel/users.xlsx", "Sheet1", false);
        List<User> listOfBusinessUsers = excelUtils.filterUsersBy(listOfUsers, typeOfUsers);
        return listOfBusinessUsers;
    }

    public static List<DeviceData> getListOfDeviceData() {
        if (listOfDeviceData == null) {
            //We only want to execute this once
            listOfDeviceData = excelUtils.getListOfDeviceData("configs/data/excel/DevicesData.xlsx", "TestDataWellFormed_Simple");
        }
        return listOfDeviceData;
    }


    public static User getCorrectLoginDetails(String initialOfUser, List<User> listOfUsers) {
        User selectCorrectUser = null;
        for (User u : listOfUsers) {
            String initials = "_" + u.getInitials();
            if (initialOfUser.contains(initials)) {
                selectCorrectUser = u;
                break;
            }
        }
        return selectCorrectUser;
    }

    public static User getCorrectLoginDetailsManufacturer(String initialsOfUser, List<User> listOfUsers) {
        User selectCorrectUser = null;
        for (User u : listOfUsers) {
            String un = u.getInitials();
            if (initialsOfUser.contains(un)) {
                selectCorrectUser = u;
                break;
            }
        }

        return selectCorrectUser;
    }


    public static List<DeviceData> getValidatedDataOnly(boolean onlyValidatedData, List<DeviceData> listOfDevicesOfSpecificType) {
        List<DeviceData> listOfValidatedData = new ArrayList<>();

        if (onlyValidatedData) {
            for (DeviceData dd : listOfDevicesOfSpecificType) {
                if (dd.validatedData.equals("Y")) {
                    listOfValidatedData.add(dd);
                }
            }
        } else {
            listOfValidatedData = listOfDevicesOfSpecificType;
        }
        return listOfValidatedData;

    }


    public static List<DeviceData> getListOfDevicesOfSpecificType(List<DeviceData> listOfDeviceData, String specificType) {
        if (specificType.equals("all devices")) {
            return listOfDeviceData;
        } else {
            //Filter specific data
            List<DeviceData> listOfDevicesOfType = new ArrayList<>();
            for (DeviceData dd : listOfDeviceData) {
                if (dd.deviceType.contains(specificType)) {
                    listOfDevicesOfType.add(dd);
                }
            }
            return listOfDevicesOfType;
        }
    }

    public static void printFailingData(List<DeviceData> listOfDevicesWhichHadProblems, String deviceType) {
        log.info("FINISHED ADDING DEVICE TYPE : " + deviceType);
        log.info("Number of invalid data : " + listOfDevicesWhichHadProblems.size());
        for (DeviceData data : listOfDevicesWhichHadProblems) {
            log.info("Excel File Line Number : " + data.excelFileLineNumber);
            log.info("Validated Data : " + data.validatedData);
            log.info("Device Type : " + data.deviceType);
            log.info("Device Term/Definition : " + data.device);
            log.info("Risk Classification : " + data.riskClassification);
        }
    }

    public static void printDeviceData(DeviceData data) {
        log.info("Excel File Line Number : " + data.excelFileLineNumber);
        log.info("Validated Data : " + data.validatedData);
        log.info("Device Type : " + data.deviceType);
        log.info("Device Term/Definition : " + data.device);
        log.info("Risk Classification : " + data.riskClassification);
    }

    public static DeviceData getDeviceDataCalled(List<DeviceData> listOfDevicesWhichHadProblems, String abacus) {
        DeviceData dd = null;
        for (DeviceData data : listOfDevicesWhichHadProblems) {
            String definition = data.device;
            if (definition.equals(abacus)) {
                dd = data;
                break;
            }
        }
        return dd;
    }

    public static void addToListOfManufacturersCreatedWithInitials(String initials, List<String> listOfManufactuersCreatedWithTesterInitials, String organisationName) {
        List<String> remove = new ArrayList<>();
        for (String name : listOfManufactuersCreatedWithTesterInitials) {
            if (name.contains(initials)) {
                remove.add(name);
            }
        }

        listOfManufactuersCreatedWithTesterInitials.removeAll(remove);
    }

}
