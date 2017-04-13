package com.mhra.mdcm.devices.dd.appian._test.junit._others;

import com.mhra.mdcm.devices.dd.appian.utils.selenium.others.FileUtils;
import org.openqa.selenium.WebDriver;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;

/**
 * Created by a-Uddinn on 4/7/2017.
 */
public class ProxyAuthentication implements Runnable {

    final WebDriver driver;

    public ProxyAuthentication(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public void run() {
        try {
            login();
        } catch (Exception ex) {
            System.out.println("Error in Login Thread: " + ex.getMessage());
        }
    }

    public void login() throws Exception {
        System.out.println("Trying to enter proxy login details");
        //wait - increase this wait period if required
        Thread.sleep(4000);

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

        String userName = FileUtils.getSpecificPropertyFromFile("envs" + File.separator + "mhratest.properties", "proxy.username");
        String password = FileUtils.getSpecificPropertyFromFile("envs" + File.separator + "mhratest.properties", "proxy.password");

        //Enter user name by ctrl-v
        StringSelection username = new StringSelection(userName);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(username, null);
        rb.keyPress(KeyEvent.VK_CONTROL);
        rb.keyPress(KeyEvent.VK_V);
        rb.keyRelease(KeyEvent.VK_V);
        rb.keyRelease(KeyEvent.VK_CONTROL);

        //tab to password entry field
        rb.keyPress(KeyEvent.VK_TAB);
        rb.keyRelease(KeyEvent.VK_TAB);
        Thread.sleep(1000);

        //Enter password by ctrl-v
        StringSelection pwd = new StringSelection(password);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(pwd, null);
        rb.keyPress(KeyEvent.VK_CONTROL);
        rb.keyPress(KeyEvent.VK_V);
        rb.keyRelease(KeyEvent.VK_V);
        rb.keyRelease(KeyEvent.VK_CONTROL);

        //press enter
        rb.keyPress(KeyEvent.VK_ENTER);
        rb.keyRelease(KeyEvent.VK_ENTER);

        //wait
        Thread.sleep(1000);

        //
        driver.manage().window().maximize();
    }
}