package com.mhra.mdcm.devices.dd.appian.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author TPD_Auto
 */
public class _Page {

    public static final boolean USE_DEBUG_TIME = true;
    public static final int TIMEOUT_VERY_SMALL = 1;
    public static final int TIMEOUT_SMALL = 5;
    public static final int TIMEOUT_MEDIUM = 15;
    public static final int TIMEOUT_HIGH = 30;
    public static final int TIMEOUT_VERY_HIGH = 60;
    public static final int TIMEOUT_DEFAULT = TIMEOUT_MEDIUM;

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
