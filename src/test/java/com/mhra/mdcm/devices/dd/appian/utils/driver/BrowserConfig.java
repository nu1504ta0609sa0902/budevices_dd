package com.mhra.mdcm.devices.dd.appian.utils.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Use this or the cucumber.xml don't use both
 *  mac : /Users/tayyibah/Downloads/MyProjects/chromedriver
 * @author tayyibah
 */
public class BrowserConfig {

    public String browser;
    public String seleniumExecutableLocation = "C:\\Selenium";

    public WebDriver getDriver() {

        //Override if necessary the location of selenium executable
        String exeLocation = System.getProperty("exe.location");
        if(exeLocation!=null && !exeLocation.trim().equals("")){
            seleniumExecutableLocation = exeLocation;
        }
        System.out.println("Location of EXE : " + seleniumExecutableLocation);

        if (browser == null) {
            //Should be picked up
            browser = System.getProperty("current.browser");
        }



        if (browser != null) {
            System.out.println("Browser : " + browser);

            //Firefox
            if (browser.equals("ff") || browser.equals("firefox")) {
                DesiredCapabilities gcCap = getFirefoxDesiredCapabilities(true);
                return new FirefoxDriver(gcCap);
            }

            //Chrome
            else if (browser.equals("gc") || browser.equals("chrome")) {
                DesiredCapabilities gcCap = getGoogleChromeDesiredCapabilities();
                return new ChromeDriver(gcCap);
            }
            //IE
            else if (browser.equals("ie") || browser.equals("internetexplorer")) {
                DesiredCapabilities ieCap = getIEDesiredCapabilities();
                return new InternetExplorerDriver(ieCap);
            } else if (browser.equals("ie2") || browser.equals("internetexplorer2")) {
                DesiredCapabilities ieCap = getIEDesiredCapabilities();
                return new EdgeDriver(ieCap);
            }
            //PhantomJs
            else if (browser.equals("pjs") || browser.equals("phantom") || browser.equals("phantomjs")) {
                DesiredCapabilities ieCap = getPJSDesiredCapabilities();
                return new PhantomJSDriver(ieCap);
            }

            //Selenium Grid with Chrome
            else if (browser.equals("sgc") || browser.equals("SGC")) {
                try {
                    DesiredCapabilities gcCap = getGoogleChromeDesiredCapabilities();
                    return new RemoteWebDriver(new URL("http://mdwin1008cog4:4444/wd/hub"), gcCap);
                } catch (MalformedURLException e) {
                    e.printStackTrace();

                    //If exception
                    try {
                        DesiredCapabilities ieCap = getIEDesiredCapabilities();
                        return new RemoteWebDriver(new URL("http://mdwin1008cog4:4444/wd/hub"), ieCap);
                    } catch (MalformedURLException e1) {
                        e1.printStackTrace();
                    }
                }

                //No Grid
                DesiredCapabilities ieCap = getIEDesiredCapabilities();
                return new InternetExplorerDriver(ieCap);
            }
            //Defaults to project default IE
            else {
                DesiredCapabilities ieCap = getIEDesiredCapabilities();
                return new InternetExplorerDriver(ieCap);
            }
        } else {
            System.out.println("Using DEFAULT BROWSER IE, -Dcurrent.browser not set");
            DesiredCapabilities ieCap = getIEDesiredCapabilities();
            return new InternetExplorerDriver(ieCap);
        }
    }

    private DesiredCapabilities getHtmlUnitDesiredCapabilities() {
        DesiredCapabilities capabilities = DesiredCapabilities.htmlUnit();
        capabilities.setBrowserName("Mozilla/5.0 (X11; Linux x86_64; rv:49.0) Gecko/20100101 Firefox/49.0");
        capabilities.setJavascriptEnabled(true);
        return capabilities;
    }

    private DesiredCapabilities getFirefoxDesiredCapabilities(boolean isMarionette) {
        System.setProperty("webdriver.gecko.driver", seleniumExecutableLocation + "\\firefox\\geckodriver.exe");
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        if (isMarionette)
            capabilities.setCapability("marionette", true);
        System.out.println("Location - exe : " + System.getProperty("webdriver.gecko.driver"));
        return capabilities;
    }

    private DesiredCapabilities getGoogleChromeDesiredCapabilities() {
        System.setProperty("webdriver.chrome.driver", seleniumExecutableLocation + "\\chrome\\chromedriver.exe");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();

        //Proxy proxy = NetworkUtils.getProxy();
        //capabilities.setCapability(CapabilityType.PROXY, proxy);
        //capabilities.setCapability("chrome.switches", Arrays.asList("--proxy-server=http://mca\\uddinn:@10.2.22.60:8000"));

        ChromeOptions options = new ChromeOptions();
        options.addArguments("test-type");
        options.addArguments("disable-popup-blocking");
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        return capabilities;
    }

    /**
     * You dont need to launch:
     * - Selenium Server or PhantomJS
     * - Should work out of the box
     *
     * @return
     */
    private DesiredCapabilities getPJSDesiredCapabilities() {
        System.setProperty("webdriver.ie.driver", seleniumExecutableLocation + "\\phantomjs\\phantomjs\\bin\\phantomjs.exe");
        String path = System.getProperty("phantomjs.binary.path");
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);
        caps.setCapability("takesScreenshot", true);
        caps.setCapability("browserName", "phantomjs");
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[]{
                //"--webdriver=8910",
                "--ssl-protocol=any",
                "--ignore-ssl-errors=true",
                "--webdriver-logfile=/bu/log/phantomjsdriver.log",
                "--webdriver-loglevel=NONE"
        });

        //Only required if not in the path
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, path);
        return caps;
    }

    private DesiredCapabilities getIEDesiredCapabilities() {

        System.setProperty("webdriver.ie.driver", seleniumExecutableLocation + "\\ie32\\IEDriverServer.exe");
        DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();

        //ieCapabilities.setCapability("unexpectedAlertBehaviour", "accept");
        //ieCapabilities.setCapability("enablePersistentHover", false);
        //ieCapabilities.setCapability("nativeEvents", true);
        ieCapabilities.setCapability("ignoreProtectedModeSettings", true);
        ieCapabilities.setCapability("disable-popup-blocking", false);

        ieCapabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
        ieCapabilities.setCapability(InternetExplorerDriver.UNEXPECTED_ALERT_BEHAVIOR, true);
        ieCapabilities.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING,false);
        ieCapabilities.setCapability(InternetExplorerDriver.NATIVE_EVENTS, true);
        ieCapabilities.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, true);
        ieCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        ieCapabilities.setJavascriptEnabled(true);

        //ieCapabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, "ignore");
        return ieCapabilities;
    }


}