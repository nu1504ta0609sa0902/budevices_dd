package com.mhra.mdcm.devices.dd.appian._test.junit.common;

import com.mhra.mdcm.devices.dd.appian._test.junit.rules.LoggingRule;
import com.mhra.mdcm.devices.dd.appian.pageobjects.business.*;
import com.mhra.mdcm.devices.dd.appian.pageobjects.business.sections.*;
import com.mhra.mdcm.devices.dd.appian.pageobjects.external.PortalPage;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by TPD_Auto on 07/11/2016.
 */
public class Common {

    public static final Logger log = LoggerFactory.getLogger(Common.class);

    @Rule
    public LoggingRule lr = new LoggingRule();

    @Rule
    public TestWatcher watchman= new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            log.warn("Error : " + e.getMessage());
            log.warn("Failed : " + description);
        }

        @Override
        protected void succeeded(Description description) {
            log.warn("Passed : " + description);
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
    public Products products;
    public AllOrganisations allOrganisations;
    public PortalPage portalPage;
    public CreateTestsData createTestsData;
    public TaskSection taskSection;
}
