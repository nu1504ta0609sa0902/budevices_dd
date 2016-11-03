package com.mhra.mdcm.devices.dd.appian.pageobjects.business;

import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by TPD_Auto
 */

public class NewsPage extends _Page {

    @Autowired
    public NewsPage(WebDriver driver) {
        super(driver);
    }
}
