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
import org.openqa.selenium.support.PageFactory;

import java.util.List;

/**
 * Created by TPD_Auto
 */
public class AddDevices extends _Page {

    @FindBy(css = ".FieldLayout---field_error")
    List<WebElement> errorMessages;

    @FindBy(css = ".RadioButtonGroup---choice_pair>label")
    List<WebElement> listOfDeviceTypes;
    @FindBy(xpath = ".//label[contains(text(),'GMDN Code')]//following::tr/td")
    WebElement firstGMDNMatchReturnedBySearch;
    @FindBy(xpath = ".//label[contains(text(),'GMDN Code')]//following::tr/td")
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

    //New EU medical device rules
    @FindBy(xpath = ".//span[contains(text(),'new medical device regulation')]//following::label[1]")
    WebElement radioEURuleYes;
    @FindBy(xpath = ".//span[contains(text(),'new medical device regulation')]//following::label[2]")
    WebElement radioEURuleNo;

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
    @FindBy(xpath = ".//button[contains(text(),'Add Product')]")
    WebElement addProduct2;
    @FindBy(xpath = ".//*[contains(text(),'Enter name')]//following::input[1]")
    WebElement pdProductName;
    @FindBy(xpath = ".//*[contains(text(),'and model')]//following::input[1]")
    WebElement pdProductMake;
    @FindBy(xpath = ".//*[contains(text(),'and model')]//following::input[2]")
    WebElement pdProductModel;
    @FindBy(xpath = ".//label[.='Enter name']")
    WebElement cbxProductName;
    @FindBy(xpath = ".//label[contains(text(),'and model')]")
    WebElement cbxMakeAndModel;
    @FindBy(xpath = ".//button[.='Save product']")
    WebElement saveProduct2;

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
    @FindBy(css = ".MultipleFileUploadWidget---ui-inaccessible")
    WebElement multiFileUpload;
    @FindBy(css = ".FileUploadWidget---ui-inaccessible")
    WebElement fileUpload;
    @FindBy(css = ".FileUploadWidget---ui-inaccessible")
    List<WebElement> listOfFileUploads;

    //Confirm and btnDeclareDevices
    @FindBy(xpath = ".//button[contains(text(),'Review your order')]")
    WebElement btnReviewYourOrder;
    @FindBy(xpath = ".//button[.='Continue']")
    WebElement btnProceedToPayment;
    @FindBy(xpath = ".//button[.='Continue']")
    WebElement btnProceedToReview;
    @FindBy(xpath = ".//button[contains(text(),'Finish')]")
    WebElement btnFinish;
    @FindBy(xpath = ".//button[.='Remove']")
    WebElement btnRemove;
    @FindBy(css = ".Button---primary")
    WebElement bthSubmitConfirm;
    @FindBy(css = ".CheckboxGroup---choice_pair>label")
    WebElement cbxConfirmInformation;

    //Submit and save buttons
    @FindBy(xpath = ".//button[.='Add device']")
    WebElement btnAddDevice;
    @FindBy(xpath = ".//button[.='Save']")
    WebElement btnSaveProgress;
    @FindBy(xpath = ".//button[.='Save product']")
    WebElement btnSaveProduct;
    @FindBy(xpath = ".//button[.='Save product']")
    WebElement btnSaveProduct2;

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
    @FindBy(partialLinkText = "Back to service")
    WebElement linkBackToService;

    @FindBy(xpath = ".//h3[contains(text(), 'Application complete')]/following::h4[1]")
    WebElement txtApplicationReference;

    //Payment methods
    @FindBy(xpath = ".//label[contains(text(),'Worldpay')]")
    WebElement paymentWorldPay;
    @FindBy(xpath = ".//label[contains(text(),'BACS')]")
    WebElement paymentBACS;
    @FindBy(xpath = ".//button[contains(text(),'Complete application')]")
    WebElement btnCompleteApplication;
    @FindBy(xpath = ".//div[@role='listbox']")
    WebElement ddAddressBox;

    //Product details : New Medical device names
    @FindBy(xpath = ".//*[contains(text(),'Medical device name')]//following::input[1]")
    WebElement pdMedicalDeviceName;
    @FindBy(xpath = ".//*[contains(text(),'Medical device name')]//following::input[2]")
    WebElement pdMedicalDeviceModel;
    @FindBy(xpath = ".//*[contains(text(),'Medical Device Name')]//following::input[1]")
    WebElement pdMedicalDeviceNameAIMD;

    public AddDevices(WebDriver driver) {
        super(driver);
    }

    public AddDevices addDevice() {
        WaitUtils.waitForElementToBeClickable(driver, btnAddDevice, TIMEOUT_15_SECOND, false);
        btnAddDevice.click();
        return new AddDevices(driver);
    }

    public boolean isDeviceTypeCorrect() {
        boolean allCorrect = false;

        WaitUtils.waitForElementToBeClickable(driver, generalMedicalDevice, TIMEOUT_15_SECOND, false);
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
            WaitUtils.nativeWaitInSeconds(1);
            WaitUtils.waitForElementToBeVisible(driver, By.cssSelector(".FieldLayout---field_error"), 3, false);
            WaitUtils.waitForElementToBeClickable(driver, By.cssSelector(".FieldLayout---field_error"), 3, false);
            boolean isDisplayed = errorMessages.size() > 0;
            return isDisplayed;
        } catch (Exception e) {
            return false;
        }
    }

    public AddDevices addFollowingDevice(DeviceData dd) {
        WaitUtils.waitForElementToBeClickable(driver, generalMedicalDevice, TIMEOUT_15_SECOND, false);
        WaitUtils.waitForElementToBeClickable(driver, systemOrProcedurePack, TIMEOUT_15_SECOND, false);
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
        WaitUtils.waitForElementToBeClickable(driver, btnSaveProgress, TIMEOUT_15_SECOND, false);
        PageUtils.doubleClick(driver, btnSaveProgress);

        return new AddDevices(driver);
    }

    private void addGeneralMedicalDevice(DeviceData dd) {
        searchByGMDN(dd);
        customMade(dd);
        compliesWithEUDeviceRequirements(true);

        if (dd.customMade.toLowerCase().equals("n")) {
            deviceSterile(dd);
            deviceMeasuring(dd);

            if (dd.sterile.toLowerCase().equals("y") || dd.measuring.toLowerCase().equals("y")) {
                if (dd.customMade.toLowerCase().equals("n")) {
                    riskClassification(dd);
                    notifiedBody(dd);
                }
            }
        }

    }

    private void compliesWithEUDeviceRequirements(boolean compliesWitEU) {

        WaitUtils.waitForElementToBeClickable(driver, radioEURuleYes, TIMEOUT_15_SECOND, false);
        if (compliesWitEU) {
            PageUtils.doubleClick(driver, radioEURuleYes);
        } else {
            PageUtils.doubleClick(driver, radioEURuleNo);
        }
    }

    private void addVitroDiagnosticDevice(DeviceData dd) {
        searchByGMDN(dd);
        compliesWithEUDeviceRequirements(true);
        riskClassificationIVD(dd);

        int productCount = 0;
        //No product needs to be added when Risk Classification = IVD General
        if (dd.riskClassification != null && !dd.riskClassification.equals("ivd general")) {
            for (ProductDetail x : dd.listOfProductDetails) {
                dd.productName = x.name;
                //addProduct(x);
                if(productCount > 0){
                    PageUtils.clickIfVisible(driver, addProduct);
                }
                addProductNew(dd);
                addProductModel(dd);
                notifiedBody(dd);
                subjectToPerformanceEval(dd);
                productNewToMarket(dd);
                if (dd.riskClassification.toLowerCase().contains("list a"))
                    conformToCTS(dd);
                //notifiedBody(dd);
                saveProduct(dd);

                //Remove this if we find a better solution
                WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
                WaitUtils.nativeWaitInSeconds(1);

                productCount++;
            }
        }
    }


    private void addProductModel(DeviceData dd) {
        WaitUtils.waitForElementToBeClickable(driver, pdMedicalDeviceModel, TIMEOUT_15_SECOND, false);
        pdMedicalDeviceModel.clear();
        String pn = dd.productName;
        if(pn == null){
            pn = "Model";
            dd.productName = pn;
        }
        pdMedicalDeviceModel.sendKeys(RandomDataUtils.getRandomTestName(pn));
    }

    private void addProductNew(DeviceData dd) {
        WaitUtils.waitForElementToBeClickable(driver, pdMedicalDeviceName, TIMEOUT_15_SECOND, false);
        pdMedicalDeviceName.clear();
        String pn = dd.productName;
        if(pn == null){
            pn = "Product";
            dd.productName = pn;
        }
        pdMedicalDeviceName.sendKeys(RandomDataUtils.getRandomTestName(pn));
    }

    private void addActiveImplantableDevice(DeviceData dd) {
        searchByGMDN(dd);
        compliesWithEUDeviceRequirements(true);
        customMade(dd);

       if (dd.customMade.toLowerCase().equals("y")) {
           for (ProductDetail x : dd.listOfProductDetails) {
               productDetailsAIMD(x.name);
           }
       }
    }



    private void addProcedurePackDevice(DeviceData dd) {
        searchByGMDN(dd);
        compliesWithEUDeviceRequirements(true);

        addProductNew(dd);
        addProductModel(dd);
        deviceSterile(dd);

        isBearingCEMarking(dd);
        devicesCompatible(dd);
        PageUtils.uploadDocument(listOfFileUploads.get(0), "SPP_Content_List_Template.xls", 1, 3);
    }


    private void productDetailsAIMD(String deviceName) {
        PageUtils.clickOneOfTheFollowing(driver, addProduct, addProduct2, TIMEOUT_1_SECOND);

        WaitUtils.waitForElementToBeClickable(driver, pdMedicalDeviceNameAIMD, TIMEOUT_5_SECOND);
        pdMedicalDeviceNameAIMD.sendKeys(RandomDataUtils.getRandomTestName(deviceName));

        PageUtils.uploadDocument(fileUpload, "DeviceLabelDoc2.pdf", 1, 3);
        PageUtils.uploadDocument(listOfFileUploads.get(0), "DeviceInstructionForUse1.pdf", 1, 3);

        //Save product label details
        WaitUtils.waitForElementToBeClickable(driver, saveProduct2, TIMEOUT_5_SECOND);
        saveProduct2.click();

    }

    private void productLabelName(DeviceData dd) {
        WaitUtils.waitForElementToBeClickable(driver, txtProductNameLabel, TIMEOUT_5_SECOND, false);
        txtProductNameLabel.sendKeys(RandomDataUtils.getRandomTestName("Label"));

        PageUtils.uploadDocument(fileUpload, "DeviceLabelDoc2.pdf", 1, 3);
        PageUtils.uploadDocument(listOfFileUploads.get(0), "DeviceInstructionForUse1.pdf", 1, 3);

        //Save product label details
        WaitUtils.waitForElementToBeClickable(driver, btnSaveProduct2, TIMEOUT_5_SECOND, false);
        btnSaveProduct2.click();

    }


    private void productLabelName(String labelName) {

        WaitUtils.waitForElementToBeClickable(driver, addProduct2, TIMEOUT_15_SECOND, false);
        addProduct2.click();

        WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
        WaitUtils.nativeWaitInSeconds(1);
        WaitUtils.waitForElementToBeClickable(driver, txtProductNameLabel, TIMEOUT_15_SECOND, false);
        txtProductNameLabel.sendKeys(labelName);

        PageUtils.uploadDocument(fileUpload, "DeviceLabelDoc2.pdf", 1, 3);
        PageUtils.uploadDocument(listOfFileUploads.get(0), "DeviceInstructionForUse1.pdf", 1, 3);

        //Save product label details
        WaitUtils.waitForElementToBeClickable(driver, btnSaveProduct2, TIMEOUT_5_SECOND, false);
        btnSaveProduct2.click();
    }


    private void conformToCTS(DeviceData dd) {
        WaitUtils.waitForElementToBeClickable(driver, radioConformsToCTSYes, TIMEOUT_15_SECOND, false);
        if (dd.CTS.toLowerCase().equals("y")) {
            PageUtils.doubleClick(driver, radioConformsToCTSYes);
            WaitUtils.waitForElementToBeClickable(driver, txtCTSReference, TIMEOUT_15_SECOND, false);
//            txtCTSReference.clear();
//            txtCTSReference.sendKeys("CTS039458430958");
            PageUtils.clearAndTypeText(txtCTSReference, "CTS039458430958", true);
        } else {
            PageUtils.doubleClick(driver, radioConformsToCTSNo);
            WaitUtils.waitForElementToBeClickable(driver, txtDemonstratedCompliance, TIMEOUT_15_SECOND, false);
            PageUtils.clearAndTypeText(txtDemonstratedCompliance, "Demonstrated Compliance", true);
            PageUtils.clearAndTypeText(txtTestingMethod, "Automatically Created From Excel Sheet Test Data", true);
        }
    }

    private void saveProduct(DeviceData dd) {
        WaitUtils.waitForElementToBeClickable(driver, btnSaveProduct2, TIMEOUT_15_SECOND, false);
        btnSaveProduct2.click();
    }

    private void productNewToMarket(DeviceData dd) {
        WaitUtils.waitForElementToBeClickable(driver, radioProductNewYes, TIMEOUT_15_SECOND, false);
        if (dd.isNew.toLowerCase().contains("new")) {
            PageUtils.doubleClick(driver, radioProductNewYes);
        } else {
            PageUtils.doubleClick(driver, radioProductNewNo);
        }
    }

    private void subjectToPerformanceEval(DeviceData dd) {
        WaitUtils.waitForElementToBeClickable(driver, radioSubjectToPerformanceEvalYes, TIMEOUT_15_SECOND, false);
        if (dd.evaluation.toLowerCase().equals("y")) {
            PageUtils.doubleClick(driver, radioSubjectToPerformanceEvalYes);
        } else {
            PageUtils.doubleClick(driver, radioSubjectToPerformanceEvalNo);
        }
    }

    private void addProduct(ProductDetail productDetail) {
        WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
//        WaitUtils.waitForElementToBeClickable(driver, btnSaveProduct, TIMEOUT_15_SECOND, false);
//        btnSaveProduct.click();

        //Wait for form to be visible
        String productName = productDetail.name;
        String productMake = productDetail.make;
        String productModel = productDetail.model;
        WaitUtils.waitForElementToBeClickable(driver, cbxProductName, TIMEOUT_15_SECOND, false);
        if (productName != null || !productName.equals("")) {
            cbxProductName.click();
            WaitUtils.waitForElementToBeClickable(driver, pdProductName, TIMEOUT_15_SECOND, false);
            pdProductName.sendKeys(productName);
        } else if (productMake != null || !productMake.equals("")) {
            cbxMakeAndModel.click();
            WaitUtils.waitForElementToBeClickable(driver, pdProductModel, TIMEOUT_15_SECOND, false);
            pdProductMake.sendKeys(productMake);
            pdProductModel.sendKeys(productModel);
        }
    }

    private void devicesCompatible(DeviceData dd) {
        //Does the system or procedure pack incorporate a medical device that does not bear a CE marking?
        WaitUtils.waitForElementToBeClickable(driver, ppDevicesCompatibleOriginalIntendedUseYes, TIMEOUT_15_SECOND, false);
        if (dd.intended.toLowerCase().equals("y")) {
            PageUtils.doubleClick(driver, ppDevicesCompatibleOriginalIntendedUseYes);
        } else {
            PageUtils.doubleClick(driver, ppDevicesCompatibleOriginalIntendedUseNo);
        }
    }

    private void isBearingCEMarking(DeviceData dd) {
        //Are the chosen combination of medical devices compatible in view of their original intended use?
        WaitUtils.waitForElementToBeClickable(driver, ppPackIncorporatedCEMarkingYes, TIMEOUT_15_SECOND, false);
        if (dd.CE.toLowerCase().equals("y")) {
            PageUtils.doubleClick(driver, ppPackIncorporatedCEMarkingYes);
        } else {
            PageUtils.doubleClick(driver, ppPackIncorporatedCEMarkingNo);
        }
    }

    private void notifiedBody(DeviceData dd) {

        WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
        changeNotifiedBody();
        boolean notifiedBodyOptionsCorrect = isNotifiedBodyListDisplayingCorrectDetails();

        WaitUtils.waitForElementToBeVisible(driver, nb0086BSI, TIMEOUT_5_SECOND, false);
        WaitUtils.waitForElementToBeClickable(driver, nb0086BSI, TIMEOUT_5_SECOND, false);

        //Select notified body
        if (notifiedBodyOptionsCorrect && dd.notifiedBody != null && dd.notifiedBody.toLowerCase().contains("bsi")) {
            PageUtils.singleClick(driver, nb0086BSI);
        } if (notifiedBodyOptionsCorrect && dd.notifiedBody != null && dd.notifiedBody.toLowerCase().contains("0088")) {
            PageUtils.singleClick(driver, nb0088BSI);
        } else if (notifiedBodyOptionsCorrect && dd.notifiedBody != null && dd.notifiedBody.toLowerCase().contains("Other")) {
            PageUtils.clickIfVisible(driver, nbOther);
        } else {
            //PageUtils.clickIfVisible(driver, nb0086BSI);
        }
    }

    private boolean isNotifiedBodyListDisplayingCorrectDetails() {
        //WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
        WaitUtils.waitForElementToBeClickable(driver, nb0086BSI, TIMEOUT_5_SECOND, false);
        boolean numberOfNB = listOfNotifiedBodies.size() >= 6;
        String txt = PageUtils.getText(listOfNotifiedBodies.get(5));
        boolean otherDisplayed = txt.contains("Other");
        return numberOfNB && otherDisplayed;
    }

    private void changeNotifiedBody() {
        try {
            WaitUtils.waitForElementToBeClickable(driver, linkChangeNotifiedBody, TIMEOUT_1_SECOND, false);
            linkChangeNotifiedBody.click();
            WaitUtils.waitForElementToBeClickable(driver, nb0086BSI, TIMEOUT_1_SECOND, false);
            WaitUtils.nativeWaitInSeconds(1);
        } catch (Exception e) {
            //Bug which maintains previous selection of notified body
        }
    }

    private void riskClassificationIVD(DeviceData dd) {
        WaitUtils.waitForElementToBeClickable(driver, ivdIVDGeneral, TIMEOUT_15_SECOND, false);
        WaitUtils.nativeWaitInSeconds(1);

        String lcRiskClassification = dd.riskClassification.toLowerCase();

        if (lcRiskClassification.contains("ivd general")) {
            ivdIVDGeneral.click();
        } else if (lcRiskClassification.contains("list a")) {
            ivdListA.click();
        } else if (lcRiskClassification.contains("list b")) {
            ivdListB.click();
        } else if (lcRiskClassification.contains("self-test")) {
            ivdSelfTest.click();
        }
    }

    private void selectDeviceType(DeviceData dd) {
        WaitUtils.waitForElementToBeClickable(driver, generalMedicalDevice, TIMEOUT_15_SECOND, false);
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
        WaitUtils.waitForElementToBeClickable(driver, radioCustomMadeYes, TIMEOUT_15_SECOND, false);
        if (dd.customMade.toLowerCase().equals("y")) {
            PageUtils.doubleClick(driver, radioCustomMadeYes);
        } else {
            PageUtils.doubleClick(driver, radioCustomMadeNo);
            riskClassification(dd);
        }
    }

    private void deviceMeasuring(DeviceData dd) {
        WaitUtils.waitForElementToBeClickable(driver, radioDeviceMeasuringYes, TIMEOUT_15_SECOND, false);
        if (dd.measuring.toLowerCase().equals("y")) {
            PageUtils.doubleClick(driver, radioDeviceMeasuringYes);
        } else {
            PageUtils.doubleClick(driver, radioDeviceMeasuringNo);
        }
    }

    private void deviceSterile(DeviceData dd) {
        WaitUtils.waitForElementToBeClickable(driver, radioDeviceSterileYes, TIMEOUT_15_SECOND, false);
        if (dd.sterile.toLowerCase().equals("y")) {
            PageUtils.doubleClick(driver, radioDeviceSterileYes);
        } else {
            PageUtils.doubleClick(driver, radioDeviceSterileNo);
        }
    }

    private void riskClassification(DeviceData dd) {
        WaitUtils.waitForElementToBeClickable(driver, radioRiskClass1, TIMEOUT_15_SECOND, false);
        //WaitUtils.waitForElementToBeClickable(driver, nb0086BSI, TIMEOUT_15_SECOND, false);
        String lcRiskClassiffication = dd.riskClassification.toLowerCase();
        if (lcRiskClassiffication != null) {
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
            String[] arrayOfDeviceBecauseTheyKeepBloodyChanging = {
                    "cat", "res", "tis", "sco", "con", "pro"
            };
            int pos = 0;
            String searchFor = arrayOfDeviceBecauseTheyKeepBloodyChanging[pos];
            boolean isErrorMessageDisplayed = false;
            do {

                if(pos == 0){
                    searchFor = dd.device;
                }
                WaitUtils.waitForElementToBeClickable(driver, tbxGMDNDefinitionOrTerm, TIMEOUT_15_SECOND, false);
                tbxGMDNDefinitionOrTerm.clear();
                tbxGMDNDefinitionOrTerm.sendKeys(searchFor);
                WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);

                //Wait for list of items to appear and add it only if its not a duplicate
                WaitUtils.waitForElementToBeClickable(driver, firstGMDNMatchReturnedBySearch, TIMEOUT_5_SECOND, false);
                int randomPosition = RandomDataUtils.getARandomNumberBetween(0, listOfGmdnMatchesReturnedBySearch.size() - 1);
                WebElement element = listOfGmdnMatchesReturnedBySearch.get(randomPosition);

                //Wait for it to be clickable
                WaitUtils.waitForElementToBeClickable(driver, element, TIMEOUT_5_SECOND, false);
                element = element.findElement(By.tagName("a"));
                element.click();

                //If its a duplicate Try again
                isErrorMessageDisplayed = isErrorMessageDisplayed("Duplicate");
                if (isErrorMessageDisplayed) {
                    //Try again
                    pos++;
                    searchFor = arrayOfDeviceBecauseTheyKeepBloodyChanging[pos];
                } else {
                    System.out.println(dd.deviceType + " => " + searchFor);
                }
                PageFactory.initElements(driver, this);
            } while (isErrorMessageDisplayed);

            //Default is search by gmdn term or definition : This removed 03/02/2017 push
            //previousGMDNSelection(dd);

        }
    }

    public boolean isErrorMessageDisplayed(String message) {
        try {
            //WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
            WaitUtils.waitForElementToBeVisible(driver, By.cssSelector(".FieldLayout---field_error"), 3);
            WaitUtils.waitForElementToBeClickable(driver, By.cssSelector(".FieldLayout---field_error"), 3);
            boolean isDisplayed = false;
            for (WebElement msg : errorMessages) {
                String txt = msg.getText();
                System.out.println("Error message : " + txt);
                isDisplayed = txt.toLowerCase().contains(message.toLowerCase());
                if (isDisplayed) {
                    break;
                }
            }
            return isDisplayed;
        } catch (Exception e) {
            return false;
        }
    }


    public boolean isOptionToAddAnotherDeviceVisible() {
        //WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
        WaitUtils.nativeWaitInSeconds(2);
        try {
            WaitUtils.waitForElementToBeClickable(driver, btnAddAnotherDevice, TIMEOUT_15_SECOND, false);
            boolean isVisible = btnAddAnotherDevice.isDisplayed() && btnAddAnotherDevice.isEnabled();
            return isVisible;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isOptionToReviewYourOrderVisible() {
        try {
            WaitUtils.waitForElementToBeClickable(driver, btnReviewYourOrder, TIMEOUT_15_SECOND, false);
            boolean isVisible = btnReviewYourOrder.isDisplayed() && btnReviewYourOrder.isEnabled();
            return isVisible;
        } catch (Exception e) {
            return false;
        }
    }

    public AddDevices proceedToPayment() {
        //WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
        WaitUtils.waitForElementToBeClickable(driver, cbxConfirmInformation, TIMEOUT_5_SECOND, false);
        cbxConfirmInformation.click();
        WaitUtils.waitForElementToBeClickable(driver, btnProceedToPayment, TIMEOUT_15_SECOND, false);
        btnProceedToPayment.click();
        System.out.println("Proceed to payment");
        return new AddDevices(driver);
    }

    public AddDevices confirmPayment() {
        //WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
        WaitUtils.waitForElementToBeClickable(driver, bthSubmitConfirm, TIMEOUT_15_SECOND, false);
        bthSubmitConfirm.click();
        System.out.println("Submit for registration");
        return new AddDevices(driver);
    }

    public ExternalHomePage finish() {
        //WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
        WaitUtils.nativeWaitInSeconds(1);
        WaitUtils.waitForElementToBeClickable(driver, btnFinish, TIMEOUT_15_SECOND, false);
        btnFinish.click();
        return new ExternalHomePage(driver);
    }

    public ManufacturerList backToService() {
        //WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
        WaitUtils.waitForElementToBeClickable(driver, linkBackToService, TIMEOUT_15_SECOND, false);
        linkBackToService.click();
        return new ManufacturerList(driver);
    }

    public boolean isGMDNValueDisplayed(DeviceData data) {
        WaitUtils.waitForElementToBeClickable(driver, btnAddAnotherDevice, TIMEOUT_15_SECOND, false);
        WaitUtils.waitForElementToBeClickable(driver, btnProceedToPayment, TIMEOUT_15_SECOND, false);
        boolean isDisplayed = false;
        String valueToCheck = "";

        if (data.device != null) {
            valueToCheck = data.device.toLowerCase();
        } else {
            //valueToCheck = data.gmdnCode;
        }

        for (WebElement el : listOfGMDNLinksInSummary) {
            String text = el.getText();
            if (text.toLowerCase().contains(valueToCheck)) {
                isDisplayed = true;
                break;
            }
        }

        return isDisplayed;
    }

    public boolean isGMDNValueDisplayed(String valueToCheck) {
        boolean isCorrect = false;

        for (WebElement el : listOfGMDNLinksInSummary) {
            String text = el.getText();
            if (text.contains(valueToCheck)) {
                isCorrect = true;
                break;
            }
        }

        return isCorrect;
    }

    public AddDevices addAnotherDevice() {
        WaitUtils.waitForElementToBeClickable(driver, btnAddAnotherDevice, TIMEOUT_15_SECOND, false);
        btnAddAnotherDevice.click();
        return new AddDevices(driver);
    }


//    public AddDevices viewDeviceWithGMDNValue(String gmdnCode) {
//        //boolean isNumeric = AssertUtils.isNumeric(gmdnCode);
//        WebElement el = CommonUtils.getElementWithLink(listOfGMDNLinksInSummary, gmdnCode);
//        WaitUtils.waitForElementToBeClickable(driver, el, TIMEOUT_15_SECOND, false);
//        el.click();
//        return new AddDevices(driver);
//    }

    public AddDevices removeSelectedDevice() {
        WaitUtils.waitForElementToBeClickable(driver, btnRemove, TIMEOUT_15_SECOND, false);
        btnRemove.click();
        return new AddDevices(driver);
    }

    public AddDevices proceedToReview() {
        //WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
        WaitUtils.nativeWaitInSeconds(2);
        WaitUtils.waitForElementToBeClickable(driver, btnProceedToReview, TIMEOUT_15_SECOND, false);
        btnProceedToReview.click();
        System.out.println("Proceed to review before payment");
        return new AddDevices(driver);
    }

    public AddDevices saveDevice() {
        WaitUtils.waitForElementToBeClickable(driver, btnSaveProgress, TIMEOUT_15_SECOND, false);
        PageUtils.doubleClick(driver, btnSaveProgress);
        return new AddDevices(driver);
    }


    public AddDevices enterPaymentDetails(String paymentMethod) {
        //WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
        WaitUtils.waitForElementToBeClickable(driver, ddAddressBox, TIMEOUT_5_SECOND);

        //Select billing address:
        PageUtils.selectFromDropDown(driver, ddAddressBox , "Registered Address", false);

        if(paymentMethod.toLowerCase().contains("world")){
            paymentWorldPay.click();
        }else if(paymentMethod.toLowerCase().contains("bacs")){
            paymentBACS.click();
            WaitUtils.isPageLoadingComplete(driver, TIMEOUT_PAGE_LOAD);
            PageUtils.uploadDocument(multiFileUpload, "CompletionOfTransfer1.pdf", 1, 3);
        }

        //Complete the application
        btnCompleteApplication.click();
        return new AddDevices(driver);
    }

    public String getApplicationReferenceNumber() {
        WaitUtils.waitForElementToBeClickable(driver, txtApplicationReference, TIMEOUT_10_SECOND);
        return txtApplicationReference.getText();
    }
}
