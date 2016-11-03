package com.mhra.mdcm.devices.dd.appian.pageobjects.external;

import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by TPD_Auto
 */
@Component
public class MyAccountPage extends _Page {

    @Autowired
    public MyAccountPage(WebDriver driver) {
        super(driver);
    }
}
