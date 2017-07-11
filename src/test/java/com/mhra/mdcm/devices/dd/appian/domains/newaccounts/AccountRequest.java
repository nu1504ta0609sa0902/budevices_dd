package com.mhra.mdcm.devices.dd.appian.domains.newaccounts;

import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.RandomDataUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.TestHarnessUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TPD_Auto on 25/10/2016.
 *
 * OVERRIDE THE DEFAULTS
 */
public class AccountRequest {

    public String organisationName;

    //Organisation Details
    public String address1;
    public String address2;
    public String townCity;
    public String postCode;
    public String country;
    public String telephone;
    public String fax;
    public String website;
    public boolean addressType;

    //Organisation type
    public String organisationType;
    public String vatRegistrationNumber;
    public String companyRegistrationNumber;

    //Contact Person Details
    public String title;
    public String firstName;
    public String lastName;
    public String jobTitle;
    public String phoneNumber;
    public String email;
    public String initials;

    //New username and password for the newly created user, Assumes email received with temporary password
    public String userName;
    public String tempPassword;
    public String newPassword;

    //Organisation Role
//    public String autorisedRep;
//    public String manufacturer;
    public boolean isManufacturer;
    public String organisationRole;

    //Services of interest
    public boolean deviceRegistration;
    public boolean cfsCertificateOfFreeSale;
    public boolean clinicalInvestigation;
    public boolean aitsAdverseIncidentTrackingSystem;


    public AccountRequest() {
        createDefaultRandom();
    }

    private void createDefaultRandom() {

        organisationName = RandomDataUtils.getRandomTestName("OrganisationTest").replace("_", "");

        //Organisation Details
        address1 = RandomDataUtils.getRandomNumberBetween(1, 200) + " " + RandomDataUtils.generateTestNameStartingWith("Test", 20) + " GrowLand Avenue";
        if (address1.equals("")) {
            address1 = "111 This is weired St";
        }
        address2 = "South West";
        townCity = "London";
        postCode = "UB" + RandomDataUtils.getRandomNumberBetween(1, 19) + " " + RandomDataUtils.getRandomNumberBetween(1, 10) + "UU";
        country = "United Kingdom";
        addressType = true;
        telephone = "07941" + (int) RandomDataUtils.getRandomDigits(7);
        fax = "044941" + (int) RandomDataUtils.getRandomDigits(7);
        website = "www." + organisationName.toLowerCase() + ".com";

        //Organisation type
        organisationType = "Other";
        vatRegistrationNumber = "0161" + (int) RandomDataUtils.getRandomDigits(7);
        companyRegistrationNumber = "0895" + (int) RandomDataUtils.getRandomDigits(7);

        //Contact Person Details
        title = "Prof.";
        firstName = RandomDataUtils.generateTestNameStartingWith("Noor", 5); //RandomDataUtils.getRandomTestName("Noor").replace("_", "");
        lastName = RandomDataUtils.generateTestNameStartingWith("Uddin", 5); //RandomDataUtils.getRandomTestName("Uddin").replace("_", "");
        jobTitle = getRandomJobTitle();
        phoneNumber = "01351" + (int) RandomDataUtils.getRandomDigits(7);
        email = "mhra.uat@gmail.com";

        //Organisation Role
        isManufacturer = true;
        organisationRole = "Manufacturer";

        //Services of interest
        deviceRegistration = true;
        cfsCertificateOfFreeSale = true;
        clinicalInvestigation = false;
        aitsAdverseIncidentTrackingSystem = false;
    }

    private String getRandomJobTitle() {
        List<String> listOfTitles = new ArrayList<>();
        listOfTitles.add("Tester");
        listOfTitles.add("DeveloperInTest");
        listOfTitles.add("Head of Manufacturing");
        listOfTitles.add("The Boss");
        listOfTitles.add("Head of Testing");
        listOfTitles.add("Automated Tester");
        listOfTitles.add("Chief");

        String index = RandomDataUtils.getSimpleRandomNumberBetween(0, listOfTitles.size() - 1);
        String title = listOfTitles.get(Integer.parseInt(index));

        return title;
    }


    public void setUserDetails(String loggedInAs) {
        String[] data = loggedInAs.split("\\.");
        //System.out.println(data);
        firstName = data[0];

        //Because we have Auto.Business and Noor.Uddin.Business
        String name = generateLastName();
        if (data.length == 2) {
            lastName = name;
        } else {
            if (data.length == 1) {
                lastName = name;
                //ASSUMING excel sheet username is something like Manufacturer_NU or AuthorisedRep_AT etc
                String initial = loggedInAs.split("_")[1];
                firstName = TestHarnessUtils.getHardcodedFirstName(initial);
            } else {
                String business = data[2];
                lastName = data[1] + "." + name;
            }
        }
    }

    private String generateLastName() {
        String business = "";
        if (organisationRole != null) {
            if (organisationRole.toLowerCase().equals("distributor")) {
                business = "Distributor";
            } else if (organisationRole.toLowerCase().equals("notifiedbody")) {
                business = "NotifiedBody";
            } else {
                //It can only be a manufacturer or authorisedRep
                if (isManufacturer) {
                    business = "Manufacturer";
                } else {
                    business = "AuthorisedRep";
                }
            }
        }
        return business;
    }


    public String getUserName(boolean aRandomOne) {
        String lastName = generateLastName();
        if (aRandomOne) {
            //lastName = lastName + RandomDataUtils.getTodaysDate(false, "");
            lastName = lastName + RandomDataUtils.getTodaysDate(false, "") + "_" + RandomDataUtils.getTimeHour();
        }
        String userName = lastName + "_" + initials;
        System.out.println("Create account with UserName : " + userName);
        this.userName = userName;
        return userName;
    }

    public void updateName(String nameBeginsWith) {
        if (organisationRole != null) {
            if (organisationRole.toLowerCase().equals("distributor")) {
                organisationName = organisationName.replace("OrganisationTest", nameBeginsWith);
                website = website.replace("organisationtest", nameBeginsWith);
            } else if (organisationRole.toLowerCase().equals("notifiedbody")) {
                organisationName = organisationName.replace("OrganisationTest", nameBeginsWith);
                website = website.replace("organisationtest", nameBeginsWith);
            } else if (isManufacturer) {
                organisationName = organisationName.replace("OrganisationTest", nameBeginsWith);
                website = website.replace("organisationtest", nameBeginsWith);
            } else {
                organisationName = organisationName.replace("OrganisationTest", nameBeginsWith);
                website = website.replace("organisationtest", nameBeginsWith);
            }
        }
    }

    public void updateNameEnding(String nameEndsWith) {
        organisationName = organisationName + nameEndsWith;
        website = website.replace(".com", nameEndsWith + ".com");
    }
}
