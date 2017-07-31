package com.mhra.mdcm.devices.dd.appian.pageobjects.external;

import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.ManufacturerOrganisationRequest;
import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import com.mhra.mdcm.devices.dd.appian.pageobjects.external.sections.AddDevices;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.PageUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Created by TPD_Auto
 */
public class _CreateManufacturerTestsData extends _Page {

    @FindBy(css = ".component_error")
    List<WebElement> errorMessages;

    //Organisation details
    @FindBy(xpath = ".//label[.='Name']//following::input[1]")
    WebElement orgName;
    @FindBy(xpath = ".//*[contains(text(),'Address Details')]//following::input[1]")
    WebElement addressLine1;
    @FindBy(xpath = ".//*[contains(text(),'Address Details')]//following::input[1]")
    WebElement addressLine2;
    @FindBy(xpath = ".//label[contains(text(),'City')]//following::input[1]")
    WebElement townCity;
    @FindBy(xpath = ".//label[contains(text(),'Post')]//following::input[1]")
    WebElement postCode;
    @FindBy(xpath = ".//label[contains(text(),'Post')]//following::input[@type='text'][2]")
    WebElement telephone;
    @FindBy(xpath = ".//label[contains(text(),'Fax')]//following::input[1]")
    WebElement fax;
    @FindBy(xpath = ".//label[contains(text(),'Website')]//following::input[1]")
    WebElement website;

    //Contact Person Details
    @FindBy(xpath = ".//span[contains(text(),'Title')]//following::div[@role='listbox']")
    WebElement title;
    @FindBy(xpath = ".//*[contains(text(),'First ')]//following::input[1]")
    WebElement firstName;
    @FindBy(xpath = ".//label[contains(text(),'Last ')]//following::input[1]")
    WebElement lastName;
    @FindBy(xpath = ".//label[contains(text(),'Job ')]//following::input[1]")
    WebElement jobTitle;
    @FindBy(xpath = ".//label[.='Email']//following::input[1]")
    WebElement emailAddress;
    @FindBy(xpath = ".//label[contains(text(),'Job ')]//following::input[2]")
    WebElement phoneNumber;

    //Letter of designation
    @FindBy(css = "input.FileUploadWidget---ui-inaccessible")
    WebElement fileUpload;

    //Submit and cancel
    @FindBy(xpath = ".//button[contains(text(),'Save Registration')]")
    WebElement btnSaveRegistration;
    @FindBy(xpath = ".//button[contains(text(),'Continue')]")
    WebElement btnDeclareDevices;
    @FindBy(xpath = ".//button[.='Next']")
    WebElement next;
    @FindBy(xpath = ".//button[.='Cancel']")
    WebElement cancel;


    public _CreateManufacturerTestsData(WebDriver driver) {
        super(driver);
    }

    /**
     * HELPS TESTERS CREATE TEST DATA ON THE GO
     * @param ar
     * @param saveDontDeclareDevices
     * @return
     */
    public AddDevices createTestOrganisation(ManufacturerOrganisationRequest ar, boolean saveDontDeclareDevices) throws Exception {
        //WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
        //WaitUtils.nativeWaitInSeconds(3);
        //WaitUtils.waitForElementToBeClickable(driver, By.cssSelector(".PickerWidget---picker_value"), TIMEOUT_5_SECOND, false);
        WaitUtils.waitForElementToBeClickable(driver, orgName, TIMEOUT_15_SECOND, false);
        orgName.sendKeys(ar.organisationName);
        //PageUtils.selectCountryFromAutoSuggests(driver, ".gwt-SuggestBox", ar.country, false);
        boolean exception = false;
        try {
            PageUtils.selectFromAutoSuggestedListItemsManufacturers(driver, ".PickerWidget---picker_value", ar.country, true);
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

        //Contact Person Details
        //PageUtils.selectByText(title, ar.title);
        try {
            PageUtils.singleClick(driver, title);
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

        if(exception){
            PageUtils.selectFromAutoSuggestedListItemsManufacturers(driver, ".PickerWidget---picker_value", ar.country, true);
        }

        //Upload letter of designation
        String fileName = "DesignationLetter1.pdf";
        if(!ar.isManufacturer){
            fileName = "DesignationLetter2.pdf";
        }
        PageUtils.uploadDocument(fileUpload, fileName, 1, 3);

        //Submit form : remember to verify
        try{
            if(saveDontDeclareDevices){
                btnSaveRegistration.click();
            }else {
                btnDeclareDevices.click();
            }
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
                //new WebDriverWait(driver, 3).until(ExpectedConditions.elementToBeClickable(By.cssSelector(".item")));
                WaitUtils.waitForElementToBeClickable(driver, By.cssSelector(".item"), TIMEOUT_5_SECOND, false);
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

    public ExternalHomePage submitForApproval() {
        WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//button[.='Submit']"), TIMEOUT_30_SECOND, false);
        driver.findElement(By.xpath(".//button[.='Submit']")).click();
        return new ExternalHomePage(driver);
    }
}
