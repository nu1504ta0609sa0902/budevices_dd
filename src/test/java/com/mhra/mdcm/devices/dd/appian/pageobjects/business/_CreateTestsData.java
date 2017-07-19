package com.mhra.mdcm.devices.dd.appian.pageobjects.business;

import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.AccountRequest;
import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.RandomDataUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.PageUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by TPD_Auto 
 */

public class _CreateTestsData extends _Page {

    //Organisation details
    @FindBy(xpath = ".//label[.='Organisation name']//following::input[1]")
    WebElement orgName;
    @FindBy(xpath = ".//label[contains(text(),'Address line 1')]//following::input[1]")
    WebElement addressLine1;
    @FindBy(xpath = ".//label[contains(text(),'Address line 2')]//following::input[1]")
    WebElement addressLine2;
    @FindBy(xpath = ".//label[contains(text(),'City')]//following::input[1]")
    WebElement townCity;
    @FindBy(xpath = ".//label[contains(text(),'County')]//following::input[2]")
    WebElement postCode;
    @FindBy(xpath = ".//*[contains(text(),'Address type')]/following::input[6]")
    WebElement telephone;
    @FindBy(xpath = ".//label[contains(text(),'Fax')]//following::input[1]")
    WebElement fax;
    @FindBy(xpath = ".//label[contains(text(),'Website')]//following::input[1]")
    WebElement website;
    @FindBy(xpath = ".//label[contains(text(),'Registered Address')]")
    WebElement addressType;
    @FindBy(xpath = ".//label[contains(text(),'Country')]//following::input[1]")
    WebElement country;
    @FindBy(xpath = ".//a/u[contains(text(), 'Enter address')]")
    WebElement linkEnterAddressManually;

    //Organisation Type
    @FindBy(xpath = ".//label[contains(text(),'Limited Company')]")
    WebElement limitedCompany;
    @FindBy(xpath = ".//label[contains(text(),'Business Partnership')]")
    WebElement businessPartnership;
    @FindBy(xpath = ".//label[contains(text(),'Unincorporated Association')]")
    WebElement unincorporatedAssociation;
    @FindBy(xpath = ".//label[contains(text(),'Other')]")
    WebElement other;

    final String selectedType = "Selected type";
    @FindBy(xpath = ".//span[.='" + selectedType + "']//following::input[5]")
    WebElement vatRegistrationNumber;
    @FindBy(xpath = ".//span[.='" + selectedType + "']//following::input[6]")
    WebElement companyRegistrationNumber;

    //Contact Person Details
    @FindBy(xpath = ".//span[contains(text(),'Title')]//following::div[@role='listbox']")
    WebElement title;
    @FindBy(xpath = ".//label[.='First name']//following::input[1]")
    WebElement firstName;
    @FindBy(xpath = ".//label[.='Last name']//following::input[1]")
    WebElement lastName;
    @FindBy(xpath = ".//label[contains(text(),'Job title')]//following::input[1]")
    WebElement jobTitle;
    @FindBy(xpath = ".//h2[contains(text(),'Person Details')]//following::input[4]")
    WebElement phoneNumber;
    @FindBy(xpath = ".//label[.='Email']//following::input[1]")
    WebElement emailAddress;
    @FindBy(xpath = ".//label[.='User name']//following::input[1]")
    WebElement userName;

    //Organisational Role
    @FindBy(xpath = ".//label[contains(text(),'Authorised Rep')]")
    WebElement authorisedRep;
    @FindBy(xpath = ".//label[contains(text(),'Manufacturer')]")
    WebElement manufacturer;
    @FindBy(xpath = ".//label[contains(text(),'Distributor')]")
    WebElement distributor;
    @FindBy(xpath = ".//label[contains(text(),'Notified Body')]")
    WebElement notifiedBody;

    //Services of Interests
    @FindBy(xpath = ".//label[contains(text(),'Account Management')]")
    WebElement accountManagement;
    @FindBy(xpath = ".//label[contains(text(),'Device Registration')]")
    WebElement deviceReg;
    @FindBy(xpath = ".//label[contains(text(),'Certificate of Freesales')]")
    WebElement cfsCertification;
    @FindBy(xpath = ".//label[contains(text(),'Clinical Investigation')]")
    WebElement clinicalInvestigation;
    @FindBy(xpath = ".//label[contains(text(),'Adverse Incident Tracking System')]")
    WebElement aitsAdverseIncidient;

    //Terms and condition checkbox
    @FindBy(xpath = ".//input[@type='checkbox']/following::label[1]")
    WebElement cbxTermsAndConditions;

    //Submit and cancel
    @FindBy(xpath = ".//button[contains(text(),'Submit')]")
    WebElement submit;
    @FindBy(xpath = ".//button[.='Cancel']")
    WebElement cancel;

    public _CreateTestsData(WebDriver driver) {
        super(driver);
    }

    /**
     * HELPS TESTERS CREATE TEST DATA ON THE GO
     * @param ar
     * @return
     */
    public ActionsPage createNewAccountUsingBusinessTestHarness(AccountRequest ar) {
        WaitUtils.waitForPageToLoad(driver, By.xpath(".//label[.='Organisation name']//following::input[1]"), 5, false); ;
        WaitUtils.waitForElementToBeClickable(driver, orgName, TIMEOUT_20_SECOND, false);
        orgName.sendKeys(ar.organisationName);

        //Enter address manually if required
        enterAddressManually();

        //Selecting country has changed to auto suggest
        boolean exception = false;
        try {
            orgName.click();
            PageUtils.selectFromAutoSuggestedListItems(driver, ".PickerWidget---picker_value", ar.country, true);
        }catch (Exception e){
            exception = true;
        }

        //Organisation details
        WaitUtils.waitForElementToBeClickable(driver, addressLine1, TIMEOUT_10_SECOND, false);
        addressLine1.clear();
        addressLine1.sendKeys(ar.address1);
        addressLine2.sendKeys(ar.address2);
        townCity.sendKeys(ar.townCity);
        postCode.sendKeys(ar.postCode);
        telephone.sendKeys(ar.telephone);
        fax.sendKeys(ar.fax);
        website.sendKeys(ar.website);
        if(ar.addressType){
            PageUtils.doubleClick(driver, addressType);
        }

        //Organisation Type
        if(ar.organisationType.equals("Limited Company")){
            PageUtils.doubleClick(driver, limitedCompany);
            PageFactory.initElements(driver, this);
            WaitUtils.waitForElementToBeVisible(driver, companyRegistrationNumber, 5, false);
            WaitUtils.nativeWaitInSeconds(1);
            WaitUtils.waitForElementToBeClickable(driver, companyRegistrationNumber, 5, false);
            WaitUtils.nativeWaitInSeconds(1);
            vatRegistrationNumber.sendKeys(ar.vatRegistrationNumber);
            companyRegistrationNumber.sendKeys(ar.companyRegistrationNumber);

        }else if(ar.organisationType.equals("Business Partnership")){
            PageUtils.doubleClick(driver, businessPartnership);
            PageFactory.initElements(driver, this);
            WaitUtils.waitForElementToBeVisible(driver, vatRegistrationNumber, 5, false);
            WaitUtils.nativeWaitInSeconds(1);
            WaitUtils.waitForElementToBeClickable(driver, vatRegistrationNumber, 5, false);
            WaitUtils.nativeWaitInSeconds(1);
            vatRegistrationNumber.sendKeys(ar.vatRegistrationNumber);

        }else if(ar.organisationType.equals("Unincorporated Association")){
            PageUtils.doubleClick(driver, unincorporatedAssociation);
            PageFactory.initElements(driver, this);

        }else if(ar.organisationType.equals("Other")){
            PageUtils.doubleClick(driver, other);
            PageFactory.initElements(driver, this);

        }

        //Contact Person Details
        try {
            PageUtils.singleClick(driver, title);
            WaitUtils.isPageLoadingComplete(driver, 1);
            WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//div[contains(text(), '"+ ar.title + "')]"), TIMEOUT_5_SECOND, false);
            WebElement titleToSelect = driver.findElement(By.xpath(".//div[contains(text(), '"+ ar.title + "')]"));
            PageUtils.singleClick(driver, titleToSelect);
        } catch (Exception e) {
            e.printStackTrace();
        }

        firstName.sendKeys(ar.firstName);
        lastName.sendKeys(ar.lastName);
        jobTitle.sendKeys(ar.jobTitle);
        phoneNumber.sendKeys(ar.phoneNumber);
        emailAddress.sendKeys(ar.email);
        userName.sendKeys(ar.getUserName(true));

        //Organisation Role
        if(ar.organisationRole.toLowerCase().equals("distributor")){
            PageUtils.doubleClick(driver, distributor);
        }else if(ar.organisationRole.toLowerCase().equals("notifiedbody")){
            PageUtils.doubleClick(driver, notifiedBody);
        }else{
            //Is either a manufacturer or authorisedRep
            if(ar.isManufacturer){
                PageUtils.doubleClick(driver, manufacturer);
            }else{
                PageUtils.doubleClick(driver, authorisedRep);
            }
        }

        try {
            //Services of Interests
            if (ar.deviceRegistration) {
                PageUtils.singleClick(driver, deviceReg);
            }
            if (ar.cfsCertificateOfFreeSale) {
                WaitUtils.waitForElementToBeClickable(driver, cfsCertification, TIMEOUT_10_SECOND, false);
                PageUtils.singleClick(driver, cfsCertification);
            }
            if (ar.clinicalInvestigation) {
                WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//span[.='Selected Services']//following::input[3]"), TIMEOUT_10_SECOND, false);
                PageUtils.singleClick(driver, clinicalInvestigation);
            }
            if (ar.aitsAdverseIncidentTrackingSystem) {
                WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//span[.='Selected Services']//following::input[4]"), TIMEOUT_10_SECOND, false);
                PageUtils.singleClick(driver, aitsAdverseIncidient);
            }
        }catch (Exception e){
            //This may come back again, waiting confirmation 18/05/2017
        }

        //Some weired bug where input boxes looses value on focus
        if(exception) {
            try {
                PageUtils.selectFromAutoSuggestedListItems(driver, ".PickerWidget---picker_value", ar.country, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Terms and condition checkbox introduced 17/05
        cbxTermsAndConditions.click();

        //Submit form : remember to verify
        WaitUtils.waitForElementToBeClickable(driver, submit, TIMEOUT_5_SECOND);
        submit.click();

        return new ActionsPage(driver);
    }

    private void enterAddressManually() {
        try {
            PageUtils.singleClick(driver, linkEnterAddressManually);
            WaitUtils.isPageLoadingComplete(driver, TIMEOUT_15_SECOND);
        }catch (Exception e){
            //Introduced suddenly on 17/05 sprint 19 changes
        }
    }


    public ActionsPage clickCancel() {
        PageUtils.doubleClick(driver, cancel);
        return new ActionsPage(driver);
    }
}