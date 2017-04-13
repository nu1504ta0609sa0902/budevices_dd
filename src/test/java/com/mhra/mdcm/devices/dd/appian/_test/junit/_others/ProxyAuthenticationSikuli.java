package com.mhra.mdcm.devices.dd.appian._test.junit._others;

import org.openqa.selenium.WebDriver;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

/**
 * Created by a-Uddinn on 4/7/2017.
 */
public class ProxyAuthenticationSikuli{

    final WebDriver driver;
    final String url;

    public ProxyAuthenticationSikuli(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.url = baseUrl;
    }

    public void login() throws Exception {

        //Now try to enter some value
        System.out.println("Trying to enter proxy login details via ROBOT API");
        //wait - increase this wait period if required
        Thread.sleep(5000);

        //create robot for keyboard operations
        Robot rb = new Robot();

        //Tab to a different window
        String inJenkinsWindowLoosesFocusOnLauch = System.getProperty("using.ci");
        System.out.println("Using CI : " + inJenkinsWindowLoosesFocusOnLauch);
        if(inJenkinsWindowLoosesFocusOnLauch!=null) {
            System.out.println("In Jenkins : Using CI ");
            rb.keyPress(KeyEvent.VK_WINDOWS);
            rb.keyPress(KeyEvent.VK_DOWN);
            rb.keyRelease(KeyEvent.VK_WINDOWS);
            rb.keyRelease(KeyEvent.VK_DOWN);
        }else{
            System.out.println("Running from CMD or IDE");
        }

        //Find fields and enter values
        Screen screen = new Screen();
        Pattern image = new Pattern("C:\\gmail.PNG");

        //wait
        Thread.sleep(1000);

        //maximise
        driver.manage().window().maximize();
    }
}