package com.mhra.mdcm.devices.dd.appian.pageobjects.external.sections;

import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Created by TPD_Auto
 */
public class EditManufacturer extends _Page {


    //ORGANISATION DETAILS
    @FindBy(xpath = ".//label[.='Organisation name']//following::input[1]")
    WebElement orgName;
    @FindBy(xpath = ".//label[.='Address line 1']//following::input[1]")
    WebElement orgAddressLine1;
    @FindBy(xpath = ".//label[contains(text(),'Address line 2')]//following::input[1]")
    WebElement orgAddressLine2;
    @FindBy(xpath = ".//label[contains(text(),'City')]//following::input[1]")
    WebElement orgCityTown;
    @FindBy(xpath = ".//label[.='Postcode']//following::input[1]")
    WebElement orgPostCode;
    @FindBy(css = ".GFWJSJ4DEY.GFWJSJ4DIY>div")
    WebElement orgCountry;
    @FindBy(xpath = ".//label[contains(text(),'Telephone')]//following::input[1]")
    WebElement orgTelephone;
    @FindBy(xpath = ".//label[contains(text(),'Website')]//following::input[1]")
    WebElement webSite;

    @FindBy(xpath = ".//span[contains(text(),'Address type')]//following::p[1]")
    WebElement addressType;

    @FindBy(css = ".component_error")
    List <WebElement> errorMessages;

    @FindBy(xpath = ".//button[.='Yes']")
    WebElement confirmYes;
    @FindBy(xpath = ".//button[.='No']")
    WebElement confirmNo;

    @FindBy(xpath = ".//button[contains(text(),'Save')]")
    List<WebElement> saveYes;
    @FindBy(xpath = ".//button[contains(text(),'Cancel')]")
    WebElement saveNo;

    //Submit or cancel button
    @FindBy(css = "button.GFWJSJ4DCF")
    WebElement submitBtn;
    @FindBy(css = ".GFWJSJ4DFXC.left button.GFWJSJ4DNE")
    WebElement cancelBtn;

    //Contact details
    @FindBy(xpath = ".//span[contains(text(),'Title')]//following::select[1]")
    WebElement title;
    @FindBy(xpath = ".//label[.='First name']//following::input[1]")
    WebElement firstName;
    @FindBy(xpath = ".//label[.='Last name']//following::input[1]")
    WebElement lastName;
    @FindBy(xpath = ".//label[contains(text(),'Job title')]//following::input[1]")
    WebElement jobTitle;
    @FindBy(xpath = ".//label[.='Email']//following::input[1]")
    WebElement email;
    @FindBy(xpath = ".//label[.='Email']//following::input[2]")
    WebElement telephone;

    public EditManufacturer(WebDriver driver) {
        super(driver);
    }


//    public EditManufacturer updateFollowingFields(String keyValuePairToUpdate, AccountRequest updatedData) {
//
//        WaitUtils.waitForElementToBeClickable(driver, submitBtn, TIMEOUT_5_SECOND, false);
//        String[] dataPairs = keyValuePairToUpdate.split(",");
//
//        for (String pairs : dataPairs) {
//
//            String key = pairs;
//            boolean orgNameUpdated = false;
//
//            //Organisation details
//            if (key.equals("org.name")) {
//                PageUtils.updateElementValue(driver, orgName, updatedData.organisationName, TIMEOUT_5_SECOND);
//                orgNameUpdated = true;
//            }else if (key.equals("org.address1")) {
//                PageUtils.updateElementValue(driver, orgAddressLine1, updatedData.address1, TIMEOUT_5_SECOND);
//            }else if (key.equals("org.address2")) {
//                PageUtils.updateElementValue(driver, orgAddressLine2, updatedData.address2, TIMEOUT_5_SECOND);
//            }else if (key.equals("org.city")) {
//                PageUtils.updateElementValue(driver, orgCityTown, updatedData.townCity, TIMEOUT_5_SECOND);
//            }else if (key.equals("org.postcode")) {
//                PageUtils.updateElementValue(driver, orgPostCode, updatedData.postCode, TIMEOUT_5_SECOND);
//            }else if (key.equals("org.country")) {
//                driver.findElement(By.cssSelector(".GFWJSJ4DEY.GFWJSJ4DIY a:nth-child(2)")).click();
//                driver.findElement(By.cssSelector(".GFWJSJ4DEY.GFWJSJ4DMX>div input")).clear();
//                PageUtils.selectFromAutoSuggests(driver, By.xpath(".//label[.='Country']//following::input[1]"), updatedData.country);
//            }else if (key.equals("org.telephone")) {
//                PageUtils.updateElementValue(driver, orgTelephone, updatedData.telephone, TIMEOUT_5_SECOND);
//            }else if (key.equals("org.website")) {
//                if(orgNameUpdated)
//                PageUtils.updateElementValue(driver, webSite, updatedData.website, TIMEOUT_5_SECOND);
//            }
//
//            //Contact details
//            if (key.equals("contact.title")) {
//                PageUtils.selectByText(title, updatedData.title);
//            }else if (key.equals("contact.firstname")) {
//                PageUtils.updateElementValue(driver, firstName, updatedData.firstName, TIMEOUT_5_SECOND);
//            } else if (key.equals("contact.lastname")) {
//                PageUtils.updateElementValue(driver, lastName, updatedData.lastName, TIMEOUT_5_SECOND);
//            } else if (key.equals("contact.job.title")) {
//                PageUtils.updateElementValue(driver, jobTitle, updatedData.jobTitle, TIMEOUT_5_SECOND);
//            } else if (key.equals("contact.email")) {
//                PageUtils.updateElementValue(driver, email, updatedData.email, TIMEOUT_5_SECOND);
//            } else if (key.equals("contact.telephone")) {
//                PageUtils.updateElementValue(driver, telephone, updatedData.telephone, TIMEOUT_5_SECOND);
//            }
//        }
//
//        PageUtils.doubleClick(driver, submitBtn);
//
//        return new EditManufacturer(driver);
//    }
//
//
//    public boolean isErrorMessageDisplayed() {
//        try {
//            WaitUtils.waitForElementToBeVisible(driver, By.cssSelector(".component_error"), 3, false);
//            boolean isDisplayed = errorMessages.size() > 0;
//            return isDisplayed;
//        }catch (Exception e){
//            return false;
//        }
//    }
//
//
//    public MyAccountPage saveChanges(boolean saveChanges) {
//        WaitUtils.waitForElementToBeClickable(driver, saveNo, TIMEOUT_DEFAULT, false);
//        if(saveChanges){
//            saveYes.get(1).click();
//        }else{
//            saveNo.click();
//        }
//        return new MyAccountPage(driver);
//    }
//
//
//    public boolean isAddressTypeEditable() {
//        boolean isEditable = true;
//        WaitUtils.waitForElementToBeClickable(driver, orgName, TIMEOUT_5_SECOND, false);
//        WaitUtils.waitForElementToBeClickable(driver, addressType, TIMEOUT_5_SECOND, false);
//        try{
//            addressType.sendKeys("not editable");
//        }catch (Exception e){
//            isEditable = false;
//        }
//        return isEditable;
//    }
//
//
//    public ManufacturerDetails confirmChanges(boolean confirm) {
//        WaitUtils.waitForElementToBeClickable(driver, confirmYes, TIMEOUT_DEFAULT, false);
//        if(confirm){
//            confirmYes.click();
//        }else{
//            confirmNo.click();
//        }
//        return new ManufacturerDetails(driver);
//    }

}
