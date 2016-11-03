package com.mhra.mdcm.devices.dd.appian.pageobjects.external;

import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.CommonUtils;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by TPD_Auto
 */
@Component
public class PortalPage extends _Page {

    @Autowired
    public PortalPage(WebDriver driver) {
        super(driver);
    }

    public boolean areLinksVisible(String delimitedLinks) {
        boolean visible = CommonUtils.areLinksVisible(driver, delimitedLinks);
        return visible;
    }

    public boolean areLinksClickable(String delimitedLinks) {
        boolean clickable = CommonUtils.areLinksClickable(driver, delimitedLinks);
        return clickable;
    }
}
