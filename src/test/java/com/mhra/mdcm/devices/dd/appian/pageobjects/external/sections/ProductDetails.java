package com.mhra.mdcm.devices.dd.appian.pageobjects.external.sections;

import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.DeviceData;
import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.AssertUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Created by TPD_Auto
 */
public class ProductDetails extends _Page {

    //General Medical Device Data
    @FindBy(xpath = ".//span[.='GMDN code']//following::p[1]")
    WebElement gmdnCode;
    @FindBy(xpath = ".//span[.='GMDN term']//following::p[1]")
    WebElement gmdnTermDefinition;
    @FindBy(xpath = ".//span[.='Risk classification']//following::p[1]")
    WebElement riskClassification;
    @FindBy(xpath = ".//span[.='Custom made']//following::p[1]")
    WebElement customMade;
    @FindBy(xpath = ".//span[contains(text(),'Sterile')]//following::p[1]")
    WebElement sterile;
    @FindBy(xpath = ".//span[contains(text(),'Measuring')]//following::p[1]")
    WebElement measuring;
    
    public ProductDetails(WebDriver driver) {
        super(driver);
    }

    public boolean isProductOrDeviceDetailValid(DeviceData deviceData) {
        boolean allValid = true;
        String deviceType = deviceData.deviceType;
        if(deviceType.equals("General Medical Device")){
            allValid = isGeneralMedicalDeviceValid(deviceData);
        }else if(deviceType.equals("In Vitro Diagnostic Device")){
            //allValid = isGeneralMedicalDeviceValid(deviceData);
        }else if(deviceType.equals("System or Procedure Pack")){
            //allValid = isGeneralMedicalDeviceValid(deviceData);
        }else if(deviceType.equals("Active Implantable Medical Devices")){
            //allValid = isGeneralMedicalDeviceValid(deviceData);
        }

        return allValid;
    }

    private boolean isGeneralMedicalDeviceValid(DeviceData deviceData) {
        boolean allValid = true;
        String fields [] = new String []{
            "gmdn", "risk classification", "custom made", "sterile", "measuring"
        };

        for(String field: fields){
            if(field.equals("gmdn")){
                //Check and verify data is correct
                String termOrDefinition = deviceData.device;
                if(termOrDefinition!=null && !termOrDefinition.equals("")){
                    WaitUtils.waitForElementToBeClickable(driver, gmdnTermDefinition, TIMEOUT_10_SECOND, false);
                    allValid = AssertUtils.areChangesDisplayed(gmdnTermDefinition, termOrDefinition);
                }else{
                    //Gmdn code
                    //WaitUtils.waitForElementToBeClickable(driver, gmdnCode, TIMEOUT_10_SECOND, false);
                    //allValid =  AssertUtils.areChangesDisplayed(gmdnCode, deviceData.gmdnCode);
                }
            }else if(field.equals("risk classification")){
                String data = deviceData.riskClassification;
                if(data!=null && !data.equals("")){
                    data = "Class";
                    allValid = AssertUtils.areChangesDisplayed(riskClassification, data);
                }
            }else if(field.equals("custom made")){
                String data = deviceData.customMade;
                if(data.toLowerCase().equals("y")){
                    allValid = AssertUtils.areChangesDisplayed(customMade, "Yes");
                }else{
                    allValid = AssertUtils.areChangesDisplayed(customMade, "No");
                }
            }else if(field.equals("sterile")){
                String data = deviceData.sterile;
                if(data.toLowerCase().equals("y")){
                    allValid = AssertUtils.areChangesDisplayed(sterile, "Yes");
                }else{
                    allValid = AssertUtils.areChangesDisplayed(sterile, "No");
                }
            }else if(field.equals("measuring")){
                String data = deviceData.measuring;
                if(data.toLowerCase().equals("y")){
                    allValid = AssertUtils.areChangesDisplayed(measuring, "Yes");
                }else{
                    allValid = AssertUtils.areChangesDisplayed(measuring, "No");
                }
            }

            if(!allValid){
                break;
            }
        }

        return allValid;
    }
}
