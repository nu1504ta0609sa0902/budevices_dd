package com.mhra.mdcm.devices.dd.appian.pageobjects.business.sections;

import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.AccountRequest;
import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import com.mhra.mdcm.devices.dd.appian.pageobjects.business.ActionsPage;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.PageUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by TPD_Auto 
 */

public class CreateTestsData extends _Page {

    //Organisation details
    @FindBy(xpath = ".//label[.='Organisation name']//following::input[1]")
    WebElement orgName;
    @FindBy(xpath = ".//label[contains(text(),'Address line 1')]//following::input[1]")
    WebElement addressLine1;
    @FindBy(xpath = ".//label[contains(text(),'Address line 2')]//following::input[1]")
    WebElement addressLine2;
    @FindBy(xpath = ".//label[contains(text(),'City')]//following::input[1]")
    WebElement townCity;
    @FindBy(xpath = ".//label[contains(text(),'Postcode')]//following::input[1]")
    WebElement postCode;
    //@FindBy(xpath = ".//span[contains(text(),'Country')]//following::select[1]")
    //WebElement country;
    @FindBy(xpath = ".//label[contains(text(),'Country')]//following::input[1]")
    WebElement country;
    @FindBy(xpath = ".//label[contains(text(),'Telephone')]//following::input[1]")
    WebElement telephone;
    @FindBy(xpath = ".//label[contains(text(),'Fax')]//following::input[1]")
    WebElement fax;
    @FindBy(xpath = ".//label[contains(text(),'Website')]//following::input[1]")
    WebElement website;
    @FindBy(xpath = ".//span[contains(text(),'Address type')]//following::input[1]")
    WebElement addressType;

    //Organisation Type
    final String selectedType = "Selected type";
    @FindBy(xpath = ".//span[.='" + selectedType + "']//following::input[1]")
    WebElement limitedCompany;
    @FindBy(xpath = ".//span[.='" + selectedType + "']//following::input[2]")
    WebElement businessPartnership;
    @FindBy(xpath = ".//span[.='" + selectedType + "']//following::input[3]")
    WebElement unincorporatedAssociation;
    @FindBy(xpath = ".//span[.='" + selectedType + "']//following::input[4]")
    WebElement other;
    @FindBy(xpath = ".//span[.='" + selectedType + "']//following::input[5]")
    WebElement vatRegistrationNumber;
    @FindBy(xpath = ".//span[.='" + selectedType + "']//following::input[6]")
    WebElement companyRegistrationNumber;

    //Contact Person Details
    @FindBy(xpath = ".//span[contains(text(),'Title')]//following::select[1]")
    WebElement title;
    @FindBy(xpath = ".//label[.='First name']//following::input[1]")
    WebElement firstName;
    @FindBy(xpath = ".//label[.='Last name']//following::input[1]")
    WebElement lastName;
    @FindBy(xpath = ".//label[contains(text(),'Job title')]//following::input[1]")
    WebElement jobTitle;
    @FindBy(xpath = ".//h3[contains(text(),'Person Details')]//following::input[4]")
    WebElement phoneNumber;
    @FindBy(xpath = ".//label[.='Email']//following::input[1]")
    WebElement emailAddress;

    //Organisational Role
    final String selectedRoles = "Selected roles";
    @FindBy(xpath = ".//span[.='" + selectedRoles + "']//following::input[1]")
    WebElement authorisedRep;
    @FindBy(xpath = ".//span[.='" + selectedRoles + "']//following::input[2]")
    WebElement manufacturer;

    //Services of Interests
    final String selectedServices = "Selected services";
    @FindBy(xpath = ".//span[.='" + selectedServices + "']//following::input[1]")
    WebElement deviceReg;
    @FindBy(xpath = ".//span[.='" + selectedServices + "']//following::input[2]")
    WebElement cfsCertification;
    @FindBy(xpath = ".//span[.='" + selectedServices + "']//following::input[3]")
    WebElement clinicalInvestigation;
    @FindBy(xpath = ".//span[.='" + selectedServices + "']//following::input[4]")
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
        WaitUtils.waitForPageToLoad(driver, By.xpath(".//label[.='Organisation name']//following::input[1]"), 5, false); ;
        WaitUtils.waitForElementToBeClickable(driver, orgName, 5, false);
        orgName.sendKeys(ar.organisationName);

        //Selecting country has changed to auto suggest
        boolean exception = false;
        try {
            orgName.click();
            selectCountryFromAutoSuggests(driver, ".gwt-SuggestBox", ar.country, true);
        }catch (Exception e){
            exception = true;
        }

        //Organisation details
        WaitUtils.waitForElementToBeClickable(driver, addressLine1, TIMEOUT_DEFAULT, false);
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
            PageUtils.clickIfVisible(driver, limitedCompany);
            PageFactory.initElements(driver, this);
            WaitUtils.waitForElementToBeVisible(driver, companyRegistrationNumber, 5, false);
            WaitUtils.nativeWaitInSeconds(1);
            WaitUtils.waitForElementToBeClickable(driver, companyRegistrationNumber, 5, false);
            WaitUtils.nativeWaitInSeconds(1);
            vatRegistrationNumber.sendKeys(ar.vatRegistrationNumber);
            companyRegistrationNumber.sendKeys(ar.companyRegistrationNumber);

        }else if(ar.organisationType.equals("Business Partnership")){
            PageUtils.clickIfVisible(driver, businessPartnership);
            PageFactory.initElements(driver, this);
            WaitUtils.waitForElementToBeVisible(driver, vatRegistrationNumber, 5, false);
            WaitUtils.nativeWaitInSeconds(1);
            WaitUtils.waitForElementToBeClickable(driver, vatRegistrationNumber, 5, false);
            WaitUtils.nativeWaitInSeconds(1);
            vatRegistrationNumber.sendKeys(ar.vatRegistrationNumber);

        }else if(ar.organisationType.equals("Unincorporated Association")){
            PageUtils.clickIfVisible(driver, unincorporatedAssociation);
            PageFactory.initElements(driver, this);

        }else if(ar.organisationType.equals("Other")){
            PageUtils.clickIfVisible(driver, other);
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

        //Some weired bug where input boxes looses value on focus
        if(exception) {
            orgName.click();
            try {
                selectCountryFromAutoSuggests(driver, ".gwt-SuggestBox", ar.country, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Submit form : remember to verify
        submit.click();

        return new ActionsPage(driver);
    }

    private void selectCountryFromAutoSuggests(WebDriver driver, String elementPath, String countryName, boolean throwException) throws Exception {
        boolean completed = true;
        int count = 0;
        do {
            try {

                count++;    //It will go forever without this
                WebElement country = driver.findElements(By.cssSelector(elementPath)).get(0);
                new Actions(driver).moveToElement(country).perform();

                //Enter the country I am interested in
                country.sendKeys("\n");
                country.clear();
                country.sendKeys(countryName, Keys.ENTER);
                new WebDriverWait(driver, 3).until(ExpectedConditions.elementToBeClickable(By.cssSelector(".item")));
                country.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);

                completed = true;
            } catch (Exception e) {
                completed = false;
                WaitUtils.nativeWaitInSeconds(1);
                //PageFactory.initElements(driver, this);
            }
        } while (!completed && count < 1);

        if(!completed && throwException){
            throw new Exception("Country name not selected");
        }
    }

    public ActionsPage clickCancel() {
        PageUtils.doubleClick(driver, cancel);
        return new ActionsPage(driver);
    }
}