package com.mhra.mdcm.devices.dd.appian.pageobjects.external.sections;

import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import com.mhra.mdcm.devices.dd.appian.pageobjects.external._CreateManufacturerTestsData;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.RandomDataUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Created by TPD_Auto
 */
public class ManufacturerList extends _Page {

    //Register manufacturer button
    @FindBy(xpath = ".//button[contains(text(), 'Register new manufacturer')]")
    WebElement btnRegisterNewManufacturer;
    @FindBy(xpath = ".//button[contains(text(), 'Add new manufacturer')]")
    WebElement btnAddNewManufacturer;

    //List of table rows
    @FindBy(xpath = ".//h2[contains(text(),'Manufacturer')]//following::tbody[1]/tr/td[1]")
    List<WebElement> listOfManufacturerNames;
    @FindBy(xpath = ".//*[contains(text(), 'Registration status')]//following::tr[@__gwt_subrow='0']")
    List<WebElement> listOfTableRows;
    @FindBy(xpath = ".//*[contains(text(), 'Registration status')]")
    WebElement manufacturerRegistrationStatus;

    //Individual elements
    @FindBy(xpath = ".//h2[contains(text(),'Manufacturer')]//following::tbody[1]/tr/td[1]")
    WebElement aManufacturerName;


    @FindBy(css = ".GridWidget---count")
    WebElement itemCount;
    @FindBy(css = ".GridWidget---count")
    List<WebElement> itemCounts;
    @FindBy(css = "[aria-label='Next page']")
    WebElement nextPage;
    @FindBy(css = "[aria-label='Previous page']")
    WebElement prevPage;
    @FindBy(css = "[aria-label='Last page']")
    WebElement lastPage;

    public ManufacturerList(WebDriver driver) {
        super(driver);
    }

    public String getARandomManufacturerName() {
        //WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
        WaitUtils.waitForElementToBeVisible(driver, aManufacturerName, TIMEOUT_10_SECOND, false);
        WaitUtils.waitForElementToBeClickable(driver, aManufacturerName, TIMEOUT_5_SECOND, false);
        int index = RandomDataUtils.getNumberBetween(0, listOfManufacturerNames.size() - 1);
        WebElement link = listOfManufacturerNames.get(index);
        String name = link.getText();
        return name;
    }


    public ManufacturerList sortBy(String sortBy, int numberOfTimesToClick) {
        WaitUtils.waitForElementToBeClickable(driver, manufacturerRegistrationStatus, TIMEOUT_10_SECOND, false);
        if(sortBy.equals("Registration Status")){
            for(int c = 0; c < numberOfTimesToClick; c++) {
                manufacturerRegistrationStatus.click();
                WaitUtils.waitForElementToBeClickable(driver, manufacturerRegistrationStatus, TIMEOUT_10_SECOND, false);
            }
        }

        return new ManufacturerList(driver);
    }


    public _CreateManufacturerTestsData registerNewManufacturer() {
        WaitUtils.waitForElementToBeClickable(driver, btnAddNewManufacturer, TIMEOUT_15_SECOND, false);
        btnAddNewManufacturer.click();
        return new _CreateManufacturerTestsData(driver);
    }

}
