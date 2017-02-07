package com.mhra.mdcm.devices.dd.appian.pageobjects.external.sections;

import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.DeviceData;
import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.AssertUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TPD_Auto
 */
public class ManufacturerDetails extends _Page {

    @FindBy(css = ".component_error")
    List <WebElement> errorMessages;

    @FindBy(css = "a.GFWJSJ4DGR[aria-label='Add a device']")
    WebElement addADevice;
    @FindBy(xpath = ".//button[contains(text(),'Declare devices')]")
    WebElement declareDevices;
    @FindBy(xpath = ".//a[contains(text(),'Amend Represented')]")
    WebElement amendRepresentativeParty;
    @FindBy(xpath = ".//a[contains(text(),'Edit Account Information')]")
    WebElement editAccountInformation;
    @FindBy(xpath = ".//a[.='Risk classification']//following::td[2]")
    List<WebElement> listOfGMDNDefinitions;


    //Contact details
    @FindBy(xpath = ".//span[contains(text(),'Full')]//following::p[1]")
    WebElement fullName;
    @FindBy(xpath = ".//span[contains(text(),'Job')]//following::p[1]")
    WebElement jobTitle;
    @FindBy(xpath = ".//span[contains(text(),'Email')]//following::p[1]")
    WebElement email;
    @FindBy(xpath = ".//h3[contains(text(),'Person Details')]//following::span[.='Telephone']/following::p[1]")
    WebElement telephone;

    //Organisation details
    @FindBy(xpath = ".//span[contains(text(),'Role')]//following::p[1]")
    WebElement role;

    //ORGANISATION DETAILS
    @FindBy(css = "div>h4")
    WebElement orgName;
//    @FindBy(xpath = ".//label[.='Organisation name']//following::p[1]")
//    WebElement orgName;
    @FindBy(xpath = ".//span[.='Address line 1']//following::p[1]")
    WebElement orgAddressLine1;
    @FindBy(xpath = ".//span[contains(text(),'Address line 2')]//following::p[1]")
    WebElement orgAddressLine2;
    @FindBy(xpath = ".//span[contains(text(),'City')]//following::p[1]")
    WebElement orgCityTown;
    @FindBy(xpath = ".//span[.='Postcode']//following::p[1]")
    WebElement orgPostCode;
    @FindBy(css = ".GFWJSJ4DEY.GFWJSJ4DIY>div")
    WebElement orgCountry;
    @FindBy(xpath = ".//span[contains(text(),'Telephone')]//following::p[1]")
    WebElement orgTelephone;
    @FindBy(xpath = ".//span[contains(text(),'Website')]//following::p[1]")
    WebElement webSite;


    public ManufacturerDetails(WebDriver driver) {
        super(driver);
    }

    public boolean isOrganisationNameCorrect(String name) {
        WaitUtils.waitForElementToBeClickable(driver, orgName, TIMEOUT_DEFAULT, false);
        boolean contains = orgName.getText().contains(name);
        return contains;
    }


    public boolean isErrorMessageDisplayed() {
        try {
            WaitUtils.waitForElementToBeVisible(driver, By.cssSelector(".component_error"), 3, false);
            boolean isDisplayed = errorMessages.size() > 0;
            return isDisplayed;
        }catch (Exception e){
            return false;
        }
    }

    public AddDevices clickAddDeviceBtn() {
        WaitUtils.waitForElementToBeVisible(driver, addADevice, TIMEOUT_MEDIUM, false);
        WaitUtils.waitForElementToBeClickable(driver, addADevice, TIMEOUT_MEDIUM, false);
        addADevice.click();
        return new AddDevices(driver);
    }

    public AddDevices clickDeclareDevicesBtn() {
        WaitUtils.waitForElementToBeVisible(driver, declareDevices, TIMEOUT_MEDIUM, false);
        WaitUtils.waitForElementToBeClickable(driver, declareDevices, TIMEOUT_MEDIUM, false);
        declareDevices.click();
        return new AddDevices(driver);
    }

//    public boolean isDisplayedDataCorrect(AccountManufacturerRequest manufacaturerData, DeviceData deviceData) {
//        //Check displayed devices are correct
//        String device = deviceData.gmdnTermOrDefinition;
//        boolean allValid = isDevicesDisplayedCorrect(device);
//        return allValid;
//    }

    public boolean isDevicesDisplayedCorrect(String deviceList) {
        String[] data = deviceList.split(",");
        WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//a[.='Risk classification']//following::td[2]"), TIMEOUT_MEDIUM, false);

        //Displayed list of gmdns
        List<String> gmdns = new ArrayList<>();
        for(WebElement el: listOfGMDNDefinitions){
            gmdns.add(el.getText());
        }

        //Verify it matches with my expected data set
        boolean allFound = true;
        for(String d: data){
            boolean foundOne = false;
            for(String gmdn: gmdns){
                if(gmdn.contains(d)){
                    foundOne = true;
                    break;
                }
            }

            //All of them must exists, therefore foundOne should be true
            if(!foundOne){
                allFound = false;
                break;
            }
        }

        return allFound;
    }

    public EditManufacturer amendRepresentedParty() {
        WaitUtils.waitForElementToBeClickable(driver, amendRepresentativeParty, TIMEOUT_MEDIUM, false);
        amendRepresentativeParty.click();
        return new EditManufacturer(driver);
    }

    public boolean isCorrectPage() {
        try {
            WaitUtils.waitForElementToBeClickable(driver, addADevice, TIMEOUT_MEDIUM, false);
            WaitUtils.waitForElementToBeClickable(driver, amendRepresentativeParty, TIMEOUT_MEDIUM, false);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
//
//    public boolean verifyManufacturerUpdatesDisplayedOnPage(String keyValuePairToUpdate, AccountRequest updatedData) {
//
//        WaitUtils.waitForElementToBeVisible(driver, email, TIMEOUT_DEFAULT, false);
//        WaitUtils.waitForElementToBeVisible(driver, fullName, TIMEOUT_MEDIUM, false);
//
//        boolean allChangesDisplayed = true;
//
//        //Check for the following
//        String[] dataPairs = keyValuePairToUpdate.split(",");
//
//        for(String pairs: dataPairs){
//            //String[] split = pairs.split("=");
//            String key = pairs;
//
//            //Contact details
//            if (key.equals("contact.title")) {
//                allChangesDisplayed = AssertUtils.areChangesDisplayed(fullName,  updatedData.title);
//            } else if (key.equals("contact.firstname")) {
//                allChangesDisplayed = AssertUtils.areChangesDisplayed(fullName,  updatedData.firstName);
//            } else if (key.equals("contact.lastname")) {
//                allChangesDisplayed = AssertUtils.areChangesDisplayed(fullName,  updatedData.lastName);
//            } else if (key.equals("contact.job.title")) {
//                allChangesDisplayed = AssertUtils.areChangesDisplayed(jobTitle,  updatedData.jobTitle);
//            } else if (key.equals("contact.email")) {
//                allChangesDisplayed = AssertUtils.areChangesDisplayed(email,  updatedData.email);
//            } else if (key.equals("contact.telephone")) {
//                allChangesDisplayed = AssertUtils.areChangesDisplayed(telephone,  updatedData.telephone);
//            }
//
//            boolean orgNameUpdated = false;
//            //Organisation details
//            if (key.equals("org.name")) {
//                allChangesDisplayed = AssertUtils.areChangesDisplayed(orgName, updatedData.organisationName);
//                orgNameUpdated = true;
//            }else if (key.equals("org.address1")) {
//                allChangesDisplayed = AssertUtils.areChangesDisplayed(orgAddressLine1, updatedData.address1);
//            }else if (key.equals("org.address2")) {
//                allChangesDisplayed = AssertUtils.areChangesDisplayed(orgAddressLine2, updatedData.address2);
//            }else if (key.equals("org.city")) {
//                allChangesDisplayed = AssertUtils.areChangesDisplayed(orgCityTown, updatedData.townCity);
//            }else if (key.equals("org.postcode")) {
//                allChangesDisplayed = AssertUtils.areChangesDisplayed(orgPostCode, updatedData.postCode);
//            }else if (key.equals("org.country")) {
//                allChangesDisplayed = AssertUtils.areChangesDisplayed(orgCountry, updatedData.country);
//            }else if (key.equals("org.telephone")) {
//                allChangesDisplayed = AssertUtils.areChangesDisplayed(orgTelephone, updatedData.telephone);
//            }else if (key.equals("org.website")) {
//                if(orgNameUpdated)
//                allChangesDisplayed = AssertUtils.areChangesDisplayed(webSite, updatedData.website);
//            }
//
//
//            if(!allChangesDisplayed){
//                break;
//            }
//        }
//
//        return allChangesDisplayed;
//    }

    public EditManufacturer editAccountInformation() {
        WaitUtils.waitForElementToBeClickable(driver, editAccountInformation, TIMEOUT_MEDIUM, false);
        editAccountInformation.click();
        return new EditManufacturer(driver);
    }

    public ProductDetails viewProduct(DeviceData deviceData) {
        if(deviceData.device!=null) {
            WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText(deviceData.device), TIMEOUT_MEDIUM, false);
            driver.findElement(By.partialLinkText(deviceData.device)).click();
        }
        return new ProductDetails(driver);
    }
}
