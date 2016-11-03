package com.mhra.mdcm.devices.dd.appian;

import com.mhra.mdcm.devices.dd.appian._test.junit.smoke.SmokeTestsBusiness;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({SmokeTestsBusiness.class} )
//@Suite.SuiteClasses({SmokeTestsBusiness.class, SmokeTestsManufacturers.class, SmokeTestsAuthorisedRep.class} )
public class RunAllSmokeTest {
}
