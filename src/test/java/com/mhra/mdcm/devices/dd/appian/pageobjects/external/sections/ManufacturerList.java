package com.mhra.mdcm.devices.dd.appian.pageobjects.external.sections;

import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
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

    @FindBy(css = "button.GFWJSJ4DCF")
    WebElement linkRegisterNewManufacturer;
    @FindBy(css = ".SafeImage.GFWJSJ4DOFB")
    WebElement linkManufacturerRegistration;
    @FindBy(xpath = ".//button[.='Register My Organisation']")
    WebElement linkRegisterMyNewOrganisation;

    @FindBy(css = "td>div>a")
    List<WebElement> listOfManufacturerNames;
    @FindBy(xpath = ".//*[contains(text(), 'registration status')]//following::tr[@__gwt_subrow='0']")
    List<WebElement> listOfTableRows;
    @FindBy(xpath = ".//*[contains(text(), 'registration status')]")
    WebElement manufacturerRegistrationStatus;


    @FindBy(css = ".GFWJSJ4DFDC div")
    WebElement itemCount;
    @FindBy(css = ".gwt-Image[aria-label='Next page']")
    WebElement nextPage;
    @FindBy(css = ".gwt-Image[aria-label='Previous page']")
    WebElement prevPage;
    @FindBy(css = ".gwt-Image[aria-label='Last page']")
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
            WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText(manufacturerName), TIMEOUT_HIGH, false);
            WebElement man = driver.findElement(By.partialLinkText(manufacturerName));
            man.click();
        }
        return new ManufacturerDetails(driver);
    }

    public String getARandomManufacturerName() {
        WaitUtils.waitForElementToBeClickable(driver, By.cssSelector(".left>div>a"), TIMEOUT_VERY_HIGH, false);
        WaitUtils.waitForElementToBeVisible(driver, By.cssSelector(".left>div>a"), TIMEOUT_MEDIUM, false);
        int index = RandomDataUtils.getNumberBetween(0, listOfManufacturerNames.size() - 1);
        WebElement link = listOfManufacturerNames.get(index);
        String name = link.getText();
        return name;
    }

    public boolean isManufacturerDisplayedInList(String manufacturerName){
        WaitUtils.nativeWaitInSeconds(2);
        WaitUtils.waitForElementToBeClickable(driver, By.cssSelector("td>div>a"), TIMEOUT_MEDIUM, false);
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
        WaitUtils.waitForElementToBeClickable(driver, nextPage, TIMEOUT_MEDIUM, false);
        nextPage.click();
        return new ManufacturerList(driver);
    }

    public ManufacturerList clickPrev(){
        WaitUtils.waitForElementToBeClickable(driver, prevPage, TIMEOUT_MEDIUM, false);
        prevPage.click();
        return new ManufacturerList(driver);
    }

    public ManufacturerList clickLastPage(){
        WaitUtils.waitForElementToBeClickable(driver, lastPage, TIMEOUT_MEDIUM, false);
        lastPage.click();
        return new ManufacturerList(driver);
    }


//    public CreateManufacturerTestsData registerNewManufacturer() {
//        WaitUtils.waitForElementToBeClickable(driver, linkRegisterNewManufacturer, TIMEOUT_DEFAULT, false);
//        linkRegisterNewManufacturer.click();
//        return new CreateManufacturerTestsData(driver);
//    }

    public int getNumberOfPages() {
        WaitUtils.waitForElementToBeVisible(driver, itemCount, TIMEOUT_MEDIUM, false);
        WaitUtils.waitForElementToBeClickable(driver, itemCount, TIMEOUT_MEDIUM, false);
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
        WaitUtils.waitForElementToBeClickable(driver, manufacturerRegistrationStatus, TIMEOUT_DEFAULT, false);
        if(sortBy.equals("Registration Status")){
            for(int c = 0; c < numberOfTimesToClick; c++) {
                manufacturerRegistrationStatus.click();
                WaitUtils.waitForElementToBeClickable(driver, manufacturerRegistrationStatus, TIMEOUT_DEFAULT, false);
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
            WaitUtils.waitForElementToBeClickable(driver, By.cssSelector(".left>div>a"), TIMEOUT_VERY_HIGH, false);
            WaitUtils.waitForElementToBeVisible(driver, By.cssSelector(".left>div>a"), TIMEOUT_MEDIUM, false);
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


    public CreateManufacturerTestsData registerNewManufacturer() {
        WaitUtils.waitForElementToBeClickable(driver, linkRegisterNewManufacturer, TIMEOUT_DEFAULT, false);
        linkRegisterNewManufacturer.click();
        return new CreateManufacturerTestsData(driver);
    }

    public CreateManufacturerTestsData registerMyOrganisation() {
        WaitUtils.waitForElementToBeClickable(driver, linkRegisterMyNewOrganisation, TIMEOUT_DEFAULT, false);
        linkRegisterMyNewOrganisation.click();
        return new CreateManufacturerTestsData(driver);
    }


    public ManufacturerList gotoListOfManufacturerPage() {
        WaitUtils.waitForElementToBeClickable(driver, linkManufacturerRegistration, TIMEOUT_DEFAULT, false);
        linkManufacturerRegistration.click();
        return new ManufacturerList(driver);
    }
}
