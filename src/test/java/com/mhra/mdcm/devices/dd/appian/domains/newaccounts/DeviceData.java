package com.mhra.mdcm.devices.dd.appian.domains.newaccounts;

import com.mhra.mdcm.devices.dd.appian.utils.datadriven.ExcelDataSheet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TPD_Auto
 *
 * Encapsultes a data row from our excel data sheets
 */
public class DeviceData {

    public static final String MANUFACTURER_RT_TEST = "ManufacturerRT01Test";
    public static final String AUTHORISED_REP_RT_TEST = "AuthorisedRepRT01Test";
    public int lineNumber;

    //Device data
    public String validatedData;
    public String deviceType;
    public String riskClassification;
    public String device;
    public String description;
    public String sterile;
    public String customMade;
    public String measuring;
    public String evaluation;
    public String isNew;
    public String CTS;
    public String CE;
    public String intended;
    public String notifiedBody;
    public String productNames;
    public String productMakes;
    public String productModels;
    public String manufacturerCodes;
    public String ctsRef;
    public String deviceLabel;
    public String deviceDetails;
    public String instructionDetails;

    //IVD risk classification
    public List<ProductDetail> listOfProductDetails = new ArrayList<>();

    public DeviceData(String[] dataUpdated) {
        //Iterate over each line of data and popultate the correct fields
        for(String dt: dataUpdated){
            String fieldName = ExcelDataSheet.getFieldValue(dt, 0);
            String fieldValue = ExcelDataSheet.getFieldValue(dt, 1);
            if(fieldName!=null)
                populateCorrectField(fieldName, fieldValue);
        }
        //System.out.println("DONE");
    }

    public DeviceData(int lineNumber, String[] dataUpdated) {
        this(dataUpdated);
        this.lineNumber = lineNumber;
    }


    private void populateCorrectField(String fieldName, String fieldValue) {
        String field = fieldName.toLowerCase().trim();
        //System.out.println(field);
        if(field.equals("validateddata")){
            validatedData = fieldValue;
        }
        else if(field.equals("device type")){
            deviceType = getDeviceType(fieldValue);
        }
        else if(field.equals("risk classification")){
            riskClassification = fieldValue;
        }
        else if(field.equals("device")){
            device = fieldValue;
        }
        else if(field.equals("description")){
            description = fieldValue;
        }
        else if(field.equals("sterile")){
            sterile = fieldValue;
        }
        else if(field.equals("custom made")){
            customMade = fieldValue;
        }
        else if(field.equals("measuring")){
            measuring = fieldValue;
        }
        else if(field.equals("evaluation")){
            evaluation = fieldValue;
        }
        else if(field.equals("new")){
            isNew = fieldValue;
        }
        else if(field.equals("cts")){
            CTS = fieldValue;
        }
        else if(field.equals("ce")){
            CE = fieldValue;
        }
        else if(field.equals("intended")){
            intended = fieldValue;
        }
        else if(field.equals("notified body")){
            notifiedBody = fieldValue;
        }
        else if(field.equals("productdetails")){
            //Add to list of values
            if(!fieldValue.equals("NULL") && !fieldValue.equals("")) {
                String[] lop = fieldValue.split(";");
                for (String detail : lop) {
                    ProductDetail pd = new ProductDetail(detail);
                    listOfProductDetails.add(pd);
                }
            }
        }
        else if(field.equals("cts ref")){
            ctsRef = fieldValue;
        }
        else if(field.equals("device label")){
            deviceLabel = fieldValue;
        }
        else if(field.equals("device details")){
            deviceDetails = fieldValue;
        }
        else if(field.equals("instruction details")){
            instructionDetails = fieldValue;
        }
    }

    private String getDeviceType(String fieldValue) {
        String deviceType = fieldValue.toLowerCase();

        if(deviceType.contains("general medical device") || deviceType.contains("gmd")){
            deviceType = "general medical device";
        }else if(deviceType.contains("vitro diagnostic") || deviceType.contains("ivd")){
            deviceType = "vitro diagnostic";
        }else if(deviceType.contains("active implantable") || deviceType.contains("aimd")){
            deviceType = "active implantable";
        }else if(deviceType.contains("procedure pack") || deviceType.contains("spp")){
            deviceType = "procedure pack";
        }
        return  deviceType;
    }


    @Override
    public String toString() {
        return "DeviceData{" +
                "lineNumber='" + lineNumber + '\'' +
                ",validatedData='" + validatedData + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", riskClassification='" + riskClassification + '\'' +
                ", device='" + device + '\'' +
                ", description='" + description + '\'' +
                ", sterile='" + sterile + '\'' +
                ", customMade='" + customMade + '\'' +
                ", measuring='" + measuring + '\'' +
                ", evaluation='" + evaluation + '\'' +
                ", isNew='" + isNew + '\'' +
                ", CTS='" + CTS + '\'' +
                ", CE='" + CE + '\'' +
                ", intended='" + intended + '\'' +
                ", notifiedBody='" + notifiedBody + '\'' +
                ", productNames='" + productNames + '\'' +
                ", productMakes='" + productMakes + '\'' +
                ", productModels='" + productModels + '\'' +
                ", manufacturerCodes='" + manufacturerCodes + '\'' +
                ", ctsRef='" + ctsRef + '\'' +
                ", deviceLabel='" + deviceLabel + '\'' +
                ", deviceDetails='" + deviceDetails + '\'' +
                ", instructionDetails='" + instructionDetails + '\'' +
                ", listOfProducts=" + listOfProductDetails +
                '}';
    }
}
