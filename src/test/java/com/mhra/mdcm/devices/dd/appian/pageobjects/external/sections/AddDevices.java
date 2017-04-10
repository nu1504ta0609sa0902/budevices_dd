package com.mhra.mdcm.devices.dd.appian.pageobjects.external.sections;

import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.DeviceData;
import com.mhra.mdcm.devices.dd.appian.domains.newaccounts.ProductDetail;
import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import com.mhra.mdcm.devices.dd.appian.pageobjects.external.ExternalHomePage;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.RandomDataUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.PageUtils;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Created by TPD_Auto
 */
public class AddDevices extends _Page {

    @FindBy(css = ".FieldLayout---field_error")
    List<WebElement> errorMessages;

    @FindBy(css = ".RadioButtonGroup---choice_pair>label")
    List<WebElement> listOfDeviceTypes;
//    @FindBy(xpath = ".//*[.='Term']//following::td[contains(@class, 'GFWJSJ4DCEB')]")
//    List<WebElement> listOfTermsOrCodeMatches;
    @FindBy(xpath = ".//div[contains(text(),'Term')]//following::a[string-length(text()) > 0]")
    List<WebElement> listOfGmdnMatchesReturnedBySearch;
    @FindBy(css = ".ParagraphText---richtext_paragraph .StrongText---richtext_strong")
    WebElement labelValidGMDNCodeMessage;

    //Device types
    @FindBy(xpath = ".//label[contains(text(),'General Medical')]")
    WebElement generalMedicalDevice;
    @FindBy(xpath = ".//label[contains(text(),'Vitro Diagnostic Device')]")
    WebElement inVitroDiagnosticDevice;
    @FindBy(xpath = ".//label[contains(text(),'Active Implantable')]")
    WebElement activeImplantableMedicalDevice;
    @FindBy(xpath = ".//label[contains(text(),'Procedure Pack')]")
    WebElement systemOrProcedurePack;

    //Product details form inputs
    @FindBy(xpath = ".//label[contains(text(), 'Product name')]//following::input[1]")
    WebElement txtProductName;
    @FindBy(xpath = ".//label[contains(text(), 'Product make')]//following::input[1]")
    WebElement txtProductMake;
    @FindBy(xpath = ".//label[contains(text(), 'Product model')]//following::input[1]")
    WebElement txtProductModel;

    //GMDN search by selecting a radio button
    @FindBy(css = "input[type='text']")
    WebElement tbxGMDNDefinitionOrTerm;

    //Custom made, sterile and measuring
    @FindBy(xpath = ".//span[contains(text(),'custom made')]//following::label[1]")
    WebElement radioCustomMadeYes;
    @FindBy(xpath = ".//span[contains(text(),'custom made')]//following::label[2]")
    WebElement radioCustomMadeNo;
    @FindBy(xpath = ".//span[contains(text(),'device sterile')]//following::label[1]")
    WebElement radioDeviceSterileYes;
    @FindBy(xpath = ".//span[contains(text(),'device sterile')]//following::label[2]")
    WebElement radioDeviceSterileNo;
    @FindBy(xpath = ".//span[contains(text(),'device measuring')]//following::label[1]")
    WebElement radioDeviceMeasuringYes;
    @FindBy(xpath = ".//span[contains(text(),'device measuring')]//following::label[2]")
    WebElement radioDeviceMeasuringNo;

    //Custom Made = No, Then enter risk classification
    @FindBy(xpath = ".//span[contains(text(),'risk class')]//following::label[1]")
    WebElement radioRiskClass1;
    @FindBy(xpath = ".//span[contains(text(),'risk class')]//following::label[2]")
    WebElement radioRiskClass2a;
    @FindBy(xpath = ".//span[contains(text(),'risk class')]//following::label[3]")
    WebElement radioRiskClass2b;
    @FindBy(xpath = ".//span[contains(text(),'risk class')]//following::label[4]")
    WebElement radioRiskClass3;

    //Notified bodies
    @FindBy(xpath = ".//span[contains(text(),'Notified Body')]//following::label[1]")
    WebElement nb0086BSI;
    @FindBy(xpath = ".//span[contains(text(),'Notified Body')]//following::label[2]")
    WebElement nb0088BSI;
    @FindBy(xpath = ".//span[contains(text(),'Notified Body')]//following::label[3]")
    WebElement nb0120BSI;
    @FindBy(xpath = ".//span[contains(text(),'Notified Body')]//following::label[4]")
    WebElement nb0473BSI;
    @FindBy(xpath = ".//span[contains(text(),'Notified Body')]//following::label[5]")
    WebElement nb0843BSI;
    @FindBy(xpath = ".//span[contains(text(),'Notified Body')]//following::label[6]")
    WebElement nbOther;

    //List of notified bodies
    @FindBy(xpath = ".//span[contains(text(),'Notified Body')]//following::input[@type='radio']//following::label")
    List<WebElement> listOfNotifiedBodies;

    //IVD risk classification
    @FindBy(xpath = ".//label[contains(text(),'List A')]")
    WebElement ivdListA;
    @FindBy(xpath = ".//label[contains(text(),'List B')]")
    WebElement ivdListB;
    @FindBy(xpath = ".//label[contains(text(),'Self-Test')]")
    WebElement ivdSelfTest;
    @FindBy(xpath = ".//label[contains(text(),'IVD General')]")
    WebElement ivdIVDGeneral;

    //Procedure pack
    @FindBy(xpath = ".//span[contains(text(),'pack incorporate')]//following::label[1]")
    WebElement ppPackIncorporatedCEMarkingYes;
    @FindBy(xpath = ".//span[contains(text(),'pack incorporate')]//following::label[2]")
    WebElement ppPackIncorporatedCEMarkingNo;
    @FindBy(xpath = ".//span[contains(text(),'devices compatible')]//following::label[1]")
    WebElement ppDevicesCompatibleOriginalIntendedUseYes;
    @FindBy(xpath = ".//span[contains(text(),'devices compatible')]//following::label[2]")
    WebElement ppDevicesCompatibleOriginalIntendedUseNo;


    //Add product
    @FindBy(xpath = ".//button[.='Add product']")
    WebElement addProduct;
    @FindBy(xpath = ".//*[contains(text(),'Product name')]//following::input[1]")
    WebElement pdProductName;
    @FindBy(xpath = ".//*[contains(text(),'Product make')]//following::input[1]")
    WebElement pdProductMake;
    @FindBy(xpath = ".//*[contains(text(),'Product model')]//following::input[1]")
    WebElement pdProductModel;

    @FindBy(xpath = ".//*[contains(text(),'performance eval')]//following::label[1]")
    WebElement radioSubjectToPerformanceEvalYes;
    @FindBy(xpath = ".//*[contains(text(),'performance eval')]//following::label[2]")
    WebElement radioSubjectToPerformanceEvalNo;
    @FindBy(xpath = ".//*[contains(text(),'product new')]//following::label[1]")
    WebElement radioProductNewYes;
    @FindBy(xpath = ".//*[contains(text(),'product new')]//following::label[2]")
    WebElement radioProductNewNo;
    @FindBy(xpath = ".//*[contains(text(),'product conform to')]//following::label[1]")
    WebElement radioConformsToCTSYes;
    @FindBy(xpath = ".//*[contains(text(),'product conform to')]//following::label[2]")
    WebElement radioConformsToCTSNo;
    @FindBy(xpath = ".//*[contains(text(),'provide the CTS')]//following::input[1]")
    WebElement txtCTSReference;
    @FindBy(xpath = ".//*[contains(text(),'demonstrated compliance')]//following::textarea[1]")
    WebElement txtDemonstratedCompliance;
    @FindBy(xpath = ".//*[contains(text(),'testing method')]//following::textarea[1]")
    WebElement txtTestingMethod;
    @FindBy(xpath = ".//*[contains(text(),'product name as it appears')]//following::input[1]")
    WebElement txtProductNameLabel;
    @FindBy(xpath = ".//*[contains(text(),'device label')]//following::textarea[1]")
    WebElement txtDocumentDetails;
    @FindBy(xpath = ".//*[contains(text(),'device label')]//following::textarea[2]")
    WebElement txtInstructionDetails;


    //Option to add other devices
    @FindBy(xpath = ".//button[contains(text(),'Add another device')]")
    WebElement btnAddAnotherDevice;

    //File upload buttons
    @FindBy(css = ".FileUploadWidget---ui-inaccessible")
    WebElement fileUpload;
    @FindBy(css = ".FileUploadWidget---ui-inaccessible")
    List<WebElement> listOfFileUploads;

    //Confirm and btnDeclareDevices
    @FindBy(xpath = ".//button[contains(text(),'Review your order')]")
    WebElement btnReviewYourOrder;
    @FindBy(xpath = ".//button[.='Proceed to payment']")
    WebElement btnProceedToPayment;
    @FindBy(xpath = ".//button[contains(text(),'Finish')]")
    WebElement btnFinish;
    @FindBy(xpath = ".//button[.='Remove']")
    WebElement btnRemove;
    @FindBy(css = ".Button---primary")
    WebElement submitConfirm;

    //Submit and save buttons
    @FindBy(xpath = ".//button[.='Add device']")
    WebElement btnAddDevice;
    @FindBy(xpath = ".//button[.='Save']")
    WebElement btnSaveProgress;

    //Error message
    @FindBy(css = ".component_error")
    WebElement errMessage;
    @FindBy(css = ".MessageLayout---error")
    WebElement validationErrMessage;

    //Device Summary
    @FindBy(xpath = ".//div[contains(text(),'GMDN code')]//following::a")
    List<WebElement> listOfGMDNLinksInSummary;
    @FindBy(partialLinkText = "Change Notified Body")
    WebElement linkChangeNotifiedBody;

    //All GMDN table
    @FindBy(xpath = ".//*[contains(text(),'Term definition')]//following::tr/td[2]")
    List<WebElement> listOfAllGmdnTermDefinitions;

    //Links
    @FindBy(partialLinkText = "View all GMDN terms")
    WebElement viewAllGMDNTermDefinition;

    public AddDevices(WebDriver driver) {
        super(driver);
    }

    public AddDevices addDevice() {
        WaitUtils.waitForElementToBeClickable(driver, btnAddDevice, TIMEOUT_MEDIUM, false);
        btnAddDevice.click();
        return new AddDevices(driver);
    }

    public boolean isDeviceTypeCorrect() {
        boolean allCorrect = false;

        WaitUtils.waitForElementToBeClickable(driver, generalMedicalDevice, TIMEOUT_MEDIUM, false);
        for (WebElement e : listOfDeviceTypes) {
            String text = e.getText();
            if (text.toLowerCase().contains("general medical device") || text.toLowerCase().contains("in vitro diagnostic device") ||
                    text.toLowerCase().contains("active implantable medical device") || text.toLowerCase().contains("system or procedure pack")) {
                allCorrect = true;
            } else {
                allCorrect = false;
                break;
            }
        }

        return allCorrect;
    }


    public boolean isErrorMessageDisplayed() {
        try {
            WaitUtils.waitForElementToBeVisible(driver, By.cssSelector(".FieldLayout---field_error"), 3, false);
            WaitUtils.waitForElementToBeClickable(driver, By.cssSelector(".FieldLayout---field_error"), 3, false);
            boolean isDisplayed = errorMessages.size() > 0;
            return isDisplayed;
        } catch (Exception e) {
            return false;
        }
    }

    public AddDevices addFollowingDevice(DeviceData dd) {
        WaitUtils.waitForElementToBeClickable(driver, generalMedicalDevice, TIMEOUT_MEDIUM, false);
        WaitUtils.waitForElementToBeClickable(driver, systemOrProcedurePack, TIMEOUT_MEDIUM, false);
        //Select device type
        selectDeviceType(dd);

        if (dd.deviceType.toLowerCase().contains("general medical device")) {
            addGeneralMedicalDevice(dd);
        } else if (dd.deviceType.toLowerCase().contains("vitro diagnostic")) {
            addVitroDiagnosticDevice(dd);
        } else if (dd.deviceType.toLowerCase().contains("active implantable")) {
            addActiveImplantableDevice(dd);
        } else if (dd.deviceType.toLowerCase().contains("procedure pack")) {
            addProcedurePackDevice(dd);
        } else {
            //Verify all error messages if possible
        }

        //Business doing testing so don't do any write only tests
        WaitUtils.waitForElementToBeClickable(driver, btnReviewYourOrder, TIMEOUT_MEDIUM, false);
        PageUtils.doubleClick(driver, btnReviewYourOrder);

        return new AddDevices(driver);
    }

    private void addGeneralMedicalDevice(DeviceData dd) {
        searchByGMDN(dd);
        customMade(dd);

        if(dd.customMade.toLowerCase().equals("n")) {
            deviceSterile(dd);
            deviceMeasuring(dd);

            if (dd.sterile.toLowerCase().equals("y") || dd.measuring.toLowerCase().equals("y")) {
                if (dd.customMade.toLowerCase().equals("n"))
                    notifiedBody(dd);
            }
        }
        //saveProduct(dd);
    }

    private void addVitroDiagnosticDevice(DeviceData dd) {
        searchByGMDN(dd);
        riskClassificationIVD(dd);

        //No product needs to be added when Risk Classification = IVD General
        if(dd.riskClassification!=null && !dd.riskClassification.equals("ivd general")) {
            //If more than 1 product listed
//            int numberOfProductName = dd.listOfProductDetails.size();
//            if (numberOfProductName <= 1) {
//                ProductDetail productDetail = dd.listOfProductDetails.get(0);
//                if (numberOfProductName == 1) {
//                    dd.productNames = productDetail.name;
//                }
//                //List of device to add
//                addProduct(productDetail);
//                subjectToPerformanceEval(dd);
//                productNewToMarket(dd);
//                if (dd.riskClassification.toLowerCase().contains("list a"))
//                    conformToCTS(dd);
//                notifiedBody(dd);
//                saveProduct(dd);
//            } else {
//                for (ProductDetail x : dd.listOfProductDetails) {
//                    dd.productNames = x.name;
//                    addProduct(x);
//                    subjectToPerformanceEval(dd);
//                    productNewToMarket(dd);
//                    if (dd.riskClassification.toLowerCase().contains("list a"))
//                        conformToCTS(dd);
//                    notifiedBody(dd);
//                    saveProduct(dd);
//
//                    //Remove this if we find a better solution
//                    WaitUtils.nativeWaitInSeconds(1);
//                }
//            }
            for (ProductDetail x : dd.listOfProductDetails) {
                dd.productNames = x.name;
                addProduct(x);
                subjectToPerformanceEval(dd);
                productNewToMarket(dd);
                if (dd.riskClassification.toLowerCase().contains("list a"))
                    conformToCTS(dd);
                notifiedBody(dd);
                saveProduct(dd);

                //Remove this if we find a better solution
                WaitUtils.nativeWaitInSeconds(1);
            }
        }
    }

    private void addActiveImplantableDevice(DeviceData dd) {
        searchByGMDN(dd);
        customMade(dd);
        int numberOfProductName = dd.listOfProductDetails.size();
        if(numberOfProductName <= 1) {
            if(numberOfProductName==1){
                dd.productNames = dd.listOfProductDetails.get(0).name;
            }
            //List of device to add
            if (dd.customMade.toLowerCase().equals("y")) {
                productLabelName(dd);
            }
        }else{
            for(ProductDetail x: dd.listOfProductDetails){
                productLabelName(x.name);
            }
        }
        //saveProduct(dd);
    }

    private void addProcedurePackDevice(DeviceData dd) {
        searchByGMDN(dd);
//        customMade(dd); removed since 04/01/2017
//        deviceMeasuring(dd); removed since 04/01/2017

        deviceSterile(dd);

        if (dd.sterile.toLowerCase().equals("y") || dd.measuring.toLowerCase().equals("y")) {
            //if (dd.customMade.toLowerCase().equals("n"))
                notifiedBody(dd);
        }
        packIncorporated(dd);
        devicesCompatible(dd);
        //saveProduct(dd);
    }

    private void productLabelName(DeviceData dd) {
        WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//button[.='Add Product']"), TIMEOUT_MEDIUM, false);
        driver.findElement(By.xpath(".//button[.='Add Product']")).click();
        WaitUtils.waitForElementToBeClickable(driver, txtProductNameLabel, TIMEOUT_MEDIUM, false);
        //txtProductNameLabel.sendKeys(RandomDataUtils.getRandomTestName("Label"));
//        txtProductNameLabel.clear();
//        txtProductNameLabel.sendKeys(dd.deviceLabel);
        PageUtils.clearAndTypeText(txtProductNameLabel, dd.deviceLabel, true);

        PageUtils.uploadDocument(fileUpload, "DeviceLabelDoc2.pdf", 1, 3);
        txtDocumentDetails.sendKeys(dd.deviceDetails);
        PageUtils.uploadDocument(listOfFileUploads.get(1), "DeviceInstructionForUse1.pdf", 1, 3);
        txtInstructionDetails.sendKeys(dd.instructionDetails);

        //Save product label details
        WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//button[.='Save Product']"), TIMEOUT_MEDIUM, false);
        driver.findElement(By.xpath(".//button[.='Save Product']")).click();

    }


    private void productLabelName(String labelName) {
        WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//button[.='Add Product']"), TIMEOUT_MEDIUM, false);
        driver.findElement(By.xpath(".//button[.='Add Product']")).click();
        WaitUtils.nativeWaitInSeconds(1);
        WaitUtils.waitForElementToBeClickable(driver, txtProductNameLabel, TIMEOUT_MEDIUM, false);
//        txtProductNameLabel.clear();
//        txtProductNameLabel.sendKeys(labelName);
        PageUtils.clearAndTypeText(txtProductNameLabel, labelName, true);

        PageUtils.uploadDocument(fileUpload, "DeviceLabelDoc2.pdf", 1, 3);
        PageUtils.uploadDocument(listOfFileUploads.get(1), "DeviceInstructionForUse1.pdf", 1, 3);

        //Save product label details
        WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//button[.='Save Product']"), TIMEOUT_MEDIUM, false);
        driver.findElement(By.xpath(".//button[.='Save Product']")).click();
    }


    private void conformToCTS(DeviceData dd) {
        WaitUtils.waitForElementToBeClickable(driver, radioConformsToCTSYes, TIMEOUT_MEDIUM, false);
        if (dd.CTS.toLowerCase().equals("y")) {
            PageUtils.doubleClick(driver, radioConformsToCTSYes);
            WaitUtils.waitForElementToBeClickable(driver, txtCTSReference, TIMEOUT_MEDIUM, false);
//            txtCTSReference.clear();
//            txtCTSReference.sendKeys("CTS039458430958");
            PageUtils.clearAndTypeText(txtCTSReference, "CTS039458430958", true);
        } else {
            PageUtils.doubleClick(driver, radioConformsToCTSNo);
            WaitUtils.waitForElementToBeClickable(driver, txtDemonstratedCompliance, TIMEOUT_MEDIUM, false);
//            txtDemonstratedCompliance.clear();
//            txtDemonstratedCompliance.sendKeys("Demonstrated Compliance");
//            txtTestingMethod.clear();
//            txtTestingMethod.sendKeys("Manually Tested");
            PageUtils.clearAndTypeText(txtDemonstratedCompliance,"Demonstrated Compliance" , true);
            PageUtils.clearAndTypeText(txtTestingMethod,"Manually Tested" , true);
        }
    }

    private void saveProduct(DeviceData dd) {
        WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//button[.='Save product']"), TIMEOUT_MEDIUM, false);
        WebElement saveProduct = driver.findElement(By.xpath(".//button[.='Save product']"));
        saveProduct.click();
    }

    private void productNewToMarket(DeviceData dd) {
        WaitUtils.waitForElementToBeClickable(driver, radioProductNewYes, TIMEOUT_MEDIUM, false);
        if (dd.isNew.toLowerCase().contains("new")) {
            PageUtils.doubleClick(driver, radioProductNewYes);
        } else {
            PageUtils.doubleClick(driver, radioProductNewNo);
        }
    }

    private void subjectToPerformanceEval(DeviceData dd) {
        WaitUtils.waitForElementToBeClickable(driver, radioSubjectToPerformanceEvalYes, TIMEOUT_MEDIUM, false);
        if (dd.evaluation.toLowerCase().equals("y")) {
            PageUtils.doubleClick(driver, radioSubjectToPerformanceEvalYes);
        } else {
            PageUtils.doubleClick(driver, radioSubjectToPerformanceEvalNo);
        }
    }

    private void addProduct(ProductDetail productDetail) {
        WaitUtils.waitForElementToBeClickable(driver, addProduct, TIMEOUT_MEDIUM, false);
        WaitUtils.nativeWaitInSeconds(1);
        addProduct.click();

        //Wait for form to be visible
        String productName = productDetail.name;
        String productMake = productDetail.make;
        String productModel = productDetail.model;
        WaitUtils.waitForElementToBeClickable(driver, pdProductModel, TIMEOUT_MEDIUM, false);
        if (productName != null || !productName.equals("")) {
//            pdProductName.clear();
//            pdProductName.sendKeys(productName);
            PageUtils.clearAndTypeText(pdProductName,productName , true);
        } else if (productMake != null || !productMake.equals("")) {
//            pdProductMake.click();
//            pdProductMake.sendKeys(productMake);
            PageUtils.clearAndTypeText(pdProductMake,productMake , true);
            PageUtils.clearAndTypeText(pdProductModel,productModel , true);
        }

//        pdProductModel.clear();
//        pdProductModel.sendKeys(productModel);
//        PageUtils.clearAndTypeText(pdProductModel,productModel , true);
    }

    private void devicesCompatible(DeviceData dd) {
        //Does the system or procedure pack incorporate a medical device that does not bear a CE marking?
        WaitUtils.waitForElementToBeClickable(driver, ppDevicesCompatibleOriginalIntendedUseYes, TIMEOUT_MEDIUM, false);
        if (dd.intended.toLowerCase().equals("y")) {
            PageUtils.doubleClick(driver, ppDevicesCompatibleOriginalIntendedUseYes);
        } else {
            PageUtils.doubleClick(driver, ppDevicesCompatibleOriginalIntendedUseNo);
        }
    }

    private void packIncorporated(DeviceData dd) {
        //Are the chosen combination of medical devices compatible in view of their original intended use?
        WaitUtils.waitForElementToBeClickable(driver, ppPackIncorporatedCEMarkingYes, TIMEOUT_MEDIUM, false);
        if (dd.CE.toLowerCase().equals("y")) {
            PageUtils.doubleClick(driver, ppPackIncorporatedCEMarkingYes);
        } else {
            PageUtils.doubleClick(driver, ppPackIncorporatedCEMarkingNo);
        }
    }

    private void notifiedBody(DeviceData dd) {
        changeNotifiedBody();
        //Select notified body
        if (dd.notifiedBody!=null && dd.notifiedBody.toLowerCase().contains("0086")) {
            WaitUtils.waitForElementToBeClickable(driver, nb0086BSI, TIMEOUT_MEDIUM, false);
            PageUtils.doubleClick(driver, nb0086BSI);
//        }else if (dd.notifiedBody!=null && dd.notifiedBody.toLowerCase().contains("0088")) {
//            WaitUtils.waitForElementToBeClickable(driver, nb0088LLOYDS, TIMEOUT_MEDIUM, false);
//            PageUtils.doubleClick(driver, nb0088LLOYDS);
//        }else if (dd.notifiedBody!=null && dd.notifiedBody.toLowerCase().contains("0120")) {
//            WaitUtils.waitForElementToBeClickable(driver, nb0120SGS, TIMEOUT_MEDIUM, false);
//            PageUtils.doubleClick(driver, nb0120SGS);
//        }else if (dd.notifiedBody!=null && dd.notifiedBody.toLowerCase().contains("0473")) {
//            WaitUtils.waitForElementToBeClickable(driver, nb00473AMTAC, TIMEOUT_MEDIUM, false);
//            PageUtils.doubleClick(driver, nb00473AMTAC);
//        }else if (dd.notifiedBody!=null && dd.notifiedBody.toLowerCase().contains("0843")) {
//            WaitUtils.waitForElementToBeClickable(driver, nb00843UL, TIMEOUT_MEDIUM, false);
//            PageUtils.doubleClick(driver, nb00843UL);
        }else {
            //THIS REQUIRES A NUMBER
            //PageUtils.doubleClick(driver, nbOthers);
            WaitUtils.waitForElementToBeClickable(driver, nb0086BSI, TIMEOUT_MEDIUM, false);
            PageUtils.doubleClick(driver, nb0086BSI);
        }
    }

    private void changeNotifiedBody() {
        try{
            WaitUtils.waitForElementToBeClickable(driver, linkChangeNotifiedBody, TIMEOUT_SMALL, false);
            linkChangeNotifiedBody.click();
            WaitUtils.waitForElementToBeClickable(driver, nb0086BSI, TIMEOUT_SMALL, false);
            WaitUtils.nativeWaitInSeconds(1);
        }catch (Exception e){
            //Bug which maintains previous selection of notified body
        }
    }

    private void riskClassificationIVD(DeviceData dd) {
        WaitUtils.waitForElementToBeClickable(driver, ivdIVDGeneral, TIMEOUT_MEDIUM, false);
        WaitUtils.nativeWaitInSeconds(1);

        String lcRiskClassification = dd.riskClassification.toLowerCase();

        if (lcRiskClassification.contains("ivd general")) {
            //WaitUtils.waitForElementToBePartOfDOM(driver, By.xpath(".//label[contains(text(),'IVD General')]"), TIMEOUT_MEDIUM, false);
            //PageUtils.clickIfVisible(driver, ivdIVDGeneral);
            ivdIVDGeneral.click();
        } else if (lcRiskClassification.contains("list a")) {
            //WaitUtils.waitForElementToBePartOfDOM(driver, By.xpath(".//label[contains(text(),'List A')]"), TIMEOUT_MEDIUM, false);
            //PageUtils.clickIfVisible(driver, ivdListA);
            ivdListA.click();
        } else if (lcRiskClassification.contains("list b")) {
            //WaitUtils.waitForElementToBePartOfDOM(driver, By.xpath(".//label[contains(text(),'List B')]"), TIMEOUT_MEDIUM, false);
            //PageUtils.clickIfVisible(driver, ivdListB);
            ivdListB.click();
        } else if (lcRiskClassification.contains("self-test")) {
            //WaitUtils.waitForElementToBePartOfDOM(driver, By.xpath(".//label[contains(text(),'Self-Test')]"), TIMEOUT_MEDIUM, false);
            //PageUtils.clickIfVisible(driver, ivdSelfTest);
            ivdSelfTest.click();
        }
    }

    private void selectDeviceType(DeviceData dd) {
        WaitUtils.waitForElementToBeClickable(driver, generalMedicalDevice, TIMEOUT_MEDIUM, false);
        String lcDeviceType = dd.deviceType.toLowerCase();
        if (lcDeviceType.contains("general medical device")) {
            PageUtils.clickIfVisible(driver, generalMedicalDevice);
            PageUtils.doubleClick(driver, generalMedicalDevice);
        } else if (lcDeviceType.contains("vitro diagnostic")) {
            PageUtils.clickIfVisible(driver, inVitroDiagnosticDevice);
            PageUtils.doubleClick(driver, inVitroDiagnosticDevice);
        } else if (lcDeviceType.contains("active implantable")) {
            PageUtils.clickIfVisible(driver, activeImplantableMedicalDevice);
            PageUtils.doubleClick(driver, activeImplantableMedicalDevice);
        } else if (lcDeviceType.contains("procedure pack")) {
            PageUtils.clickIfVisible(driver, systemOrProcedurePack);
            PageUtils.doubleClick(driver, systemOrProcedurePack);
        }
    }

    private void customMade(DeviceData dd) {
        WaitUtils.waitForElementToBeClickable(driver, radioCustomMadeYes, TIMEOUT_MEDIUM, false);
        if (dd.customMade.toLowerCase().equals("y")) {
            PageUtils.doubleClick(driver, radioCustomMadeYes);
        } else {
            PageUtils.doubleClick(driver, radioCustomMadeNo);
            riskClassification(dd);
        }
    }

    private void deviceMeasuring(DeviceData dd) {
        WaitUtils.waitForElementToBeClickable(driver, radioDeviceMeasuringYes, TIMEOUT_MEDIUM, false);
        if (dd.measuring.toLowerCase().equals("y")) {
            PageUtils.doubleClick(driver, radioDeviceMeasuringYes);
        } else {
            PageUtils.doubleClick(driver, radioDeviceMeasuringNo);
        }
    }

    private void deviceSterile(DeviceData dd) {
        WaitUtils.waitForElementToBeClickable(driver, radioDeviceSterileYes, TIMEOUT_MEDIUM, false);
        if (dd.sterile.toLowerCase().equals("y")) {
            PageUtils.doubleClick(driver, radioDeviceSterileYes);
        } else {
            PageUtils.doubleClick(driver, radioDeviceSterileNo);
        }
    }

    private void riskClassification(DeviceData dd) {
        WaitUtils.waitForElementToBeClickable(driver, radioRiskClass1, TIMEOUT_MEDIUM, false);
        //WaitUtils.waitForElementToBeClickable(driver, nb0086BSI, TIMEOUT_MEDIUM, false);
        String lcRiskClassiffication = dd.riskClassification.toLowerCase();
        if(lcRiskClassiffication!=null) {
            if (lcRiskClassiffication.contains("class i")) {
                PageUtils.doubleClick(driver, radioRiskClass1);
            } else if (lcRiskClassiffication.contains("class iia")) {
                PageUtils.doubleClick(driver, radioRiskClass2a);
            } else if (lcRiskClassiffication.contains("class iib")) {
                PageUtils.doubleClick(driver, radioRiskClass2b);
            } else if (lcRiskClassiffication.contains("class iii")) {
                PageUtils.doubleClick(driver, radioRiskClass3);
            }
        }
    }

    private void searchByGMDN(DeviceData dd) {
        if (dd.device != null) {
            String [] arrayOfDeviceBecauseTheyKeepBloodyChanging = {
                    "cat", "res", "tis", "sco", "con", "pro"
            };
            int pos = 0;
            String searchFor = arrayOfDeviceBecauseTheyKeepBloodyChanging[pos];
            boolean isErrorMessageDisplayed = false;
            do {
                WaitUtils.waitForElementToBeClickable(driver, tbxGMDNDefinitionOrTerm, TIMEOUT_MEDIUM, false);
                tbxGMDNDefinitionOrTerm.clear();
                tbxGMDNDefinitionOrTerm.sendKeys(searchFor);
                WaitUtils.isPageLoadingComplete(driver, 1);

                //Wait for list of items to appear and add it only if its not a duplicate
                WaitUtils.waitForElementToBeClickable(driver, By.xpath(".//div[contains(text(),'Term')]//following::td"), TIMEOUT_DEFAULT, false);
                int randomPosition = RandomDataUtils.getARandomNumberBetween(0, listOfGmdnMatchesReturnedBySearch.size());
                WebElement element = listOfGmdnMatchesReturnedBySearch.get(randomPosition);
                element.click();
                //element.findElement(By.tagName("a")).click();
                //WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);

                //If its a duplicate Try again
                isErrorMessageDisplayed = isErrorMessageDisplayed();
                if(isErrorMessageDisplayed) {
                    //Try again
                    pos++;
                    searchFor = arrayOfDeviceBecauseTheyKeepBloodyChanging[pos];
                }else{
                    log.info(dd.deviceType + " => " + searchFor);
                }
            }while (isErrorMessageDisplayed);

            //Default is search by gmdn term or definition : This removed 03/02/2017 push
            //previousGMDNSelection(dd);

        } else {
//            WaitUtils.waitForElementToBeClickable(driver, radioByGMDNCode, TIMEOUT_MEDIUM, false);
//            radioByGMDNCode.click();
//            WaitUtils.waitForElementToBeClickable(driver, tbxGMDNCode, TIMEOUT_MEDIUM, false);
//            tbxGMDNCode.sendKeys(dd.gmdnCode);
//            lblGMDNDefinitionOrTerm.click();
            //PageUtils.selectFromAutoSuggests(driver, By.cssSelector("input.gwt-SuggestBox"), dd.gmdnTermOrDefinition);
        }
    }

//    private void previousGMDNSelection(DeviceData dd) {
//        WaitUtils.waitForElementToBeClickable(driver, radioGMDNDefinitionOrTerm, TIMEOUT_MEDIUM, false);
//        radioGMDNDefinitionOrTerm.click();
//        WaitUtils.nativeWaitInSeconds(1);
//        PageUtils.doubleClick(driver, radioByGMDNCode);
//        WaitUtils.nativeWaitInSeconds(1);
//        PageUtils.doubleClick(driver, radioGMDNDefinitionOrTerm);
//        WaitUtils.waitForElementToBeClickable(driver, tbxGMDNDefinitionOrTermSuggest, TIMEOUT_MEDIUM, false);
//        tbxGMDNDefinitionOrTermSuggest.clear();
//        boolean completed = PageUtils.selectFromAutoSuggests(driver, By.cssSelector("input.gwt-SuggestBox"), dd.device);
//        if(!completed){
//            log.error("No items found for GMDN term : " + dd.device);
//        }
//    }

    public boolean isOptionToAddAnotherDeviceVisible() {
        //WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
        WaitUtils.nativeWaitInSeconds(2);
        try {
            WaitUtils.waitForElementToBeClickable(driver, btnAddAnotherDevice, TIMEOUT_MEDIUM, false);
            boolean isVisible = btnAddAnotherDevice.isDisplayed() && btnAddAnotherDevice.isEnabled();
            return isVisible;
        }catch (Exception e){
            return false;
        }
    }

    public boolean isOptionToReviewYourOrderVisible() {
        try {
            WaitUtils.waitForElementToBeClickable(driver, btnReviewYourOrder, TIMEOUT_MEDIUM, false);
            boolean isVisible = btnReviewYourOrder.isDisplayed() && btnReviewYourOrder.isEnabled();
            return isVisible;
        }catch (Exception e){
            return false;
        }
    }

    public AddDevices proceedToPayment() {
        WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
        WaitUtils.nativeWaitInSeconds(2);
        WaitUtils.waitForElementToBeClickable(driver, btnProceedToPayment, TIMEOUT_MEDIUM, false);
        btnProceedToPayment.click();
        log.info("Proceed to payment");
        return new AddDevices(driver);
    }

    public AddDevices submitRegistration() {
        WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
        WaitUtils.nativeWaitInSeconds(1);
        WaitUtils.waitForElementToBeClickable(driver, submitConfirm, TIMEOUT_MEDIUM, false);
        submitConfirm.click();
        log.info("Submit for registration");
        return new AddDevices(driver);
    }

    public ExternalHomePage finish() {
        WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
        WaitUtils.nativeWaitInSeconds(1);
        WaitUtils.waitForElementToBeClickable(driver, btnFinish, TIMEOUT_MEDIUM, false);
        btnFinish.click();
        return new ExternalHomePage(driver);
    }

    public boolean isGMDNValueDisplayed(DeviceData data) {
        WaitUtils.waitForElementToBeClickable(driver, btnAddAnotherDevice, TIMEOUT_MEDIUM, false);
        WaitUtils.waitForElementToBeClickable(driver, btnProceedToPayment, TIMEOUT_MEDIUM, false);
        boolean isDisplayed = false;
        String valueToCheck = "";

        if(data.device!=null){
            valueToCheck = data.device.toLowerCase();
        }else{
            //valueToCheck = data.gmdnCode;
        }

        for(WebElement el: listOfGMDNLinksInSummary){
            String text = el.getText();
            if(text.toLowerCase().contains(valueToCheck)){
                isDisplayed = true;
                break;
            }
        }

        return isDisplayed;
    }

    public boolean isGMDNValueDisplayed(String valueToCheck) {
        boolean isCorrect = false;

        for(WebElement el: listOfGMDNLinksInSummary){
            String text = el.getText();
            if(text.contains(valueToCheck)){
                isCorrect = true;
                break;
            }
        }

        return isCorrect;
    }

    public AddDevices addAnotherDevice() {
        WaitUtils.waitForElementToBeClickable(driver, btnAddAnotherDevice, TIMEOUT_MEDIUM, false);
        btnAddAnotherDevice.click();
        return new AddDevices(driver);
    }


//    public AddDevices viewDeviceWithGMDNValue(String gmdnCode) {
//        //boolean isNumeric = AssertUtils.isNumeric(gmdnCode);
//        WebElement el = CommonUtils.getElementWithLink(listOfGMDNLinksInSummary, gmdnCode);
//        WaitUtils.waitForElementToBeClickable(driver, el, TIMEOUT_MEDIUM, false);
//        el.click();
//        return new AddDevices(driver);
//    }

    public AddDevices removeSelectedDevice() {
        WaitUtils.waitForElementToBeClickable(driver, btnRemove, TIMEOUT_MEDIUM, false);
        btnRemove.click();
        return new AddDevices(driver);
    }
}
