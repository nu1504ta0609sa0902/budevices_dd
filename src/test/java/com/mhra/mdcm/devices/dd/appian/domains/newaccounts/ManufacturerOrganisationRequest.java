package com.mhra.mdcm.devices.dd.appian.domains.newaccounts;


import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.RandomDataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TPD_Auto
 *
 * CREATES DEFAULT MANUFACTURER OR AUTHORISEDREP DATA
 *
 * OVERRIDE THE DEFAULTS USING CUCUMBER SCENARIOS TEST DATA
 */
public class ManufacturerOrganisationRequest {

    public static final String MANUFACTURER_RT_TEST = "ManufacturerRT01Test";
    public static final String AUTHORISED_REP_RT_TEST = "AuthorisedRepRT01Test";
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

    //Contact Person Details
    public String title;
    public String firstName;
    public String lastName;
    public String jobTitle;
    public String phoneNumber;
    public String email;

    //Upload letter of designation
    public String letterOfDesignation;
    public String description;

    public boolean isManufacturer;


    public ManufacturerOrganisationRequest() {
        createDefaultRandom();
    }

    private void createDefaultRandom() {

        organisationName = RandomDataUtils.getRandomTestName("OrganisationTest");//.replace("_", "");

        //Organisation Details
        address1 = RandomDataUtils.getRandomNumberBetween(1, 200) + " " + RandomDataUtils.generateTestNameStartingWith("Test", 5) + " GrowLand Avenue";
        if(address1.equals("")){
            address1 = "111 This is weired St";
        }
        address2 = "South West";
        townCity = "London";
        postCode = "UB" + RandomDataUtils.getRandomNumberBetween(1, 19) + " " + RandomDataUtils.getRandomNumberBetween(1, 10) + "UU";
        country = "United Kingdom";
        telephone = "07941" + (int) RandomDataUtils.getRandomDigits(7);
        fax = "044941" + (int) RandomDataUtils.getRandomDigits(7);
        website = "www." + organisationName.toLowerCase() + ".com";

        //Contact Person Details
        title = getRandomTitle();
        firstName = RandomDataUtils.generateTestNameStartingWith("Noor", 2);
        lastName = RandomDataUtils.generateTestNameStartingWith("Uddin", 2);

        //Get real first name and last name

        jobTitle = getRandomJobTitle();
        phoneNumber = "01351" + (int) RandomDataUtils.getRandomDigits(7);
        email = "mhra.uat@gmail.com";

        isManufacturer = true;
        description = RandomDataUtils.generateTestNameStartingWith("Test Description of Document", 0);
    }

    private String getRandomTitle(){

        List<String> listOfTitles = new ArrayList<>();
        listOfTitles.add("Mr.");
        listOfTitles.add("Mrs.");
        listOfTitles.add("Miss");
        listOfTitles.add("Dr.");
        listOfTitles.add("Ms.");
        listOfTitles.add("Prof.");
        String index = RandomDataUtils.getSimpleRandomNumberBetween(0, listOfTitles.size() - 1);
        String title = listOfTitles.get(Integer.parseInt(index));

        return title;
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
        listOfTitles.add("Superman");
        listOfTitles.add("Batman");
        listOfTitles.add("CTO Microsoft");
        listOfTitles.add("Darth Vadar");
        listOfTitles.add("Master Of Universe");
        listOfTitles.add("Bill And Ted");
        listOfTitles.add("SHIELD");
        listOfTitles.add("Demolition Man");
        listOfTitles.add("Ninja");
        listOfTitles.add("Marines");

        String index = RandomDataUtils.getSimpleRandomNumberBetween(0, listOfTitles.size() - 1);
        String title = listOfTitles.get(Integer.parseInt(index));

        return title;
    }

    public static void main(String[] args){
        ManufacturerOrganisationRequest ar = new ManufacturerOrganisationRequest();
    }


    public void setUserDetails(String loggedInAs) {
        String[] data = loggedInAs.split("\\.");
        //System.out.println(data);
        firstName = data[0];

        //Because we have Auto.Business and Noor.Uddin.Business
        String name = generateLastName();
        if(data.length == 2){
            lastName = name;
        }else {
            String business = data[2];
            lastName = data[1] + "." + name;
        }
    }

    private String generateLastName() {
        String business = "";
        if(isManufacturer){
            business = "Manufacturer";
        }else{
            business = "AuthorisedRep";
        }
        return business;
    }


    @Override
    public String toString() {
        return "AccountManufacturerRequest{" +
                "\norganisationName='" + organisationName + '\'' +
//                "\naddress1='" + address1 + '\'' +
//                "\naddress2='" + address2 + '\'' +
//                "\ntownCity='" + townCity + '\'' +
//                "\npostCode='" + postCode + '\'' +
//                "\ncountry='" + country + '\'' +
//                "\ntelephone='" + telephone + '\'' +
//                "\nfax='" + fax + '\'' +
                "\nwebsite='" + website + '\'' +
//                "\nvatRegistrationNumber='" + vatRegistrationNumber + '\'' +
//                "\ncompanyRegistrationNumber='" + companyRegistrationNumber + '\'' +
//                "\ntitle='" + title + '\'' +
                "\nfirstName='" + firstName + '\'' +
                "\nlastName='" + lastName + '\'' +
//                "\njobTitle='" + jobTitle + '\'' +
//                "\nphoneNumber='" + phoneNumber + '\'' +
                "\nemail='" + email + '\'' +
                "\nisManufacturer=" + isManufacturer +
//                "\ndeviceRegistration=" + deviceRegistration +
//                "\ncfsCertificateOfFreeSale=" + cfsCertificateOfFreeSale +
//                "\nclinicalInvestigation=" + clinicalInvestigation +
//                "\naitsAdverseIncidentTrackingSystem=" + aitsAdverseIncidentTrackingSystem +
                '}';
    }


    public void updateName(String nameBeginsWith) {
        if(isManufacturer){
            organisationName = organisationName.replace("OrganisationTest", nameBeginsWith);
            website = website.replace("organisationtest", nameBeginsWith);
        }else{
            organisationName = organisationName.replace("OrganisationTest", nameBeginsWith);
            website = website.replace("organisationtest", nameBeginsWith);
        }
    }

    public void updateNameEnding(String nameEndsWith) {
        organisationName = organisationName + nameEndsWith;
        website = website.replace(".com", nameEndsWith + ".com");
    }
}
