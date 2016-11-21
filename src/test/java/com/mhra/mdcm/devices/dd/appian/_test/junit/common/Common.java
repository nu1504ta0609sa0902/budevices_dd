package com.mhra.mdcm.devices.dd.appian._test.junit.common;

import com.mhra.mdcm.devices.dd.appian.pageobjects.business.*;
import com.mhra.mdcm.devices.dd.appian.pageobjects.business.sections.*;
import com.mhra.mdcm.devices.dd.appian.pageobjects.external.PortalPage;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by TPD_Auto on 07/11/2016.
 */
public class Common {
    static {
        //This helps with creating log files each time we run the tests, remember append=false needs to be set
        Calendar ins = Calendar.getInstance();
        int hour = ins.get(Calendar.HOUR_OF_DAY);
        int min = ins.get(Calendar.MINUTE);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.setProperty("current.date", dateFormat.format(new Date()) + "_" + hour + "_" + min);
    }

    public static final Logger log = LoggerFactory.getLogger(Common.class);
    public static long totalTime = 0;

    @ClassRule
    public static TestRule suiteWatchMan = new TestWatcher() {
        @Override
        protected void finished(Description description) {
            log.warn("\nSuite completed!"); // insert actual logic here
            logTotalTime("Total time taken : ", totalTime, description);
        }

        private void logTotalTime(String message, long totalTime, Description description) {
            long seconds = totalTime / 1000;
            int min = (int) (seconds / 60);
            int sec = (int) (seconds % 60);
            //Log pass/fail message for the test
            log.warn(message + min + " min, " + sec + " seconds for test : " + description);
        }
    };

    @Rule
    public TestWatcher eachTestWatchMan = new TestWatcher() {
        long timeStart = System.currentTimeMillis();

        @Override
        protected void starting(Description description) {
            super.starting(description);
            timeStart = System.currentTimeMillis();
        }

        @Override
        protected void failed(Throwable e, Description description) {
            //log.warn("Failed : " + description);
            logTime("Failed,", description);
            log.warn("Error : " + e.getMessage());
        }

        @Override
        protected void succeeded(Description description) {
            //log.warn("Passed : " + description);
            logTime("Passed,", description);
        }

        private void logTime(String message, Description description) {
            long timeEnd = System.currentTimeMillis();
            long diffTime = (timeEnd - timeStart);
            long seconds = diffTime / 1000;
            int min = (int) (seconds / 60);
            int sec = (int) (seconds % 60);

            //Log pass/fail message for the test
            log.warn(message + " in " + min + " min, " + sec + " seconds for test : " + description);
            totalTime = totalTime + diffTime;
        }
    };

    //Pages
    public NewsPage newsPage;
    public TasksPage tasksPage;
    public RecordsPage recordsPage;
    public ReportsPage reportsPage;
    public ActionsPage actionsPage;

    public Accounts accounts;
    public Devices devices;
    public AllOrganisations allOrganisations;
    public PortalPage portalPage;
    public CreateTestsData createTestsData;
    public TaskSection taskSection;
    public AllDevices allDevices;
    public Products products;
    public AllProducts allProducts;
}
