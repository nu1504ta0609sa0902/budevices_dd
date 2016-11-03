package com.mhra.mdcm.devices.dd.appian.pageobjects.business.sections;

import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.RandomDataUtils;
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
public class AllOrganisations extends _Page {

    @FindBy(xpath = ".//h2[.='Status']//following::a")
    List<WebElement> listOfAllOrganisations;
    @FindBy(xpath = ".//table//th")
    List<WebElement> listOfTableColumns;

    //Search box
    @FindBy(xpath = ".//*[contains(@class, 'filter')]//following::input[1]")
    WebElement searchBox;

    @Autowired
    public AllOrganisations(WebDriver driver) {
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

        if(expectedHeadings.contains("All Organisations")){
            itemsDisplayed = listOfAllOrganisations.size() > 0;
        }
        return itemsDisplayed;
    }

    public List<String> isTableColumnCorrect(String[] columns) {
        WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//table//th") , 10, false);
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

    public String getRandomOrganisation(boolean exists) {
        String name = RandomDataUtils.getRandomTestName("NonExistingOrg");

        //Search for an existing name
        if(exists){
            WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText("Manufacturer") , TIMEOUT_DEFAULT, false);
            boolean found = false;
            do {
                int randomNumberBetween = RandomDataUtils.getSimpleRandomNumberBetween(0, listOfAllOrganisations.size(), false);
                WebElement element = listOfAllOrganisations.get(randomNumberBetween);
                String orgName = element.getText();
                if(orgName!=null && !orgName.trim().equals("")){
                    name = orgName;
                    found = true;
                }
            }while(!found);

        }
        return name;
    }

    public AllOrganisations searchForOrganisation(String organisationName) {
        WaitUtils.waitForElementToBeClickable(driver, searchBox, 10, false);
        searchBox.clear();
        searchBox.sendKeys(organisationName);
        searchBox.sendKeys(Keys.ENTER);
        return new AllOrganisations(driver);
    }

    /**
     * By default heading contains an anchor, therefore we should do -1
     * @return
     */
    public int getNumberOfMatches() {
        WaitUtils.waitForPageToLoad(driver, By.xpath("WaitForPageToLoad") , TIMEOUT_SMALL, false);
        WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText("Manufacturer") , 10, false);
        int size = listOfAllOrganisations.size();
        size = (size-1) / 2;
        return size;
    }
}
