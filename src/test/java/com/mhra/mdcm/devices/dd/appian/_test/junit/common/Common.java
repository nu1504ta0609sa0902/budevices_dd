package com.mhra.mdcm.devices.dd.appian._test.junit.common;

import com.mhra.mdcm.devices.dd.appian._test.junit.rules.LoggingRule;
import com.mhra.mdcm.devices.dd.appian.pageobjects.business.*;
import com.mhra.mdcm.devices.dd.appian.pageobjects.business.sections.*;
import com.mhra.mdcm.devices.dd.appian.pageobjects.external.PortalPage;
import org.junit.Rule;

/**
 * Created by TPD_Auto on 07/11/2016.
 */
public class Common {

    @Rule
    public LoggingRule lr = new LoggingRule();

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
