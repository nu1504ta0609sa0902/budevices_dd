package com.mhra.mdcm.devices.dd.appian.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author TPD_Auto
 */
public class _Page {

    public static final int TIMEOUT_PAGE_LOAD = 1;

    public static final boolean USE_DEBUG_TIME = true;
    public static final int TIMEOUT_1_SECOND = 1;
    public static final int TIMEOUT_3_SECOND = 3;
    public static final int TIMEOUT_5_SECOND = 5;
    public static final int TIMEOUT_10_SECOND = 10;
    public static final int TIMEOUT_15_SECOND = 15;
    public static final int TIMEOUT_20_SECOND = 20;
    public static final int TIMEOUT_30_SECOND = 30;
    public static final int TIMEOUT_40_SECOND = 40;
    public static final int TIMEOUT_50_SECOND = 50;
    public static final int TIMEOUT_60_SECOND = 60;
    public static final int TIMEOUT_DEFAULT = TIMEOUT_40_SECOND;

    public final Logger log = LoggerFactory.getLogger(_Page.class);


    public WebDriver driver;


    public _Page(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public WebDriver getDriver() {
        return driver;
    }

    public String getTitle(){
        return driver.getTitle();
    }
}
