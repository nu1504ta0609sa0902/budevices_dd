package com.mhra.mdcm.devices.dd.appian.pageobjects.business.sections;

import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.RandomDataUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.AssertUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.PageUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TPD_Auto
 */
@Component
public class Accounts extends _Page {

    @FindBy(xpath = ".//h2[.='Status']//following::a")
    List<WebElement> listOfAccounts;
    @FindBy(xpath = ".//table//th")
    List<WebElement> listOfTableColumns;

    //Edit information related to an account
    @FindBy(linkText = "Edit Account Information")
    WebElement editAccountInfoLink;
    @FindBy(xpath = ".//span[.='Address type']//following::input[1]")
    WebElement addressType;
    @FindBy(xpath = ".//label[.='Job Title']//following::input[1]")
    WebElement jobTitle;
    @FindBy(xpath = ".//button[.='Submit']")
    WebElement submitBtn;

    //Updated information related to an account
    @FindBy(xpath = ".//span[.='Job title']//following::p[1]")
    WebElement jobTitleTxt;


    //Search box
    @FindBy(xpath = ".//*[contains(@class, 'filter')]//following::input[1]")
    WebElement searchBox;

    @Autowired
    public Accounts(WebDriver driver) {
        super(driver);
    }


    public boolean isHeadingCorrect(String expectedHeadings) {
        WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//h2[.='" + expectedHeadings + "']") , 10, false);
        WebElement heading = driver.findElement(By.xpath(".//h2[.='" + expectedHeadings + "']"));
        boolean contains = heading.getText().contains(expectedHeadings);
        return contains;
    }


    public boolean isItemsDisplayed(String expectedHeadings) {
        boolean itemsDisplayed = false;
        WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//h2[.='" + expectedHeadings + "']") , 10, false);

        if(expectedHeadings.contains("Accounts")){
            itemsDisplayed = listOfAccounts.size() > 0;
        }

        return itemsDisplayed;
    }

    public List<String> isTableColumnCorrect(String[] columns) {
        WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//table//th") , TIMEOUT_DEFAULT, false);
        List<String> listOfColumns = new ArrayList<>();
        for(WebElement el: listOfTableColumns){
            String text = el.getText();
            if(text!=null){
                listOfColumns.add(text);
            }
        }

        //Verify columns matches expectation
        List<String> columnsNotFound = new ArrayList<>();
        for(String c: columns){
            c = c.trim();
            if(!listOfColumns.contains(c)){
                System.out.println("Column Not Found : " + c);
                columnsNotFound.add(c);
            }
        }

        return columnsNotFound;
    }

    public Accounts searchForAccount(String orgName) {
        WaitUtils.waitForElementToBeClickable(driver, searchBox, 10, false);
        searchBox.clear();
        searchBox.sendKeys(orgName);
        searchBox.sendKeys(Keys.ENTER);
        return new Accounts(driver);
    }

    public boolean numberOfMatchesShouldBe(int minCount) {
        try{
            WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//h2[.='Status']//following::a[2]"), TIMEOUT_SMALL, false);
            int actualCount = (listOfAccounts.size()-1)/2;
            return actualCount >= minCount;
        }catch (Exception e){
            return minCount == 0;
        }
    }

    /**
     * NOTE THERE MAY BE MORE THAN 1 LINK PER ROW
     * @return
     */
    public String getARandomAccount() {
        WaitUtils.isPageLoaded(driver, By.xpath(".//h2[.='Status']//following::a[2]"), TIMEOUT_SMALL, 2);
        WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//h2[.='Status']//following::a[2]"), TIMEOUT_SMALL, false);

        int position = RandomDataUtils.getSimpleRandomNumberBetween(1, listOfAccounts.size() - 1, false);
        WebElement accountLinks = listOfAccounts.get(position);
        String accountName = accountLinks.getText();
        return accountName;
    }

    public Accounts viewSpecifiedAccount(String randomAccountName) {
        WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText(randomAccountName), TIMEOUT_DEFAULT, false);
        WebElement accountLinks = driver.findElement(By.partialLinkText(randomAccountName));
        //accountLinks.click();
        PageUtils.doubleClick(driver, accountLinks);
        return new Accounts(driver);
    }

    public Accounts gotoEditAccountInformation() {
        WaitUtils.waitForElementToBeClickable(driver, editAccountInfoLink, TIMEOUT_DEFAULT, false);
        editAccountInfoLink.click();
        return new Accounts(driver);
    }

    public Accounts editAccountInformation(String keyValuePairToUpdate) {
        String[] dataPairs = keyValuePairToUpdate.split(",");

        for(String pairs: dataPairs){
            String[] split = pairs.split("=");
            String key = split[0];
            String value = split[1];
            if(key.equals("job.title")){
                WaitUtils.waitForElementToBeClickable(driver, jobTitle, TIMEOUT_DEFAULT, false);
                jobTitle.clear();
                jobTitle.sendKeys(RandomDataUtils.generateTestNameStartingWith(value, 5));
            }
        }

        //Submit data, but you must select address types
        addressType.click();
        submitBtn.click();

        return new Accounts(driver);
    }

    public boolean verifyUpdatesDisplayedOnPage(String keyValuePairToUpdate) {
        WaitUtils.waitForElementToBeVisible(driver, jobTitleTxt, TIMEOUT_SMALL, false);
        boolean allChangesDisplayed = true;

        //Check for the following
        String[] dataPairs = keyValuePairToUpdate.split(",");

        for(String pairs: dataPairs){
            String[] split = pairs.split("=");
            String key = split[0];
            String value = split[1];
            if(key.equals("job.title")){
                allChangesDisplayed = AssertUtils.areChangesDisplayed(jobTitleTxt,  value);
            }
        }

        return allChangesDisplayed;
    }

    public boolean isOrderedAtoZ() {
        int getFirstX = 20;
        List<String> listOfOrderedOrganisations = new ArrayList<>();
        WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//h2[.='Status']//following::a[2]"), TIMEOUT_SMALL, false);

        //Get list of organisation names
        int position = 0;   //Only even ones are organisation name
        int elementCount = 0;
        for(WebElement el: listOfAccounts){

            //At the moment only the even ones are organisation names
            if(position % 2 == 0){
                String orgName = el.getText();
                listOfOrderedOrganisations.add(orgName);
            }

            if(elementCount == getFirstX){
                break;
            }

            elementCount++;
            position++;
        }

        return false;
    }
}
