package com.mhra.mdcm.devices.dd.appian.utils.selenium.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by TPD_Auto on 20/10/2016.
 */
public class CommonUtils {


    public static  boolean areLinksVisible(WebDriver driver, String delimitedLinks) {
        boolean allLinksVisible = true;
        String[] links = delimitedLinks.split(",");
        for(String aLink: links){
            WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText(aLink), 10, false);
            boolean isDisplayed = driver.findElement(By.partialLinkText(aLink)).isDisplayed();
            if(!isDisplayed){
                allLinksVisible = false;
                System.out.println("Link not visible : " + aLink);
                break;
            }
        }

        return allLinksVisible;
    }


    public static boolean areLinksClickable(WebDriver driver, String delimitedLinks) {
        boolean allLinksClickable = true;
        String[] links = delimitedLinks.split(",");
        for(String aLink: links){
            try{
                WaitUtils.waitForElementToBeClickable(driver, By.partialLinkText(aLink), 10, false);
            }catch (Exception e){
                allLinksClickable = false;
                System.out.println("Link not clickable : " + aLink);
                break;
            }
        }

        return allLinksClickable;
    }
}
