package com.mhra.mdcm.devices.dd.appian.pageobjects.external.sections;

import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Created by TPD_Auto
 */
public class EditManufacturer extends _Page {


    //ORGANISATION DETAILS
    @FindBy(xpath = ".//label[.='Organisation name']//following::input[1]")
    WebElement orgName;
    @FindBy(xpath = ".//label[.='Address line 1']//following::input[1]")
    WebElement orgAddressLine1;
    @FindBy(xpath = ".//label[contains(text(),'Address line 2')]//following::input[1]")
    WebElement orgAddressLine2;
    @FindBy(xpath = ".//label[contains(text(),'City')]//following::input[1]")
    WebElement orgCityTown;
    @FindBy(xpath = ".//label[.='Postcode']//following::input[1]")
    WebElement orgPostCode;
    @FindBy(css = ".GFWJSJ4DEY.GFWJSJ4DIY>div")
    WebElement orgCountry;
    @FindBy(xpath = ".//label[contains(text(),'Telephone')]//following::input[1]")
    WebElement orgTelephone;
    @FindBy(xpath = ".//label[contains(text(),'Website')]//following::input[1]")
    WebElement webSite;

    @FindBy(xpath = ".//span[contains(text(),'Address type')]//following::p[1]")
    WebElement addressType;

    @FindBy(css = ".component_error")
    List <WebElement> errorMessages;

    @FindBy(xpath = ".//button[.='Yes']")
    WebElement confirmYes;
    @FindBy(xpath = ".//button[.='No']")
    WebElement confirmNo;

    @FindBy(xpath = ".//button[contains(text(),'Save')]")
    List<WebElement> saveYes;
    @FindBy(xpath = ".//button[contains(text(),'Cancel')]")
    WebElement saveNo;

    //Submit or cancel button
    @FindBy(css = "button.GFWJSJ4DCF")
    WebElement submitBtn;
    @FindBy(css = ".GFWJSJ4DFXC.left button.GFWJSJ4DNE")
    WebElement cancelBtn;

    //Contact details
    @FindBy(xpath = ".//span[contains(text(),'Title')]//following::select[1]")
    WebElement title;
    @FindBy(xpath = ".//label[.='First name']//following::input[1]")
    WebElement firstName;
    @FindBy(xpath = ".//label[.='Last name']//following::input[1]")
    WebElement lastName;
    @FindBy(xpath = ".//label[contains(text(),'Job title')]//following::input[1]")
    WebElement jobTitle;
    @FindBy(xpath = ".//label[.='Email']//following::input[1]")
    WebElement email;
    @FindBy(xpath = ".//label[.='Email']//following::input[2]")
    WebElement telephone;

    public EditManufacturer(WebDriver driver) {
        super(driver);
    }



}
