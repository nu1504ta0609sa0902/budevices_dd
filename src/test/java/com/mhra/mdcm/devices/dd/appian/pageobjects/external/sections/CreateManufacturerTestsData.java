package com.mhra.mdcm.devices.dd.appian.pageobjects.external.sections;

import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.AccountManufacturerRequest;
import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.PageUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.util.List;

/**
 * Created by TPD_Auto
 */
public class CreateManufacturerTestsData extends _Page {

    @FindBy(css = ".component_error")
    List<WebElement> errorMessages;

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
    @FindBy(xpath = ".//label[contains(text(),'Country')]//following::input[1]")
    WebElement country;
    @FindBy(xpath = ".//label[contains(text(),'Telephone')]//following::input[1]")
    WebElement telephone;
    @FindBy(xpath = ".//label[contains(text(),'Fax')]//following::input[1]")
    WebElement fax;
    @FindBy(xpath = ".//label[contains(text(),'Website')]//following::input[1]")
    WebElement website;

    //Contact Person Details
    @FindBy(xpath = ".//span[contains(text(),'Title')]//following::select[1]")
    WebElement title;
    @FindBy(xpath = ".//label[.='First name']//following::input[1]")
    WebElement firstName;
    @FindBy(xpath = ".//label[.='Last name']//following::input[1]")
    WebElement lastName;
    @FindBy(xpath = ".//label[contains(text(),'Job title')]//following::input[1]")
    WebElement jobTitle;
    @FindBy(xpath = ".//h3[contains(text(),'Person Details')]//following::input[5]")
    WebElement phoneNumber;
    @FindBy(xpath = ".//label[.='Email']//following::input[1]")
    WebElement emailAddress;

    //Letter of designation
    @FindBy(css = ".gwt-FileUpload")
    WebElement fileUpload;

    //Submit and cancel
    @FindBy(xpath = ".//button[.='Declare devices']")
    WebElement btnDeclareDevices;
    @FindBy(xpath = ".//button[.='Next']")
    WebElement next;
    @FindBy(xpath = ".//button[.='Cancel']")
    WebElement cancel;


    public CreateManufacturerTestsData(WebDriver driver) {
        super(driver);
    }

    /**
     * HELPS TESTERS CREATE TEST DATA ON THE GO
     * @param ar
     * @return
     */
    public AddDevices createTestOrganisation(AccountManufacturerRequest ar) throws Exception {
        //WaitUtils.waitForElementToBeClickable(driver, By.cssSelector(".gwt-SuggestBox"), TIMEOUT_10_SECOND, false);
        WaitUtils.waitForElementToBeClickable(driver, orgName, TIMEOUT_DEFAULT, false);
        orgName.sendKeys(ar.organisationName);
        selectCountryFromAutoSuggests(driver, ".gwt-SuggestBox", ar.country, false);

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

        //Contact Person Details
        PageUtils.selectByText(title, ar.title);
        firstName.sendKeys(ar.firstName);
        lastName.sendKeys(ar.lastName);
        jobTitle.sendKeys(ar.jobTitle);
        phoneNumber.sendKeys(ar.phoneNumber);
        emailAddress.sendKeys(ar.email);

        //Upload letter of designation
        String fileName = "DesignationLetter1.pdf";
        if(!ar.isManufacturer){
            fileName = "DesignationLetter2.pdf";
        }
        PageUtils.uploadDocument(fileUpload, fileName, 1, 3);

        //Submit form : remember to verify
        try{
            btnDeclareDevices.click();
        }catch (Exception e){
            next.click();
        }

        return new AddDevices(driver);
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



    public boolean isErrorMessageDisplayed() {
        try {
            WaitUtils.waitForElementToBeVisible(driver, By.cssSelector(".component_error"), 3, false);
            boolean isDisplayed = errorMessages.size() > 0;
            return isDisplayed;
        }catch (Exception e){
            return false;
        }
    }
}
