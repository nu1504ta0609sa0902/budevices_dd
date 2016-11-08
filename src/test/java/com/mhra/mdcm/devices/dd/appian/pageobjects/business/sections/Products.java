package com.mhra.mdcm.devices.dd.appian.pageobjects.business.sections;

import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by TPD_Auto 
 */
@Component
public class Products extends _Page {

    @FindBy(xpath = ".//h2[.='model']//following::a")
    List<WebElement> listOfProducts;

    @FindBy(xpath = ".//h2[.='Country']//following::a")
    List<WebElement> listOfAllProducts;


    public Products(WebDriver driver) {
        super(driver);
    }

    public boolean isHeadingCorrect(String expectedHeadings) {
        WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//h2[.='" + expectedHeadings + "']") , 10, false);
        WebElement heading = driver.findElement(By.xpath(".//h2[.='" + expectedHeadings + "']"));
        boolean contains = heading.getText().equals(expectedHeadings);
        return contains;
    }

    public boolean isItemsDisplayed(String expectedHeadings) {
        boolean itemsDisplayed = false;
        WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//h2[.='" + expectedHeadings + "']") , 10, false);

        if(expectedHeadings.equals("Products")){
            itemsDisplayed = listOfProducts.size() > 0;
        }else if(expectedHeadings.equals("All Products")){
            itemsDisplayed = listOfAllProducts.size() > 0;
        }

        return itemsDisplayed;
    }

}
