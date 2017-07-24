package com.mhra.mdcm.devices.dd.appian.pageobjects.external.sections;

import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import com.mhra.mdcm.devices.dd.appian.pageobjects.external._CreateManufacturerTestsData;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.RandomDataUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.openqa.selenium.By;
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


    public ManufacturerDetails viewAManufacturer(String manufacturerName) {
        WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
        if(manufacturerName == null){
            //Than view a random one
            int index = RandomDataUtils.getNumberBetween(0, listOfManufacturerNames.size() - 1);
            WebElement link = listOfManufacturerNames.get(index);
            link.click();
        }else{
            WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText(manufacturerName), TIMEOUT_30_SECOND, false);
            WebElement man = driver.findElement(By.partialLinkText(manufacturerName));
            man.click();
        }
        return new ManufacturerDetails(driver);
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

    public boolean isManufacturerDisplayedInList(String manufacturerName){
        WaitUtils.nativeWaitInSeconds(2);
        WaitUtils.waitForElementToBeClickable(driver, By.cssSelector("td>div>a"), TIMEOUT_15_SECOND, false);
        boolean found = false;
        for(WebElement item: listOfManufacturerNames){
            String name = item.getText();
            if (name.contains(manufacturerName)) {
                found = true;
                break;
            }
        }
        return found;
    }


    public ManufacturerList clickNext(){
        WaitUtils.waitForElementToBeClickable(driver, nextPage, TIMEOUT_15_SECOND, false);
        nextPage.click();
        return new ManufacturerList(driver);
    }

    public ManufacturerList clickPrev(){
        WaitUtils.waitForElementToBeClickable(driver, prevPage, TIMEOUT_15_SECOND, false);
        prevPage.click();
        return new ManufacturerList(driver);
    }

    public ManufacturerList clickLastPage(){
        WaitUtils.waitForElementToBeClickable(driver, lastPage, TIMEOUT_15_SECOND, false);
        lastPage.click();
        return new ManufacturerList(driver);
    }


//    public CreateManufacturerTestsData registerNewManufacturer() {
//        WaitUtils.waitForElementToBeClickable(driver, btnRegisterNewManufacturer, TIMEOUT_10_SECOND, false);
//        btnRegisterNewManufacturer.click();
//        return new CreateManufacturerTestsData(driver);
//    }

    public int getNumberOfPages() {
        WaitUtils.waitForElementToBeVisible(driver, itemCount, TIMEOUT_15_SECOND, false);
        WaitUtils.waitForElementToBeClickable(driver, itemCount, TIMEOUT_15_SECOND, false);
        try {
            String text = itemCount.getText();
            String total = text.substring(text.indexOf("of") + 3);
            String itemPerPage = text.substring(text.indexOf("-") + 1, text.indexOf(" of "));

            int tt = Integer.parseInt(total.trim());
            int noi = Integer.parseInt(itemPerPage.trim());

            int reminder = tt % noi;
            int numberOfPage = (tt/noi) - 1;
            if(reminder > 0){
                numberOfPage++;
            }

            return numberOfPage;
        }catch (Exception e){
            return 0;
        }
    }

    public String getRegistrationStatus(String name) {
        String registered = "";
        for(WebElement tr: listOfTableRows){
            try {
                WebElement link = tr.findElement(By.partialLinkText(name));
                registered = tr.findElement(By.xpath("td[4]")).getText();
            }catch (Exception ex){}
        }
        return registered;
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

    public String getOrganisationCountry(String name) {
        String country = "";
        for(WebElement tr: listOfTableRows){
            try {
                WebElement link = tr.findElement(By.partialLinkText(name));
                country = tr.findElement(By.xpath("td[3]")).getText();
            }catch (Exception ex){}
        }
        return country;
    }


    public String getARandomManufacturerNameWithStatus(String status) {
        String name = null;
        boolean found = false;
        int attempts = 0;
        do {
            WaitUtils.waitForElementToBeClickable(driver, By.cssSelector(".left>div>a"), TIMEOUT_60_SECOND, false);
            WaitUtils.waitForElementToBeVisible(driver, By.cssSelector(".left>div>a"), TIMEOUT_15_SECOND, false);
            WebElement link = listOfManufacturerNames.get(attempts);
            name = link.getText();
            String registered = getRegistrationStatus(name);
            if(registered.toLowerCase().equals(status.toLowerCase())){
                found = true;
            }
            attempts++;
        }while(attempts < listOfManufacturerNames.size() && !found);

        return name;
    }


    public _CreateManufacturerTestsData registerNewManufacturer() {
        WaitUtils.waitForElementToBeClickable(driver, btnAddNewManufacturer, TIMEOUT_15_SECOND, false);
        btnAddNewManufacturer.click();
        return new _CreateManufacturerTestsData(driver);
    }
}
