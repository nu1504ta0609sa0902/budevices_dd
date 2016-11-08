package com.mhra.mdcm.devices.dd.appian.pageobjects.business.sections;

import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.AccountRequest;
import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import com.mhra.mdcm.devices.dd.appian.pageobjects.business.ActionsPage;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.PageUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by TPD_Auto 
 */
@Component
public class CreateTestsData extends _Page {

    //Organisation details
    @FindBy(xpath = ".//label[.='Organisation Name']//following::input[1]")
    WebElement orgName;
    @FindBy(xpath = ".//label[.='Address line 1']//following::input[1]")
    WebElement addressLine1;
    @FindBy(xpath = ".//label[.='Address line 2']//following::input[1]")
    WebElement addressLine2;
    @FindBy(xpath = ".//label[.='Town/City']//following::input[1]")
    WebElement townCity;
    @FindBy(xpath = ".//label[.='Postcode']//following::input[1]")
    WebElement postCode;
    @FindBy(xpath = ".//span[.='Country']//following::select[1]")
    WebElement country;
    @FindBy(xpath = ".//label[.='Telephone']//following::input[1]")
    WebElement telephone;
    @FindBy(xpath = ".//label[.='Fax']//following::input[1]")
    WebElement fax;
    @FindBy(xpath = ".//label[.='Website']//following::input[1]")
    WebElement website;
    @FindBy(xpath = ".//span[.='Address type']//following::input[1]")
    WebElement addressType;

    //Organisation Type
    @FindBy(xpath = ".//span[.='Selected Type']//following::input[1]")
    WebElement limitedCompany;
    @FindBy(xpath = ".//span[.='Selected Type']//following::input[2]")
    WebElement businessPartnership;
    @FindBy(xpath = ".//span[.='Selected Type']//following::input[3]")
    WebElement unincorporatedAssociation;
    @FindBy(xpath = ".//span[.='Selected Type']//following::input[4]")
    WebElement other;
    @FindBy(xpath = ".//span[.='Selected Type']//following::input[5]")
    WebElement vatRegistrationNumber;
    @FindBy(xpath = ".//span[.='Selected Type']//following::input[6]")
    WebElement companyRegistrationNumber;

    //Contact Person Details
    @FindBy(xpath = ".//span[.='Title']//following::select[1]")
    WebElement title;
    @FindBy(xpath = ".//label[.='First Name']//following::input[1]")
    WebElement firstName;
    @FindBy(xpath = ".//label[.='Last Name']//following::input[1]")
    WebElement lastName;
    @FindBy(xpath = ".//label[.='Job Title']//following::input[1]")
    WebElement jobTitle;
    @FindBy(xpath = ".//label[.='Phone Number']//following::input[1]")
    WebElement phoneNumber;
    @FindBy(xpath = ".//label[.='Email Address']//following::input[1]")
    WebElement emailAddress;

    //Organisational Role
    @FindBy(xpath = ".//span[.='Selected Roles']//following::input[1]")
    WebElement authorisedRep;
    @FindBy(xpath = ".//span[.='Selected Roles']//following::input[2]")
    WebElement manufacturer;

    //Services of Interests
    @FindBy(xpath = ".//span[.='Selected Services']//following::input[1]")
    WebElement deviceReg;
    @FindBy(xpath = ".//span[.='Selected Services']//following::input[2]")
    WebElement cfsCertification;
    @FindBy(xpath = ".//span[.='Selected Services']//following::input[3]")
    WebElement clinicalInvestigation;
    @FindBy(xpath = ".//span[.='Selected Services']//following::input[4]")
    WebElement aitsAdverseIncidient;

    //Submit and cancel
    @FindBy(xpath = ".//button[.='Submit']")
    WebElement submit;
    @FindBy(xpath = ".//button[.='Cancel']")
    WebElement cancel;



    public CreateTestsData(WebDriver driver) {
        super(driver);
    }

    /**
     * HELPS TESTERS CREATE TEST DATA ON THE GO
     * @param ar
     * @return
     */
    public ActionsPage createTestOrganisation(AccountRequest ar) {
        WaitUtils.waitForElementToBeClickable(driver, orgName, TIMEOUT_DEFAULT, false);
        orgName.sendKeys(ar.organisationName);

        //Organisation details
        WaitUtils.waitForElementToBeClickable(driver, addressLine1, TIMEOUT_DEFAULT, false);
        addressLine1.clear();
        addressLine1.sendKeys(ar.address1);
        addressLine2.sendKeys(ar.address2);
        townCity.sendKeys(ar.townCity);
        postCode.sendKeys(ar.postCode);
        //country.sendKeys(ar.country);
        PageUtils.selectByText(country, ar.country);
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
            WaitUtils.waitForElementToBeClickable(driver, companyRegistrationNumber, TIMEOUT_DEFAULT, false);
            vatRegistrationNumber.sendKeys(ar.vatRegistrationNumber);
            companyRegistrationNumber.sendKeys(ar.companyRegistrationNumber);
        }else if(ar.organisationType.equals("Business Partnership")){
            PageUtils.doubleClick(driver, businessPartnership);
            PageFactory.initElements(driver, this);
            WaitUtils.waitForElementToBeClickable(driver, vatRegistrationNumber, TIMEOUT_DEFAULT, false);
            vatRegistrationNumber.sendKeys(ar.vatRegistrationNumber);
        }else if(ar.organisationType.equals("Unincorporated Association")){
            PageUtils.doubleClick(driver, unincorporatedAssociation);
            PageFactory.initElements(driver, this);
        }else if(ar.organisationType.equals("Other")){
            PageUtils.doubleClick(driver, other);
            PageFactory.initElements(driver, this);
        }

        //Contact Person Details
        PageUtils.selectByText(title, ar.title);
        firstName.sendKeys(ar.firstName);
        lastName.sendKeys(ar.lastName);
        jobTitle.sendKeys(ar.jobTitle);
        phoneNumber.sendKeys(ar.phoneNumber);
        emailAddress.sendKeys(ar.email);

        //Organisation Role
        if(ar.isManufacturer){
            PageUtils.doubleClick(driver, manufacturer);
        }else{
            PageUtils.doubleClick(driver, authorisedRep);
        }

        //Services of Interests
        if(ar.deviceRegistration){
            PageUtils.singleClick(driver, deviceReg);
        }
        if(ar.cfsCertificateOfFreeSale){
            WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//span[.='Selected Services']//following::input[2]"), TIMEOUT_DEFAULT, false);
            PageUtils.singleClick(driver, cfsCertification);
        }
        if(ar.clinicalInvestigation){
            WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//span[.='Selected Services']//following::input[3]"), TIMEOUT_DEFAULT, false);
            PageUtils.singleClick(driver, clinicalInvestigation);
        }
        if(ar.aitsAdverseIncidentTrackingSystem){
            WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//span[.='Selected Services']//following::input[4]"), TIMEOUT_DEFAULT, false);
            PageUtils.singleClick(driver, aitsAdverseIncidient);
        }

        //Submit form : remember to verify
        submit.click();

        return new ActionsPage(driver);
    }
}
